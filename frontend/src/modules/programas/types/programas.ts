// modules/programas/types/programas.ts

export interface ProgramaSocial {
  id: number;
  nome: string;
  criterios: string | null;
  orgaoGestor: string | null;
  ativo: boolean;
  criadoEm: string;
  criadoPor: number;
  atualizadoEm: string | null;
  atualizadoPor: number | null;
}

export interface ProgramaCadastroDTO {
  nome: string;
  criterios?: string;
  orgaoGestor?: string;
  ativo?: boolean;
}

export interface ProgramaAtualizacaoDTO {
  nome?: string;
  criterios?: string;
  orgaoGestor?: string;
  ativo?: boolean;
}

export interface ProgramaFormData {
  nome: string;
  criterios: string;
  orgaoGestor: string;
}

export interface ProgramaResumo {
  id: number;
  nome: string;
}
