"use client";

import { useCallback, useEffect, useState } from "react";
import { usuarioService } from "../services/usuario-service";
import type {
  Usuario,
  CriarUsuarioDTO,
  AtualizarUsuarioDTO,
} from "./../types/usuario";

export function useUsuarios() {
  const [usuarios, setUsuarios] = useState<Usuario[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const carregar = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await usuarioService.listar();
      setUsuarios(data);
    } catch (err: unknown) {
      setError(
        err instanceof Error ? err.message : "Erro ao carregar usuários"
      );
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    carregar();
  }, [carregar]);

  async function criar(data: CriarUsuarioDTO) {
    const novo = await usuarioService.criar(data);
    setUsuarios((prev) => [...prev, novo]);
    return novo;
  }

  async function atualizar(id: number, data: AtualizarUsuarioDTO) {
    const atualizado = await usuarioService.atualizar(id, data);
    setUsuarios((prev) => prev.map((u) => (u.id === id ? atualizado : u)));
    return atualizado;
  }

  async function mudarStatus(id: number, usuario: Usuario) {
    const data: AtualizarUsuarioDTO = {
      username: usuario.username,
      perfil: usuario.perfil,
      tecnicoId: usuario.tecnico.id,
      ativo: !usuario.ativo,
    };
    const atualizado = await usuarioService.atualizar(id, data);
    setUsuarios((prev) => prev.map((u) => (u.id === id ? atualizado : u)));
    return atualizado;
  }

  return {
    usuarios,
    loading,
    error,
    carregar,
    criar,
    atualizar,
    mudarStatus,
  };
}
