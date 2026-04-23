// src/services/atendimentoService.ts

import { api } from '@/lib/api' 
import type { AtendimentoCadastroDTO, AtendimentoRespostaDTO } from '../types/atendimento'

const BASE = "/api/atendimentos";


export const atendimentoService = {
  listarPorProntuario:  async (prontuarioId: number) => {
    return api<AtendimentoRespostaDTO[]>(`${BASE}/prontuario/${prontuarioId}`)
  },

  buscarPorId: async (id: number) => {
    return api<AtendimentoRespostaDTO>(`${BASE}/${id}`)
  },

  cadastrar: async (dto: AtendimentoCadastroDTO) => {
    return api<AtendimentoRespostaDTO>(`${BASE}`, {
      method: "POST",
      body: dto,
    });
  },

  atualizar: async (id: number, dto: Partial<AtendimentoCadastroDTO>) => {
    return api<AtendimentoRespostaDTO>(`${BASE}/${id}`, {
      method: "PUT",
      body: dto,
    })
  },
}
