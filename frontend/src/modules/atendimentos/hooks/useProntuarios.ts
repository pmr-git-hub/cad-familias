// src/hooks/useProntuarios.ts

import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { prontuarioService } from '../services/prontuarioService'
import type { ProntuarioCadastroDTO, ProntuarioEncerramentoDTO } from '../types/prontuario'

export function useProntuariosDaFamilia(familiaId: number) {
  return useQuery({
    queryKey: ['prontuarios', familiaId],
    queryFn: () => prontuarioService.listarPorFamilia(familiaId),
    enabled: !!familiaId,
  })
}

export function useCadastrarProntuario() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (dto: ProntuarioCadastroDTO) =>
      prontuarioService.cadastrar(dto),
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: ['prontuarios', data.familiaId] })
    },
  })
}

export function useEncerrarProntuario(familiaId: number) {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: ({ id, dto }: { id: number; dto: ProntuarioEncerramentoDTO }) =>
      prontuarioService.encerrar(id, dto),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['prontuarios', familiaId] })
    },
  })
}
