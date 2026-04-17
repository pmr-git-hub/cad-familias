// src/features/familias/hooks/use-familias.ts

"use client";

import { useEffect, useState, useCallback } from "react";
import { FamiliaDTO } from "../types/familia";
import { familiaService } from "../services/familia-service";
import { toast } from "sonner";

export function useFamilias() {
  const [familias, setFamilias] = useState<FamiliaDTO[]>([]);
  const [loading, setLoading] = useState(true);

  const carregar = useCallback(async () => {
    try {
      setLoading(true);
      const data = await familiaService.listar();
      setFamilias(data);
    } catch {
      toast.error("Erro ao carregar famílias.");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    carregar();
  }, [carregar]);

  return { familias, loading, recarregar: carregar };
}

export function useFamilia(id?: number) {
  const [familia, setFamilia] = useState<FamiliaDTO | null>(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (!id) return;

    let cancelado = false;

    const buscar = async () => {
      setLoading(true);
      try {
        const data = await familiaService.buscarPorId(id);
        if (!cancelado) setFamilia(data);
      } catch {
        if (!cancelado) toast.error("Erro ao carregar família.");
      } finally {
        if (!cancelado) setLoading(false);
      }
    };

    buscar();

    return () => {
      cancelado = true;
    };
  }, [id]);

  return { familia, loading };
}
