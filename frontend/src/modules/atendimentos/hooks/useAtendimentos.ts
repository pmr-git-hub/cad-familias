// src/hooks/useAtendimentos.ts

import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { atendimentoService } from '../services/atendimentoService'
import type { AtendimentoCadastroDTO } from '../types/atendimento'
import { format } from 'date-fns'

export function useAtendimentosDoProntuario(prontuarioId: number) {
  return useQuery({
    queryKey: ['atendimentos', prontuarioId],
    queryFn: () => atendimentoService.listarPorProntuario(prontuarioId),
    enabled: !!prontuarioId,
  })
}

export function useCadastrarAtendimento(prontuarioId: number) {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (dto: Omit<AtendimentoCadastroDTO, 'data'> & { data: Date }) => {
      const payload: AtendimentoCadastroDTO = {
        ...dto,
        data: `${format(dto.data, 'yyyy-MM-dd')}T00:00:00`,
      }
      return atendimentoService.cadastrar(payload)
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['atendimentos', prontuarioId] })
    },
  })
}
