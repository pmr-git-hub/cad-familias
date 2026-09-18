// src/modules/atendimentos/hooks/useServicosDoEquipamento.ts

import { useQuery } from '@tanstack/react-query'
import { servicoService } from '@/modules/servicos/services/servico-service'

export function useServicosDoEquipamento(equipamentoId: number | null) {
  return useQuery({
    queryKey: ['servicos', 'equipamento', equipamentoId],
    queryFn: () => servicoService.listarAtivosPorEquipamento(equipamentoId!),
    enabled: !!equipamentoId,
    staleTime: 1000 * 60 * 10,
  })
}
