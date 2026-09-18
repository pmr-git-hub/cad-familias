// src/types/atendimento.ts

import type { TipoAtendimento, ModalidadeAtendimento } from './enums'

export interface AtendimentoRespostaDTO {
  id: number
  prontuarioId: number
  tecnicoId: number
  tecnicoNome: string
  pessoaId: number | null
  pessoaNome: string | null
  servicoId: number | null
  servicoNome: string | null
  programaId: number | null
  programaNome: string | null
  data: string
  tipo: TipoAtendimento
  modalidade: ModalidadeAtendimento
  descricao: string
  criadoEm: string
}

export interface AtendimentoCadastroDTO {
  prontuarioId: number
  pessoaId: number | null
  servicoId: number | null
  programaId: number | null
  data: string
  tipo: TipoAtendimento
  modalidade: ModalidadeAtendimento
  descricao: string
}
