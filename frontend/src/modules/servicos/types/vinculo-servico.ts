// modules/servicos/types/vinculo-servico.ts

export type StatusVinculo = "ATIVO" | "SUSPENSO" | "CANCELADO";

export interface VinculoPessoaServico {
  id: number;
  pessoaId: number;
  pessoaNome: string;
  servicoId: number;
  servicoNome: string;
  dataEntrada: string;
  dataSaida: string | null;
  status: StatusVinculo;
  motivoSaida: string | null;
}

export interface VinculoPessoaServicoLoteDTO {
  servicoId: number;
  pessoaIds: number[];
  dataEntrada?: string;
}

export interface VinculoLoteFalha {
  pessoaId: number;
  pessoaNome: string;
  motivo: string;
}

export interface VinculoComGestacaoResposta {
  vinculo: VinculoPessoaServico;
  gestacao: unknown | null;
  alertas: string[];
}

export interface VinculoLoteResposta {
  sucesso: VinculoComGestacaoResposta[];
  falhas: VinculoLoteFalha[];
}

export interface VinculoDesligamentoRequest {
  motivoSaida: string;
  dataSaida: string; 
}