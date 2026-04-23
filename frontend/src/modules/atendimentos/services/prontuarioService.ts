// src/services/prontuarioService.ts

import { api } from '@/lib/api';
import type {
  ProntuarioCadastroDTO,
  ProntuarioEncerramentoDTO,
  ProntuarioRespostaDTO,
} from '../types/prontuario'

const BASE = "/api/prontuarios";


export const prontuarioService = {
  listarPorFamilia: (familiaId: number) => {
    return api<ProntuarioRespostaDTO[]>(`${BASE}/familia/${familiaId}`);
  },

  cadastrar: (dto: ProntuarioCadastroDTO) => {
    return api<ProntuarioRespostaDTO>(`${BASE}`, {
      method: "POST",
      body: dto,
    });
  },

  encerrar: (id: number, dto: ProntuarioEncerramentoDTO) => {
    return api<ProntuarioRespostaDTO>(`${BASE}/${id}/encerrar`, {
      method: "POST",
      body: dto,
    });
  },
}
