import { api } from "@/lib/api";
import type {
  Tecnico,
  TecnicoCadastroDTO,
  TecnicoAtualizacaoDTO,
  VincularEquipamentoDTO,
} from "./../types/tecnicos";

const BASE = "/api/tecnico";

export const tecnicoService = {
  listar(): Promise<Tecnico[]> {
    return api<Tecnico[]>(BASE);
  },

  buscarPorId(id: number): Promise<Tecnico> {
    return api<Tecnico>(`${BASE}/${id}`);
  },

  criar(data: TecnicoCadastroDTO): Promise<Tecnico> {
    return api<Tecnico>(BASE, { method: "POST", body: data });
  },

  atualizar(id: number, data: TecnicoAtualizacaoDTO): Promise<Tecnico> {
    return api<Tecnico>(`${BASE}/${id}`, { method: "PUT", body: data });
  },

  vincularEquipamento(tecnicoId: number, data: VincularEquipamentoDTO): Promise<Tecnico> {
    return api<Tecnico>(`${BASE}/${tecnicoId}/equipamento`, {
      method: "POST",
      body: data,
    });
  },

  desvincularEquipamento(tecnicoId: number, equipamentoId: number): Promise<Tecnico> {
    return api<Tecnico>(`${BASE}/${tecnicoId}/equipamento/${equipamentoId}`, {
      method: "DELETE",
    });
  },
};
