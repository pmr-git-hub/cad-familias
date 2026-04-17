// src/features/familias/components/familia-form.tsx

"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { toast } from "sonner";
import { Trash2, Pencil, Plus, UserPlus } from "lucide-react";
import { familiaService } from "../services/familia-service";
import { MembroModal } from "./membro-modal";
import {
  FamiliaDTO,
  PessoaDTO,
  EnderecoDTO,
  SituacaoFamilia,
  SEXO_LABELS,
  SITUACAO_LABELS,
  PARENTESCO_LABELS,
  LOCALIZACAO_LABELS,
  UF_OPTIONS,
} from "../types/familia";

interface Props {
  familiaInicial?: FamiliaDTO;
}

/* ── helpers para nunca passar null a inputs ── */
const s = (valor: string | null | undefined): string => valor ?? "";
const n = (valor: number | null | undefined): number | "" =>
  valor != null ? valor : "";

const enderecoVazio: EnderecoDTO = {
  logradouro: "",
  numero: "",
  bairro: "",
  cidade: "",
  uf: "PE",
  cep: "",
  pontoReferencia: "",
  localizacaoDomicilio: "URBANA",
};

const referenciaVazia: PessoaDTO = {
  nome: "",
  cpf: "",
  telefone: "",
  sexo: "FEMININO",
  parentesco: "RESPONSAVEL",
  rendaMensal: undefined,
  dataNascimento: "",
  numeroRg: "",
  orgaoExpeditorRg: "",
  dataExpedicaoRg: "",
  referencia: true,
  endereco: { ...enderecoVazio },
};

/** Sanitiza nulls vindos do backend para strings vazias */
function sanitizePessoa(p: PessoaDTO): PessoaDTO {
  return {
    ...p,
    nome: s(p.nome),
    cpf: s(p.cpf),
    telefone: s(p.telefone),
    dataNascimento: s(p.dataNascimento),
    numeroRg: s(p.numeroRg),
    orgaoExpeditorRg: s(p.orgaoExpeditorRg),
    dataExpedicaoRg: s(p.dataExpedicaoRg),
    rendaMensal: p.rendaMensal ?? undefined,
  };
}

function sanitizeEndereco(e: EnderecoDTO): EnderecoDTO {
  return {
    ...e,
    logradouro: s(e.logradouro),
    numero: s(e.numero),
    bairro: s(e.bairro),
    cidade: s(e.cidade),
    uf: s(e.uf) || "PE",
    cep: s(e.cep),
    pontoReferencia: s(e.pontoReferencia),
    localizacaoDomicilio: e.localizacaoDomicilio ?? "URBANA",
  };
}

export function FamiliaForm({ familiaInicial }: Props) {
  const router = useRouter();
  const editando = !!familiaInicial?.id;

  const [referencia, setReferencia] = useState<PessoaDTO>(
    familiaInicial?.pessoaReferencia
      ? sanitizePessoa(familiaInicial.pessoaReferencia)
      : { ...referenciaVazia }
  );
  const [endereco, setEndereco] = useState<EnderecoDTO>(
    familiaInicial?.pessoaReferencia?.endereco
      ? sanitizeEndereco(familiaInicial.pessoaReferencia.endereco)
      : { ...enderecoVazio }
  );
  const [membros, setMembros] = useState<PessoaDTO[]>(
    (familiaInicial?.membrosDaFamilia ?? []).map(sanitizePessoa)
  );
  const [codigoCadunico, setCodigoCadunico] = useState(
    s(familiaInicial?.codigoCadunico)
  );
  const [situacao, setSituacao] = useState<SituacaoFamilia>(
    familiaInicial?.situacao ?? "ATIVA"
  );
  const [salvando, setSalvando] = useState(false);

  // Modal de membro
  const [modalAberto, setModalAberto] = useState(false);
  const [membroEditando, setMembroEditando] = useState<PessoaDTO | null>(null);
  const [membroIndex, setMembroIndex] = useState<number | null>(null);

  // --- Handlers referência ---
  const handleRef = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setReferencia((prev) => ({ ...prev, [name]: value }));
  };

  const handleEndereco = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setEndereco((prev) => ({ ...prev, [name]: value }));
  };

  // --- Handlers membros ---
  const abrirModalNovo = () => {
    setMembroEditando(null);
    setMembroIndex(null);
    setModalAberto(true);
  };

  const abrirModalEditar = (index: number) => {
    setMembroEditando(membros[index]);
    setMembroIndex(index);
    setModalAberto(true);
  };

  const salvarMembro = (membro: PessoaDTO) => {
    if (membroIndex !== null) {
      setMembros((prev) =>
        prev.map((m, i) => (i === membroIndex ? membro : m))
      );
    } else {
      setMembros((prev) => [...prev, membro]);
    }
    setModalAberto(false);
  };

  const removerMembro = (index: number) => {
    setMembros((prev) => prev.filter((_, i) => i !== index));
  };

  // --- Cálculo renda ---
  const rendaTotal =
    (referencia.rendaMensal ?? 0) +
    membros.reduce((acc, m) => acc + (m.rendaMensal ?? 0), 0);

  // --- Submit ---
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSalvando(true);

    const payload: FamiliaDTO = {
      ...(familiaInicial?.id ? { id: familiaInicial.id } : {}),
      pessoaReferencia: {
        ...referencia,
        referencia: true,
        endereco,
      },
      membrosDaFamilia: membros.map((m) => ({ ...m, referencia: false })),
      rendaFamiliar: rendaTotal,
      codigoCadunico: codigoCadunico || undefined,
      situacao,
    };

    try {
      if (editando) {
        await familiaService.editar(familiaInicial!.id!, payload);
        toast.success("Família atualizada com sucesso!");
      } else {
        await familiaService.criar(payload);
        toast.success("Família cadastrada com sucesso!");
      }
      router.push("/familias");
    } catch {
      toast.error("Erro ao salvar família.");
    } finally {
      setSalvando(false);
    }
  };

  // --- Classes reutilizáveis ---
  const inputClass =
    "w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-blue-500 focus:ring-1 focus:ring-blue-500 outline-none";
  const labelClass = "block text-sm font-medium text-gray-700 mb-1";

  return (
    <>
      <form onSubmit={handleSubmit} className="space-y-8">
        {/* ====== SEÇÃO 1: PESSOA REFERÊNCIA ====== */}
        <section className="rounded-xl border border-gray-200 bg-white p-6 shadow-sm">
          <h2 className="text-lg font-bold text-gray-900 mb-5 flex items-center gap-2">
            <UserPlus className="h-5 w-5 text-blue-600" />
            Pessoa de Referência
          </h2>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div className="md:col-span-2">
              <label className={labelClass}>Nome Completo *</label>
              <input
                name="nome"
                value={s(referencia.nome)}
                onChange={handleRef}
                required
                className={inputClass}
              />
            </div>
            <div>
              <label className={labelClass}>CPF *</label>
              <input
                name="cpf"
                value={s(referencia.cpf)}
                onChange={handleRef}
                required
                maxLength={11}
                placeholder="00000000000"
                className={`${inputClass} font-mono`}
              />
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mt-4">
            <div>
              <label className={labelClass}>Sexo *</label>
              <select
                name="sexo"
                value={s(referencia.sexo)}
                onChange={handleRef}
                required
                className={inputClass}
              >
                {Object.entries(SEXO_LABELS).map(([v, l]) => (
                  <option key={v} value={v}>
                    {l}
                  </option>
                ))}
              </select>
            </div>
            <div>
              <label className={labelClass}>Data de Nascimento *</label>
              <input
                name="dataNascimento"
                type="date"
                value={s(referencia.dataNascimento)}
                onChange={handleRef}
                required
                className={inputClass}
              />
            </div>
            <div>
              <label className={labelClass}>Telefone</label>
              <input
                name="telefone"
                value={s(referencia.telefone)}
                onChange={handleRef}
                placeholder="(00) 00000-0000"
                className={inputClass}
              />
            </div>
            <div>
              <label className={labelClass}>Renda Mensal</label>
              <input
                name="rendaMensal"
                type="number"
                min={0}
                value={n(referencia.rendaMensal)}
                onChange={(e) =>
                  setReferencia((prev) => ({
                    ...prev,
                    rendaMensal: e.target.value
                      ? Number(e.target.value)
                      : undefined,
                  }))
                }
                className={inputClass}
              />
            </div>
          </div>

          {/* RG */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mt-4">
            <div>
              <label className={labelClass}>Nº RG</label>
              <input
                name="numeroRg"
                value={s(referencia.numeroRg)}
                onChange={handleRef}
                className={inputClass}
              />
            </div>
            <div>
              <label className={labelClass}>Órgão Expedidor</label>
              <input
                name="orgaoExpeditorRg"
                value={s(referencia.orgaoExpeditorRg)}
                onChange={handleRef}
                placeholder="SSP/PE"
                className={inputClass}
              />
            </div>
            <div>
              <label className={labelClass}>Data Expedição</label>
              <input
                name="dataExpedicaoRg"
                type="date"
                value={s(referencia.dataExpedicaoRg)}
                onChange={handleRef}
                className={inputClass}
              />
            </div>
          </div>
        </section>

        {/* ====== SEÇÃO 2: ENDEREÇO ====== */}
        <section className="rounded-xl border border-gray-200 bg-white p-6 shadow-sm">
          <h2 className="text-lg font-bold text-gray-900 mb-5">📍 Endereço</h2>

          <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
            <div className="md:col-span-2">
              <label className={labelClass}>Logradouro *</label>
              <input
                name="logradouro"
                value={s(endereco.logradouro)}
                onChange={handleEndereco}
                required
                className={inputClass}
              />
            </div>
            <div>
              <label className={labelClass}>Número *</label>
              <input
                name="numero"
                value={s(endereco.numero)}
                onChange={handleEndereco}
                required
                className={inputClass}
              />
            </div>
            <div>
              <label className={labelClass}>CEP</label>
              <input
                name="cep"
                value={s(endereco.cep)}
                onChange={handleEndereco}
                maxLength={8}
                placeholder="00000000"
                className={`${inputClass} font-mono`}
              />
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mt-4">
            <div>
              <label className={labelClass}>Bairro *</label>
              <input
                name="bairro"
                value={s(endereco.bairro)}
                onChange={handleEndereco}
                required
                className={inputClass}
              />
            </div>
            <div>
              <label className={labelClass}>Cidade *</label>
              <input
                name="cidade"
                value={s(endereco.cidade)}
                onChange={handleEndereco}
                required
                className={inputClass}
              />
            </div>
            <div>
              <label className={labelClass}>UF *</label>
              <select
                name="uf"
                value={s(endereco.uf)}
                onChange={handleEndereco}
                required
                className={inputClass}
              >
                {UF_OPTIONS.map((uf) => (
                  <option key={uf} value={uf}>
                    {uf}
                  </option>
                ))}
              </select>
            </div>
            <div>
              <label className={labelClass}>Localização</label>
              <select
                name="localizacaoDomicilio"
                value={s(endereco.localizacaoDomicilio)}
                onChange={handleEndereco}
                className={inputClass}
              >
                {Object.entries(LOCALIZACAO_LABELS).map(([v, l]) => (
                  <option key={v} value={v}>
                    {l}
                  </option>
                ))}
              </select>
            </div>
          </div>

          <div className="mt-4">
            <label className={labelClass}>Ponto de Referência</label>
            <input
              name="pontoReferencia"
              value={s(endereco.pontoReferencia)}
              onChange={handleEndereco}
              placeholder="Próximo a..."
              className={inputClass}
            />
          </div>
        </section>

        {/* ====== SEÇÃO 3: MEMBROS DA FAMÍLIA ====== */}
        <section className="rounded-xl border border-gray-200 bg-white p-6 shadow-sm">
          <div className="flex items-center justify-between mb-5">
            <h2 className="text-lg font-bold text-gray-900">
              👨‍👩‍👧‍👦 Membros da Família
            </h2>
            <button
              type="button"
              onClick={abrirModalNovo}
              className="inline-flex items-center gap-2 rounded-lg bg-blue-600 px-4 py-2 text-sm font-medium text-white hover:bg-blue-700 transition"
            >
              <Plus className="h-4 w-4" />
              Adicionar Membro
            </button>
          </div>

          {membros.length === 0 ? (
            <div className="text-center py-8 text-gray-400 text-sm">
              Nenhum membro adicionado além da pessoa de referência.
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="min-w-full text-sm">
                <thead className="bg-gray-50 text-left text-xs font-semibold uppercase text-gray-500">
                  <tr>
                    <th className="px-4 py-3">Nome</th>
                    <th className="px-4 py-3">CPF</th>
                    <th className="px-4 py-3">Parentesco</th>
                    <th className="px-4 py-3">Idade</th>
                    <th className="px-4 py-3">Renda</th>
                    <th className="px-4 py-3 text-right">Ações</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-100">
                  {membros.map((m, i) => {
                    const idade = m.dataNascimento
                      ? Math.floor(
                          (Date.now() -
                            new Date(m.dataNascimento).getTime()) /
                            31557600000
                        )
                      : "—";
                    return (
                      <tr key={m.id ?? i} className="hover:bg-gray-50">
                        <td className="px-4 py-3 font-medium text-gray-900">
                          {m.nome}
                        </td>
                        <td className="px-4 py-3 text-gray-600 font-mono">
                          {m.cpf}
                        </td>
                        <td className="px-4 py-3 text-gray-600">
                          {PARENTESCO_LABELS[m.parentesco] ?? m.parentesco}
                        </td>
                        <td className="px-4 py-3 text-gray-600">
                          {idade} anos
                        </td>
                        <td className="px-4 py-3 text-gray-600">
                          {m.rendaMensal != null
                            ? m.rendaMensal.toLocaleString("pt-BR", {
                                style: "currency",
                                currency: "BRL",
                              })
                            : "—"}
                        </td>
                        <td className="px-4 py-3 text-right space-x-1">
                          <button
                            type="button"
                            onClick={() => abrirModalEditar(i)}
                            className="rounded-lg p-1.5 text-gray-400 hover:bg-gray-100 hover:text-blue-600 transition"
                            title="Editar"
                          >
                            <Pencil className="h-4 w-4" />
                          </button>
                          <button
                            type="button"
                            onClick={() => removerMembro(i)}
                            className="rounded-lg p-1.5 text-gray-400 hover:bg-red-50 hover:text-red-600 transition"
                            title="Remover"
                          >
                            <Trash2 className="h-4 w-4" />
                          </button>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          )}
        </section>

        {/* ====== SEÇÃO 4: DADOS DA FAMÍLIA ====== */}
        <section className="rounded-xl border border-gray-200 bg-white p-6 shadow-sm">
          <h2 className="text-lg font-bold text-gray-900 mb-5">
            📋 Dados da Família
          </h2>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label className={labelClass}>Código CadÚnico</label>
              <input
                value={s(codigoCadunico)}
                onChange={(e) => setCodigoCadunico(e.target.value)}
                placeholder="Código do CadÚnico"
                className={`${inputClass} font-mono`}
              />
            </div>
            <div>
              <label className={labelClass}>Situação *</label>
              <select
                value={situacao}
                onChange={(e) =>
                  setSituacao(e.target.value as SituacaoFamilia)
                }
                required
                className={inputClass}
              >
                {Object.entries(SITUACAO_LABELS).map(([v, l]) => (
                  <option key={v} value={v}>
                    {l}
                  </option>
                ))}
              </select>
            </div>
            <div>
              <label className={labelClass}>Renda Familiar (calculada)</label>
              <div className="rounded-lg border border-gray-200 bg-gray-50 px-3 py-2 text-sm font-semibold text-green-700">
                {rendaTotal.toLocaleString("pt-BR", {
                  style: "currency",
                  currency: "BRL",
                })}
              </div>
            </div>
          </div>
        </section>

        {/* ====== BOTÕES ====== */}
        <div className="flex justify-end gap-3">
          <button
            type="button"
            onClick={() => router.push("/familias")}
            className="rounded-lg border border-gray-300 px-6 py-2.5 text-sm font-medium text-gray-700 hover:bg-gray-50 transition"
          >
            Cancelar
          </button>
          <button
            type="submit"
            disabled={salvando}
            className="rounded-lg bg-blue-600 px-6 py-2.5 text-sm font-medium text-white hover:bg-blue-700 disabled:opacity-50 transition"
          >
            {salvando
              ? "Salvando..."
              : editando
              ? "Salvar Alterações"
              : "Cadastrar Família"}
          </button>
        </div>
      </form>

      <MembroModal
        key={modalAberto ? (membroIndex ?? "novo") : "fechado"}
        aberto={modalAberto}
        membroInicial={membroEditando}
        onSalvar={salvarMembro}
        onFechar={() => setModalAberto(false)}
      />
    </>
  );
}
