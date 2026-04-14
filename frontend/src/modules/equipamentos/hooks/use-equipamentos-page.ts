"use client";

import { useState } from "react";
import { useEquipamentos } from "@/modules/equipamentos/hooks/use-equipamentos";
import type {
  Equipamento,
  EquipamentoCadastroDTO,
  EquipamentoAtualizacaoDTO,
} from "@/modules/equipamentos/types/equipamento";

export function useEquipamentosPage() {
  const { equipamentos, loading, error, criar, atualizar, mudarStatus } =
    useEquipamentos();

  // UI State
  const [busca, setBusca] = useState("");
  const [formOpen, setFormOpen] = useState(false);
  const [editando, setEditando] = useState<Equipamento | null>(null);
  const [mudandoStatus, setMudandoStatus] = useState<Equipamento | null>(null);
  const [submitting, setSubmitting] = useState(false);

  // Filtro local
  const filtrados: Equipamento[] = equipamentos.filter((e: Equipamento) => {
    const termo = busca.toLowerCase();
    return (
      e.nome.toLowerCase().includes(termo) ||
      e.tipo.toLowerCase().includes(termo) ||
      (e.bairro ?? "").toLowerCase().includes(termo) ||
      (e.cidade ?? "").toLowerCase().includes(termo)
    );
  });

  // Formata endereço legível
  function enderecoFormatado(e: Equipamento): string {
    const partes = [
      e.logradouro,
      e.numero ? `nº ${e.numero}` : null,
      e.complemento,
      e.bairro,
      e.cidade && e.estado ? `${e.cidade}/${e.estado}` : e.cidade || e.estado,
    ].filter(Boolean);
    return partes.length > 0 ? partes.join(", ") : "—";
  }

  // Handlers
  async function handleSubmit(
    data: EquipamentoCadastroDTO | EquipamentoAtualizacaoDTO
  ): Promise<void> {
    try {
      setSubmitting(true);
      if (editando) {
        await atualizar(editando.id, data as EquipamentoAtualizacaoDTO);
      } else {
        await criar(data as EquipamentoCadastroDTO);
      }
      setFormOpen(false);
      setEditando(null);
    } catch (err) {
      console.error("Erro ao salvar equipamento:", err);
    } finally {
      setSubmitting(false);
    }
  }

  async function handleMudarStatus(): Promise<void> {
    if (!mudandoStatus) return;
    try {
      setSubmitting(true);
      await mudarStatus(mudandoStatus.id, { ativo: mudandoStatus.ativo });
      setMudandoStatus(null);
    } catch (err) {
      console.error("Erro ao mudar status do equipamento:", err);
    } finally {
      setSubmitting(false);
    }
  }

  function handleEditar(equipamento: Equipamento): void {
    setEditando(equipamento);
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
    loading,
    error,
    busca,
    formOpen,
    editando,
    mudandoStatus,
    submitting,
    setBusca,
    setMudandoStatus,
    handleNovo,
    handleEditar,
    handleSubmit,
    handleMudarStatus,
    handleFecharForm,
    handleFecharMudarStatus,
    enderecoFormatado,
  };
}
