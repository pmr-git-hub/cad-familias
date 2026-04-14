"use client";

import { useState } from "react";
import { useTecnicos } from "./use-tecnicos";
import type { Tecnico, TecnicoFormData } from "./../types/tecnicos";

export function useTecnicosPage() {
  const {
    tecnicos,
    loading,
    error,
    criar,
    atualizar,
    mudarStatus,
    vincularEquipamento,
    desvincularEquipamento,
  } = useTecnicos();

  const [busca, setBusca] = useState("");
  const [formOpen, setFormOpen] = useState(false);
  const [editando, setEditando] = useState<Tecnico | null>(null);
  const [mudandoStatus, setMudandoStatus] = useState<Tecnico | null>(null);
  const [submitting, setSubmitting] = useState(false);

  const filtrados = tecnicos.filter((t) => {
    const termo = busca.toLowerCase();
    const equipamentosStr = t.equipamentos
      .map((e) => e.nomeEquipamento)
      .join(" ")
      .toLowerCase();

    return (
      t.nome.toLowerCase().includes(termo) ||
      t.cpf.includes(termo) ||
      t.especialidade.toLowerCase().includes(termo) ||
      equipamentosStr.includes(termo)
    );
  });

  function especialidadeFormatada(valor: string): string {
    const mapa: Record<string, string> = {
      ASSISTENTE_SOCIAL: "Assistente Social",
      PSICOLOGO: "Psicólogo(a)",
      PEDAGOGO: "Pedagogo(a)",
      ADVOGADO: "Advogado(a)",
      OUTROS: "Outros",
    };
    return mapa[valor] ?? valor;
  }

  async function handleSubmit(data: TecnicoFormData): Promise<void> {
    try {
      setSubmitting(true);
      const hoje = new Date().toISOString().split("T")[0];
      const { equipamentoIds, ...dadosTecnico } = data;

      if (editando) {
        // 1. Atualiza os dados do técnico
        await atualizar(editando.id, { ...dadosTecnico, ativo: editando.ativo });

        // 2. Calcula diff dos equipamentos
        const idsAtuais = editando.equipamentos
          .filter((e) => e.ativo)
          .map((e) => e.equipamentoId);

        const paraVincular = equipamentoIds.filter(
          (id) => !idsAtuais.includes(id)
        );
        const paraDesvincular = editando.equipamentos.filter(
          (e) => e.ativo && !equipamentoIds.includes(e.equipamentoId)
        );

        // 3. Vincula novos
        for (const eqId of paraVincular) {
          await vincularEquipamento(editando.id, {
            equipamentoId: eqId,
            dataInicio: hoje,
          });
        }

        // 4. Desvincula removidos
        for (const eq of paraDesvincular) {
          await desvincularEquipamento(editando.id, eq.equipamentoId);
        }
      } else {
        // Criação: manda ativo: true explicitamente
        const novo = await criar({ ...dadosTecnico, ativo: true });

        for (const eqId of equipamentoIds) {
          await vincularEquipamento(novo.id, {
            equipamentoId: eqId,
            dataInicio: hoje,
          });
        }
      }

      setFormOpen(false);
      setEditando(null);
    } catch (err) {
      console.error("Erro ao salvar técnico:", err);
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
      console.error("Erro ao mudar status do técnico:", err);
    } finally {
      setSubmitting(false);
    }
  }

  function handleEditar(tecnico: Tecnico): void {
    setEditando(tecnico);
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
    especialidadeFormatada,
  };
}
