// src/types/prontuario.ts

export interface ProntuarioRespostaDTO {
  id: number
  familiaId: number
  equipamentoId: number
  equipamentoNome: string
  tecnicoNome: string
  dataAbertura: string
  dataFechamento: string | null
  motivoEncerramento: string | null
  ativo: boolean
}

export interface ProntuarioCadastroDTO {
  familiaId: number
  equipamentoId: number
}

export interface ProntuarioEncerramentoDTO {
  dataFechamento: string // "YYYY-MM-DD"
  motivoEncerramento?: string
}
