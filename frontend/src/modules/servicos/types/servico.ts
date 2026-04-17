// modules/servicos/types/servico.ts

export interface Servico {
  id: number;
  equipamentoId: number;
  nome: string;
  descricao: string | null;
  publicoAlvo: string | null;
  faixaEtariaMin: number | null;
  faixaEtariaMax: number | null;
  diaSemana: string | null;
  horario: string | null;
  ativo: boolean;
  criadoEm: string;
  criadoPor: number;
  atualizadoEm: string | null;
  atualizadoPor: number | null;
}

export interface ServicoCadastroDTO {
  equipamentoId: number;
  nome: string;
  descricao?: string;
  publicoAlvo?: string;
  faixaEtariaMin?: number;
  faixaEtariaMax?: number;
  diaSemana?: string;
  horario?: string;
  ativo?: boolean;
}

export interface ServicoAtualizacaoDTO {
  equipamentoId?: number;
  nome?: string;
  descricao?: string;
  publicoAlvo?: string;
  faixaEtariaMin?: number;
  faixaEtariaMax?: number;
  diaSemana?: string;
  horario?: string;
  ativo?: boolean;
}

export interface ServicoFormData {
  equipamentoId: number | null;
  nome: string;
  descricao: string;
  publicoAlvo: string;
  faixaEtariaMin: string;
  faixaEtariaMax: string;
  diaSemana: string;
  horario: string;
}
