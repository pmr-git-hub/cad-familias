// modules/equipamentos/hooks/use-equipamentos-opcoes.ts
"use client";

import { useCallback, useEffect, useState } from "react";
import { equipamentoService } from "@/modules/equipamentos/services/equipamento-service";
import type { Equipamento } from "@/modules/equipamentos/types/equipamento"; 

export function useEquipamentosOpcoes() {
  const [equipamentos, setEquipamentos] = useState<Equipamento[]>([]);
  const [loading, setLoading] = useState(true);

  const carregar = useCallback(async () => {
    try {
      setLoading(true);
      const data = await equipamentoService.listar();
      setEquipamentos(data.filter((e) => e.ativo));
    } catch (err) {
      console.error("Erro ao carregar equipamentos:", err);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    carregar();
  }, [carregar]);

  return { equipamentos, loading };
}
