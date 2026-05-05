// src/modules/atendimentos/types/prontuario.ts

export type StatusProntuario = 'ABERTO' | 'ENCERRADO' | 'SUSPENSO'

export interface ProntuarioRespostaDTO {
  id: number
  familiaId: number
  equipamentoId: number
  equipamentoNome: string
  tecnicoNome: string
  dataAbertura: string
  dataFechamento: string | null
  motivoEncerramento: string | null
  status: StatusProntuario
}

export interface ProntuarioCadastroDTO {
  familiaId: number
  equipamentoId: number
}

export interface ProntuarioEncerramentoDTO {
  dataFechamento: string // "YYYY-MM-DD"
  motivoEncerramento?: string
}
