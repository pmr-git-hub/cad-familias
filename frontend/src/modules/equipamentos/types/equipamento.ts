export enum TipoEquipamento {
  CRAS = "CRAS",
  CREAS = "CREAS",
  SCFV = "SCFV",
  ACOLHIMENTO = "ACOLHIMENTO",
  OUTRO = "OUTRO",
}

export interface Equipamento {
  id: number;
  nome: string;
  tipo: TipoEquipamento;
  cep: string | null;
  logradouro: string | null;
  numero: string | null;
  complemento: string | null;
  bairro: string | null;
  cidade: string | null;
  estado: string | null;
  telefone: string | null;
  email: string | null;
  ativo: boolean;
  criadoEm: string;
  criadoPor: number;
  atualizadoEm: string | null;
  atualizadoPor: number | null;
}

export interface EquipamentoCadastroDTO {
  nome: string;
  tipo: TipoEquipamento;
  cep?: string;
  logradouro?: string;
  numero?: string;
  complemento?: string;
  bairro?: string;
  cidade?: string;
  estado?: string;
  telefone?: string;
  email?: string;
}

export interface EquipamentoAtualizacaoDTO {
  nome?: string;
  tipo?: TipoEquipamento;
  cep?: string;
  logradouro?: string;
  numero?: string;
  complemento?: string;
  bairro?: string;
  cidade?: string;
  estado?: string;
  telefone?: string;
  email?: string;
  ativo?: boolean;
}
