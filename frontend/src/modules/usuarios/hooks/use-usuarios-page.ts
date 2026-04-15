"use client";

import { useState } from "react";
import { useUsuarios } from "./use-usuarios"; 
import type { Usuario, UsuarioFormData, Perfil } from './../types/usuario';

export function useUsuariosPage() {
  const {
    usuarios,
    loading,
    error,
    criar,
    atualizar,
    mudarStatus,
  } = useUsuarios();

  const [busca, setBusca] = useState("");
  const [formOpen, setFormOpen] = useState(false);
  const [editando, setEditando] = useState<Usuario | null>(null);
  const [mudandoStatus, setMudandoStatus] = useState<Usuario | null>(null);
  const [submitting, setSubmitting] = useState(false);

  const filtrados = usuarios.filter((u) => {
    const termo = busca.toLowerCase();
    return (
      u.username.toLowerCase().includes(termo) ||
      u.tecnico?.nome?.toLowerCase().includes(termo) ||
      u.perfil.toLowerCase().includes(termo)
    );
  });

  function perfilFormatado(valor: Perfil): string {
    const mapa: Record<Perfil, string> = {
      ADMIN: "Administrador",
      USUARIO: "Usuário",
    };
    return mapa[valor] ?? valor;
  }

  async function handleSubmit(data: UsuarioFormData): Promise<void> {
    try {
      setSubmitting(true);

      if (editando) {
        await atualizar(editando.id, {
          username: data.username,
          password: data.password || undefined,
          perfil: data.perfil,
          tecnicoId: data.tecnicoId,
          ativo: editando.ativo,
        });
      } else {
        await criar({
          username: data.username,
          password: data.password,
          perfil: data.perfil,
          tecnicoId: data.tecnicoId,
          ativo: true,
        });
      }

      setFormOpen(false);
      setEditando(null);
    } catch (err) {
      console.error("Erro ao salvar usuário:", err);
    } finally {
      setSubmitting(false);
    }
  }

  async function handleMudarStatus(): Promise<void> {
    if (!mudandoStatus) return;
    try {
      setSubmitting(true);
      await mudarStatus(mudandoStatus.id, mudandoStatus);
      setMudandoStatus(null);
    } catch (err) {
      console.error("Erro ao mudar status do usuário:", err);
    } finally {
      setSubmitting(false);
    }
  }

  function handleEditar(usuario: Usuario): void {
    setEditando(usuario);
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
    perfilFormatado,
  };
}
