// src/features/familias/api/familia-api.ts

import { api } from "@/lib/api";
import { FamiliaDTO } from "../types/familia";

const BASE = "/api/familia";

export const familiaService = {
  listar: async (): Promise<FamiliaDTO[]> => {
    return api<FamiliaDTO[]>(BASE);
  },

  buscarPorId: async (id: number): Promise<FamiliaDTO> => {
    return api<FamiliaDTO>(`${BASE}/${id}`);
  },

  criar: async (data: FamiliaDTO): Promise<FamiliaDTO> => {
    return api<FamiliaDTO>(BASE, {
      method: "POST",
      body: data,
    });
  },

  editar: async (id: number, data: FamiliaDTO): Promise<FamiliaDTO> => {
    return api<FamiliaDTO>(`${BASE}/${id}`, {
      method: "PUT",
      body: data,
    });
  },
};
