"use client";

import { useCallback, useEffect, useState } from "react";
import { equipamentoService } from "../services/equipamento-service";
import type {
  Equipamento,
  EquipamentoCadastroDTO,
  EquipamentoAtualizacaoDTO,
} from "../types/equipamento";

export function useEquipamentos() {
  const [equipamentos, setEquipamentos] = useState<Equipamento[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const carregar = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await equipamentoService.listar();
      setEquipamentos(data);
    } catch (err: unknown) {
      setError(
        err instanceof Error ? err.message : "Erro ao carregar equipamentos"
      );
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    carregar();
  }, [carregar]);

  async function criar(data: EquipamentoCadastroDTO) {
    const novo = await equipamentoService.criar(data);
    setEquipamentos((prev) => [...prev, novo]);
    return novo;
  }

  async function atualizar(id: number, data: EquipamentoAtualizacaoDTO) {
    const atualizado = await equipamentoService.atualizar(id, data);
    setEquipamentos((prev) =>
      prev.map((e) => (e.id === id ? atualizado : e))
    );
    return atualizado;
  }

  async function mudarStatus(id: number, data: EquipamentoAtualizacaoDTO) {
    data.ativo = !data.ativo; // Alterna o status
    const atualizado = await equipamentoService.atualizar(id, data);
    setEquipamentos((prev) =>
      prev.map((e) => (e.id === id ? atualizado : e))
    );
  }

  
  return {
    equipamentos,
    loading,
    error,
    carregar,
    criar,
    atualizar,
    mudarStatus,
  };
}
