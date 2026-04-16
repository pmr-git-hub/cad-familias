// modules/programas/hooks/use-programas.ts

"use client";

import { useCallback, useEffect, useState } from "react";
import { programaService } from "../services/programa-service";
import type {
  ProgramaSocial,
  ProgramaCadastroDTO,
  ProgramaAtualizacaoDTO,
} from "../types/programas";

export function useProgramas() {
  const [programas, setProgramas] = useState<ProgramaSocial[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const carregar = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await programaService.listar();
      setProgramas(data);
    } catch (err: unknown) {
      setError(
        err instanceof Error ? err.message : "Erro ao carregar programas sociais"
      );
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    carregar();
  }, [carregar]);

  async function criar(data: ProgramaCadastroDTO) {
    const novo = await programaService.criar(data);
    setProgramas((prev) => [...prev, novo]);
    return novo;
  }

  async function atualizar(id: number, data: ProgramaAtualizacaoDTO) {
    const atualizado = await programaService.atualizar(id, data);
    setProgramas((prev) => prev.map((p) => (p.id === id ? atualizado : p)));
    return atualizado;
  }

  async function mudarStatus(id: number, ativoAtual: boolean) {
    let atualizado: ProgramaSocial;

    if (ativoAtual) {
      atualizado = await programaService.desativar(id);
    } else {
      atualizado = await programaService.atualizar(id, { ativo: true });
    }

    setProgramas((prev) => prev.map((p) => (p.id === id ? atualizado : p)));
    return atualizado;
  }

  return {
    programas,
    loading,
    error,
    carregar,
    criar,
    atualizar,
    mudarStatus,
  };
}
