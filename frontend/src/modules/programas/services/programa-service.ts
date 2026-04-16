// modules/programas/services/programa-service.ts

import { api } from "@/lib/api";
import type {
  ProgramaSocial,
  ProgramaCadastroDTO,
  ProgramaAtualizacaoDTO,
} from "../types/programas";

const BASE = "/api/programas-sociais";

export const programaService = {
  listar(): Promise<ProgramaSocial[]> {
    return api<ProgramaSocial[]>(BASE);
  },

  listarAtivos(): Promise<ProgramaSocial[]> {
    return api<ProgramaSocial[]>(`${BASE}/ativos`);
  },

  buscarPorId(id: number): Promise<ProgramaSocial> {
    return api<ProgramaSocial>(`${BASE}/${id}`);
  },

  buscarPorNome(nome: string): Promise<ProgramaSocial[]> {
    return api<ProgramaSocial[]>(`${BASE}/busca?nome=${encodeURIComponent(nome)}`);
  },

  criar(data: ProgramaCadastroDTO): Promise<ProgramaSocial> {
    return api<ProgramaSocial>(BASE, { method: "POST", body: data });
  },

  atualizar(id: number, data: ProgramaAtualizacaoDTO): Promise<ProgramaSocial> {
    return api<ProgramaSocial>(`${BASE}/${id}`, { method: "PUT", body: data });
  },

  desativar(id: number): Promise<ProgramaSocial> {
    return api<ProgramaSocial>(`${BASE}/${id}/desativar`, { method: "PATCH" });
  },
};
