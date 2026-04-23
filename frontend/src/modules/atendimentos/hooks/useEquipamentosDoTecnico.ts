// src/hooks/useEquipamentosDoTecnico.ts

import { useQuery } from '@tanstack/react-query'
import { equipamentoService } from '@/modules/equipamentos/services/equipamento-service' 

export function useEquipamentosDoTecnico() {
  return useQuery({
    queryKey: ['equipamentos', 'meus'],
    queryFn: () => equipamentoService.listarMeus(),
    staleTime: 1000 * 60 * 10, // 10 min — muda raramente
  })
}
