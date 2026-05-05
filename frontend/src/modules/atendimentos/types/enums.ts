// src/modules/atendimentos/types/enums.ts

export type TipoAtendimento =
  | 'VISITA_DOMICILIAR'
  | 'ATENDIMENTO_PRESENCIAL'
  | 'CONTATO_TELEFONICO'
  | 'OUTRO'

export type ModalidadeAtendimento =
  | 'INDIVIDUAL'
  | 'GRUPO'

export const TIPO_ATENDIMENTO_LABELS: Record<TipoAtendimento, string> = {
  VISITA_DOMICILIAR:      'Visita Domiciliar',
  ATENDIMENTO_PRESENCIAL: 'Atendimento Presencial',
  CONTATO_TELEFONICO:     'Contato Telefônico',
  OUTRO:                  'Outro',
}

export const MODALIDADE_ATENDIMENTO_LABELS: Record<ModalidadeAtendimento, string> = {
  INDIVIDUAL: 'Individual',
  GRUPO:      'Grupo',
}
