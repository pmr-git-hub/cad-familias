import { api } from "@/lib/api";
import type {
  Usuario,
  CriarUsuarioDTO,
  AtualizarUsuarioDTO,
} from './../types/usuario';

const BASE = "/api/usuarios";

export const usuarioService = {
  listar(): Promise<Usuario[]> {
    return api<Usuario[]>(BASE);
  },

  criar(data: CriarUsuarioDTO): Promise<Usuario> {
    return api<Usuario>(BASE, { method: "POST", body: data });
  },

  atualizar(id: number, data: AtualizarUsuarioDTO): Promise<Usuario> {
    return api<Usuario>(`${BASE}/${id}`, { method: "PUT", body: data });
  },
};
