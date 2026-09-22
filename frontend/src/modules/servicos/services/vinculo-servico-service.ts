// modules/servicos/services/vinculo-servico-service.ts

import { api } from "@/lib/api";
import type {
  VinculoPessoaServicoLoteDTO,
  VinculoLoteResposta,
  VinculoPessoaServico,
  VinculoDesligamentoRequest,
} from "../types/vinculo-servico";

const BASE = "/api/vinculos-servico";

export const vinculoServicoService = {
  vincularEmLote(data: VinculoPessoaServicoLoteDTO): Promise<VinculoLoteResposta> {
    return api<VinculoLoteResposta>(`${BASE}/lote`, { method: "POST", body: data });
  },

  listarAtivosPorServico(servicoId: number): Promise<VinculoPessoaServico[]> {
    return api<VinculoPessoaServico[]>(`${BASE}/servico/${servicoId}/ativos`);
  },

  desligar(
    vinculoId: number,
    data: VinculoDesligamentoRequest
  ): Promise<VinculoPessoaServico> {
    return api<VinculoPessoaServico>(`${BASE}/${vinculoId}/desligar`, {
      method: "PATCH",
      body: data,
    });
  },

  async desligarEmLote(
    vinculoIds: number[],
    data: VinculoDesligamentoRequest
  ): Promise<{
    sucesso: { vinculoId: number }[];
    falhas: { vinculoId: number; motivo: string }[];
  }> {
    const resultados = await Promise.allSettled(
      vinculoIds.map((id) => vinculoServicoService.desligar(id, data))
    );

    const sucesso: { vinculoId: number }[] = [];
    const falhas: { vinculoId: number; motivo: string }[] = [];

    resultados.forEach((r, idx) => {
      const vinculoId = vinculoIds[idx];
      if (r.status === "fulfilled") {
        sucesso.push({ vinculoId });
      } else {
        falhas.push({
          vinculoId,
          motivo: r.reason instanceof Error ? r.reason.message : "Erro ao desligar",
        });
      }
    });

    return { sucesso, falhas };
  },
};
