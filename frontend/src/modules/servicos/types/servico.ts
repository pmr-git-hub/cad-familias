// modules/servicos/types/servico.ts

export type TipoAcompanhamento = "GERAL" | "GESTACAO" | "MSE" | "TRABALHO_INFANTIL";

export const TIPO_ACOMPANHAMENTO_LABELS: Record<TipoAcompanhamento, string> = {
  GERAL: "Geral",
  GESTACAO: "Gestação",
  MSE: "Medida Socioeducativa",
  TRABALHO_INFANTIL: "Trabalho Infantil",
};

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
  temListaPresenca: boolean;
  tipoAcompanhamento: TipoAcompanhamento;
  ativo: boolean;
  criadoEm: string;
  criadoPor: number;
  atualizadoEm: string | null;
  atualizadoPor: number | null;
  totalPessoasVinculadas: number; // NOVO
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
  temListaPresenca?: boolean;
  tipoAcompanhamento?: TipoAcompanhamento;
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
  temListaPresenca?: boolean;
  tipoAcompanhamento?: TipoAcompanhamento;
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
  temListaPresenca: boolean;
  tipoAcompanhamento: TipoAcompanhamento;
}
