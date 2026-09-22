// modules/servicos/hooks/use-vincular-pessoas-servico.ts

"use client";

import { useEffect, useMemo, useState } from "react";
import { familiaService } from "../../familias/services/familia-service";
import type { FamiliaDTO, PessoaDTO } from "../../familias/types/familia";
import { vinculoServicoService } from "../services/vinculo-servico-service";
import type {
  VinculoLoteResposta,
  VinculoPessoaServico,
} from "../types/vinculo-servico";
import type { Servico } from "../types/servico";

export interface PessoaSelecionavel extends PessoaDTO {
  familiaId: number;
}

type Etapa = "lista" | "confirmarDesligamento";

interface DesligamentoLoteResultado {
  sucesso: { vinculoId: number }[];
  falhas: { vinculoId: number; motivo: string }[];
}

export function useVincularPessoasServico(servico: Servico | null, open: boolean) {
  // --- vínculos ativos ---
  const [vinculosAtivos, setVinculosAtivos] = useState<VinculoPessoaServico[]>([]);
  const [loadingVinculos, setLoadingVinculos] = useState(false);
  const [vinculoIdsParaDesligar, setVinculoIdsParaDesligar] = useState<Set<number>>(new Set());

  // --- busca / novos vínculos ---
  const [familias, setFamilias] = useState<FamiliaDTO[]>([]);
  const [loadingFamilias, setLoadingFamilias] = useState(false);
  const [busca, setBusca] = useState("");
  const [familiaSelecionada, setFamiliaSelecionada] = useState<FamiliaDTO | null>(null);
  const [pessoaIdsParaVincular, setPessoaIdsParaVincular] = useState<Set<number>>(new Set());

  // --- fluxo ---
  const [etapa, setEtapa] = useState<Etapa>("lista");
  const [motivoSaida, setMotivoSaida] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [resultado, setResultado] = useState<{
    vincular?: VinculoLoteResposta;
    desligar?: DesligamentoLoteResultado;
  } | null>(null);
  const [erro, setErro] = useState<string | null>(null);

  const formKey = `${servico?.id ?? "none"}-${open}`;
  const [prevFormKey, setPrevFormKey] = useState(formKey);
  if (formKey !== prevFormKey) {
    setPrevFormKey(formKey);
    setBusca("");
    setFamiliaSelecionada(null);
    setPessoaIdsParaVincular(new Set());
    setVinculoIdsParaDesligar(new Set());
    setEtapa("lista");
    setMotivoSaida("");
    setResultado(null);
    setErro(null);
  }

  useEffect(() => {
    if (!open || !servico) return;
    setLoadingVinculos(true);
    vinculoServicoService
      .listarAtivosPorServico(servico.id)
      .then(setVinculosAtivos)
      .catch(() => setErro("Erro ao carregar pessoas vinculadas"))
      .finally(() => setLoadingVinculos(false));
  }, [open, servico]);

  useEffect(() => {
    if (!open) return;
    setLoadingFamilias(true);
    familiaService
      .listar()
      .then(setFamilias)
      .catch(() => setErro("Erro ao carregar famílias"))
      .finally(() => setLoadingFamilias(false));
  }, [open]);

  const pessoaIdsJaVinculadas = useMemo(
    () => new Set(vinculosAtivos.map((v) => v.pessoaId)),
    [vinculosAtivos]
  );

  const familiasFiltradas = useMemo(() => {
    const termo = busca.trim().toLowerCase();
    if (!termo) return [];
    return familias.filter((f) => {
      const nomeRef = f.pessoaReferencia?.nome?.toLowerCase() ?? "";
      const cpfRef = f.pessoaReferencia?.cpf ?? "";
      const cadunico = f.codigoCadunico?.toLowerCase() ?? "";
      return nomeRef.includes(termo) || cpfRef.includes(termo) || cadunico.includes(termo);
    });
  }, [busca, familias]);

  const pessoasDaFamilia: PessoaSelecionavel[] = useMemo(() => {
    if (!familiaSelecionada) return [];
    const todas = [familiaSelecionada.pessoaReferencia, ...familiaSelecionada.membrosDaFamilia];
    return todas
      .filter((p) => !!p?.id && !pessoaIdsJaVinculadas.has(p.id!))
      .map((p) => ({ ...p, familiaId: familiaSelecionada.id! }));
  }, [familiaSelecionada, pessoaIdsJaVinculadas]);

  const totalAlteracoes = pessoaIdsParaVincular.size + vinculoIdsParaDesligar.size;

  function toggleDesligar(vinculoId: number) {
    setVinculoIdsParaDesligar((prev) => {
      const novo = new Set(prev);
      if (novo.has(vinculoId)) novo.delete(vinculoId);
      else novo.add(vinculoId);
      return novo;
    });
  }

  function selecionarFamilia(familia: FamiliaDTO) {
    setFamiliaSelecionada(familia);
  }

  function voltarParaBusca() {
    setFamiliaSelecionada(null);
  }

  function togglePessoaVincular(pessoaId: number) {
    setPessoaIdsParaVincular((prev) => {
      const novo = new Set(prev);
      if (novo.has(pessoaId)) novo.delete(pessoaId);
      else novo.add(pessoaId);
      return novo;
    });
  }

  function selecionarTodosDaFamilia() {
    setPessoaIdsParaVincular((prev) => {
      const novo = new Set(prev);
      pessoasDaFamilia.forEach((p) => novo.add(p.id!));
      return novo;
    });
  }

  function limparSelecaoNovos() {
    setPessoaIdsParaVincular(new Set());
  }

  function iniciarSalvar() {
    if (totalAlteracoes === 0) return;
    if (vinculoIdsParaDesligar.size > 0) {
      setEtapa("confirmarDesligamento");
    } else {
      void executarSalvar();
    }
  }

  function cancelarConfirmacao() {
    setEtapa("lista");
  }

  async function executarSalvar(): Promise<void> {
    if (!servico) return;
    try {
      setSubmitting(true);
      setErro(null);

      let vincularResp: VinculoLoteResposta | undefined;
      let desligarResp: DesligamentoLoteResultado | undefined;

      const tarefas: Promise<unknown>[] = [];

      if (pessoaIdsParaVincular.size > 0) {
        tarefas.push(
          vinculoServicoService
            .vincularEmLote({
              servicoId: servico.id,
              pessoaIds: Array.from(pessoaIdsParaVincular),
            })
            .then((r) => (vincularResp = r))
        );
      }

      if (vinculoIdsParaDesligar.size > 0) {
        tarefas.push(
          vinculoServicoService
            .desligarEmLote(Array.from(vinculoIdsParaDesligar), {
              motivoSaida: motivoSaida.trim(),
              dataSaida: new Date().toISOString().slice(0, 10), // YYYY-MM-DD
            })
            .then((r) => (desligarResp = r))
        );
      }

      await Promise.all(tarefas);

      setResultado({ vincular: vincularResp, desligar: desligarResp });
      setPessoaIdsParaVincular(new Set());
      setVinculoIdsParaDesligar(new Set());
      setMotivoSaida("");
      setEtapa("lista");

      const atualizados = await vinculoServicoService.listarAtivosPorServico(servico.id);
      setVinculosAtivos(atualizados);
    } catch (err: unknown) {
      setErro(err instanceof Error ? err.message : "Erro ao salvar alterações de vínculo");
    } finally {
      setSubmitting(false);
    }
  }

  return {
    vinculosAtivos,
    loadingVinculos,
    vinculoIdsParaDesligar,
    toggleDesligar,
    busca,
    setBusca,
    loadingFamilias,
    familiasFiltradas,
    familiaSelecionada,
    pessoasDaFamilia,
    pessoaIdsParaVincular,
    selecionarFamilia,
    voltarParaBusca,
    togglePessoaVincular,
    selecionarTodosDaFamilia,
    limparSelecaoNovos,
    etapa,
    motivoSaida,
    setMotivoSaida,
    totalAlteracoes,
    submitting,
    resultado,
    erro,
    iniciarSalvar,
    cancelarConfirmacao,
    executarSalvar,
  };
}
