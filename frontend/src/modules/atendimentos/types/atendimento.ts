// src/types/atendimento.ts

import type { TipoAtendimento, ModalidadeAtendimento } from './enums'

export interface AtendimentoRespostaDTO {
  id: number
  prontuarioId: number
  pessoaId: number | null
  data: string
  tipo: TipoAtendimento
  modalidade: ModalidadeAtendimento
  descricao: string
  tecnicoNome: string
}

export interface AtendimentoCadastroDTO {
  prontuarioId: number
  pessoaId: number | null
  data: string // LocalDateTime → mandamos "YYYY-MM-DDT00:00:00"
  tipo: TipoAtendimento
  modalidade: ModalidadeAtendimento
  descricao: string
}
