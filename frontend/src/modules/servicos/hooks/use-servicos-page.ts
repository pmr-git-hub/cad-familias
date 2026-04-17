// modules/servicos/hooks/use-servicos-page.ts

"use client";

import { useState } from "react";
import { useServicos } from "./use-servicos";
import { useEquipamentosOpcoes } from "@/modules/tecnicos/hooks/use-equipamentos-opcoes"; 
import type { Servico, ServicoFormData } from "../types/servico";

export function useServicosPage() {
  const { servicos, loading, error, criar, atualizar, mudarStatus } =
    useServicos();
  const { equipamentos, loading: loadingEquipamentos } =
    useEquipamentosOpcoes();

  const [busca, setBusca] = useState("");
  const [formOpen, setFormOpen] = useState(false);
  const [editando, setEditando] = useState<Servico | null>(null);
  const [mudandoStatus, setMudandoStatus] = useState<Servico | null>(null);
  const [submitting, setSubmitting] = useState(false);

  const equipamentoOpcoes = equipamentos.map((e) => ({
    id: e.id,
    nome: e.nome,
  }));

  const filtrados = servicos.filter((s) => {
    const termo = busca.toLowerCase();
    const nomeEquipamento =
      equipamentoOpcoes
        .find((e) => e.id === s.equipamentoId)
        ?.nome.toLowerCase() ?? "";

    return (
      s.nome.toLowerCase().includes(termo) ||
      nomeEquipamento.includes(termo) ||
      (s.publicoAlvo?.toLowerCase().includes(termo) ?? false) ||
      (s.diaSemana?.toLowerCase().includes(termo) ?? false)
    );
  });

  function getEquipamentoNome(equipamentoId: number): string {
    return equipamentoOpcoes.find((e) => e.id === equipamentoId)?.nome ?? "—";
  }

  function formatFaixaEtaria(min: number | null, max: number | null): string {
    if (min != null && max != null) return `${min} a ${max} anos`;
    if (min != null) return `A partir de ${min} anos`;
    if (max != null) return `Até ${max} anos`;
    return "—";
  }

  async function handleSubmit(data: ServicoFormData): Promise<void> {
    try {
      setSubmitting(true);

      const payload = {
        equipamentoId: data.equipamentoId!,
        nome: data.nome,
        descricao: data.descricao || undefined,
        publicoAlvo: data.publicoAlvo || undefined,
        faixaEtariaMin: data.faixaEtariaMin
          ? parseInt(data.faixaEtariaMin)
          : undefined,
        faixaEtariaMax: data.faixaEtariaMax
          ? parseInt(data.faixaEtariaMax)
          : undefined,
        diaSemana: data.diaSemana || undefined,
        horario: data.horario || undefined,
      };

      if (editando) {
        await atualizar(editando.id, payload);
      } else {
        await criar({ ...payload, ativo: true });
      }

      setFormOpen(false);
      setEditando(null);
    } catch (err) {
      console.error("Erro ao salvar serviço:", err);
    } finally {
      setSubmitting(false);
    }
  }

  async function handleMudarStatus(): Promise<void> {
    if (!mudandoStatus) return;
    try {
      setSubmitting(true);
      await mudarStatus(mudandoStatus.id, mudandoStatus.ativo);
      setMudandoStatus(null);
    } catch (err) {
      console.error("Erro ao mudar status do serviço:", err);
    } finally {
      setSubmitting(false);
    }
  }

  function handleEditar(servico: Servico): void {
    setEditando(servico);
    setFormOpen(true);
  }

  function handleNovo(): void {
    setEditando(null);
    setFormOpen(true);
  }

  function handleFecharForm(): void {
    setFormOpen(false);
    setEditando(null);
  }

  function handleFecharMudarStatus(): void {
    setMudandoStatus(null);
  }

  return {
    filtrados,
    loading: loading || loadingEquipamentos,
    error,
    busca,
    formOpen,
    editando,
    mudandoStatus,
    submitting,
    equipamentoOpcoes,
    setBusca,
    setMudandoStatus,
    handleNovo,
    handleEditar,
    handleSubmit,
    handleMudarStatus,
    handleFecharForm,
    handleFecharMudarStatus,
    getEquipamentoNome,
    formatFaixaEtaria,
  };
}
