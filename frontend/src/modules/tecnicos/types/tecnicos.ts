export type Especialidade =
  | "ASSISTENTE_SOCIAL"
  | "PSICOLOGO"
  | "PEDAGOGO"
  | "ADVOGADO"
  | "OUTROS";

export interface TecnicoEquipamento {
  id: number;
  equipamentoId: number;
  nomeEquipamento: string;
  dataInicio: string;
  dataFim: string | null;
  ativo: boolean;
}

export interface Tecnico {
  id: number;
  nome: string;
  cpf: string;
  registroProfissional: string;
  especialidade: Especialidade;
  equipamentos: TecnicoEquipamento[];
  ativo: boolean;
}

export interface TecnicoCadastroDTO {
  nome: string;
  cpf: string;
  registroProfissional: string;
  especialidade: Especialidade;
  ativo?: boolean;
}

export interface TecnicoAtualizacaoDTO {
  nome?: string;
  cpf?: string;
  registroProfissional?: string;
  especialidade?: Especialidade;
  ativo?: boolean;
}

export interface VincularEquipamentoDTO {
  equipamentoId: number;
  dataInicio: string;
}

// Tipo intermediário usado no form (criação e edição)
export interface TecnicoFormData {
  nome: string;
  cpf: string;
  registroProfissional: string;
  especialidade: Especialidade;
  equipamentoIds: number[];
}

export interface TecnicoResumo {
  id: number;
  nome: string;
  especialidade: Especialidade;
}
