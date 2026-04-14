"use client";

import { useCallback, useEffect, useState } from "react";
import { tecnicoService } from "../services/tecnico-service";
import type {
  Tecnico,
  TecnicoCadastroDTO,
  TecnicoAtualizacaoDTO,
  VincularEquipamentoDTO,
} from "./../types/tecnicos";

export function useTecnicos() {
  const [tecnicos, setTecnicos] = useState<Tecnico[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const carregar = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await tecnicoService.listar();
      setTecnicos(data);
    } catch (err: unknown) {
      setError(
        err instanceof Error ? err.message : "Erro ao carregar técnicos"
      );
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    carregar();
  }, [carregar]);

  async function criar(data: TecnicoCadastroDTO) {
    const novo = await tecnicoService.criar(data);
    setTecnicos((prev) => [...prev, novo]);
    return novo;
  }

  async function atualizar(id: number, data: TecnicoAtualizacaoDTO) {
    const atualizado = await tecnicoService.atualizar(id, data);
    setTecnicos((prev) => prev.map((t) => (t.id === id ? atualizado : t)));
    return atualizado;
  }

  async function mudarStatus(id: number, data: TecnicoAtualizacaoDTO) {
    data.ativo = !data.ativo;
    const atualizado = await tecnicoService.atualizar(id, data);
    setTecnicos((prev) => prev.map((t) => (t.id === id ? atualizado : t)));
  }

  async function vincularEquipamento(tecnicoId: number, data: VincularEquipamentoDTO) {
    const atualizado = await tecnicoService.vincularEquipamento(tecnicoId, data);
    setTecnicos((prev) => prev.map((t) => (t.id === tecnicoId ? atualizado : t)));
    return atualizado;
  }

  async function desvincularEquipamento(tecnicoId: number, equipamentoId: number) {
    const atualizado = await tecnicoService.desvincularEquipamento(tecnicoId, equipamentoId);
    setTecnicos((prev) => prev.map((t) => (t.id === tecnicoId ? atualizado : t)));
    return atualizado;
  }

  return {
    tecnicos,
    loading,
    error,
    carregar,
    criar,
    atualizar,
    mudarStatus,
    vincularEquipamento,
    desvincularEquipamento,
    
  };
}
