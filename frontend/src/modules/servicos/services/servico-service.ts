// modules/servicos/services/servico-service.ts

import { api } from "@/lib/api";
import type {
  Servico,
  ServicoCadastroDTO,
  ServicoAtualizacaoDTO,
} from "../types/servico";

const BASE = "/api/servicos";

export const servicoService = {
  listar(): Promise<Servico[]> {
    return api<Servico[]>(BASE);
  },

  listarAtivos(): Promise<Servico[]> {
    return api<Servico[]>(`${BASE}/ativos`);
  },

  listarPorEquipamento(equipamentoId: number): Promise<Servico[]> {
    return api<Servico[]>(`${BASE}/equipamento/${equipamentoId}`);
  },

  listarAtivosPorEquipamento(equipamentoId: number): Promise<Servico[]> {
    return api<Servico[]>(`${BASE}/equipamento/${equipamentoId}/ativos`);
  },

  buscarPorId(id: number): Promise<Servico> {
    return api<Servico>(`${BASE}/${id}`);
  },

  buscarPorNome(nome: string): Promise<Servico[]> {
    return api<Servico[]>(`${BASE}/busca?nome=${encodeURIComponent(nome)}`);
  },

  criar(data: ServicoCadastroDTO): Promise<Servico> {
    return api<Servico>(BASE, { method: "POST", body: data });
  },

  atualizar(id: number, data: ServicoAtualizacaoDTO): Promise<Servico> {
    return api<Servico>(`${BASE}/${id}`, { method: "PUT", body: data });
  },

  desativar(id: number): Promise<Servico> {
    return api<Servico>(`${BASE}/${id}/desativar`, { method: "PATCH" });
  },
};
