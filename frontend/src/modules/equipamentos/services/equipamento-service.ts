import { api } from "@/lib/api";
import type {
  Equipamento,
  EquipamentoCadastroDTO,
  EquipamentoAtualizacaoDTO,
} from "../types/equipamento";

const BASE = "/api/equipamentos";

export const equipamentoService = {
  listar(): Promise<Equipamento[]> {
    return api<Equipamento[]>(BASE);
  },

  buscarPorId(id: number): Promise<Equipamento> {
    return api<Equipamento>(`${BASE}/${id}`);
  },

  criar(data: EquipamentoCadastroDTO): Promise<Equipamento> {
    return api<Equipamento>(BASE, { method: "POST", body: data });
  },

  atualizar(id: number, data: EquipamentoAtualizacaoDTO): Promise<Equipamento> {
    return api<Equipamento>(`${BASE}/${id}`, { method: "PUT", body: data });
  },

  listarMeus: () => {
    return api<Equipamento[]>(`${BASE}/meusEquipamentos`);
  }
};
