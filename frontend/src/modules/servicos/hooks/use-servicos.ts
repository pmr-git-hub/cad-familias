// modules/servicos/hooks/use-servicos.ts

"use client";

import { useCallback, useEffect, useState } from "react";
import { servicoService } from "../services/servico-service";
import type {
  Servico,
  ServicoCadastroDTO,
  ServicoAtualizacaoDTO,
} from "../types/servico";

export function useServicos() {
  const [servicos, setServicos] = useState<Servico[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const carregar = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await servicoService.listar();
      setServicos(data);
    } catch (err: unknown) {
      setError(
        err instanceof Error ? err.message : "Erro ao carregar serviços"
      );
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    carregar();
  }, [carregar]);

  async function criar(data: ServicoCadastroDTO) {
    const novo = await servicoService.criar(data);
    setServicos((prev) => [...prev, novo]);
    return novo;
  }

  async function atualizar(id: number, data: ServicoAtualizacaoDTO) {
    const atualizado = await servicoService.atualizar(id, data);
    setServicos((prev) => prev.map((s) => (s.id === id ? atualizado : s)));
    return atualizado;
  }

  async function mudarStatus(id: number, ativoAtual: boolean) {
    if (ativoAtual) {
      // Desativar
      const atualizado = await servicoService.desativar(id);
      setServicos((prev) => prev.map((s) => (s.id === id ? atualizado : s)));
    } else {
      // Reativar
      const atualizado = await servicoService.atualizar(id, { ativo: true });
      setServicos((prev) => prev.map((s) => (s.id === id ? atualizado : s)));
    }
  }

  return {
    servicos,
    loading,
    error,
    carregar,
    criar,
    atualizar,
    mudarStatus,
  };
}
