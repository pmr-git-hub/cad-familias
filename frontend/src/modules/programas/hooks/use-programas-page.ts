// modules/programas/hooks/use-programas-page.ts

"use client";

import { useState } from "react";
import { useProgramas } from "./use-programas";
import type { ProgramaSocial, ProgramaFormData } from "../types/programas";

export function useProgramasPage() {
  const { programas, loading, error, criar, atualizar, mudarStatus } =
    useProgramas();

  const [busca, setBusca] = useState("");
  const [formOpen, setFormOpen] = useState(false);
  const [editando, setEditando] = useState<ProgramaSocial | null>(null);
  const [mudandoStatus, setMudandoStatus] = useState<ProgramaSocial | null>(null);
  const [submitting, setSubmitting] = useState(false);

  const filtrados = programas.filter((p) => {
    const termo = busca.toLowerCase();
    return (
      p.nome.toLowerCase().includes(termo) ||
      (p.orgaoGestor?.toLowerCase().includes(termo) ?? false) ||
      (p.criterios?.toLowerCase().includes(termo) ?? false)
    );
  });

  async function handleSubmit(data: ProgramaFormData): Promise<void> {
    try {
      setSubmitting(true);

      if (editando) {
        await atualizar(editando.id, {
          nome: data.nome,
          criterios: data.criterios || undefined,
          orgaoGestor: data.orgaoGestor || undefined,
        });
      } else {
        await criar({
          nome: data.nome,
          criterios: data.criterios || undefined,
          orgaoGestor: data.orgaoGestor || undefined,
        });
      }

      setFormOpen(false);
      setEditando(null);
    } catch (err) {
      console.error("Erro ao salvar programa social:", err);
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
      console.error("Erro ao mudar status do programa:", err);
    } finally {
      setSubmitting(false);
    }
  }

  function handleEditar(programa: ProgramaSocial): void {
    setEditando(programa);
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
  };
}
