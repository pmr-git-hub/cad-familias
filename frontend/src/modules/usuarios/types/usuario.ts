import type { TecnicoResumo } from "@/modules/tecnicos/types/tecnicos"; 

export type Perfil = "ADMIN" | "USUARIO";

export interface Usuario {
  id: number;
  username: string;
  perfil: Perfil;
  ativo: boolean;
  ultimoAcesso: string | null;
  tecnico: TecnicoResumo;
}

export interface CriarUsuarioDTO {
  username: string;
  password: string;
  perfil: Perfil;
  tecnicoId: number;
  ativo: boolean;
}

export interface AtualizarUsuarioDTO {
  username: string;
  password?: string;
  perfil: Perfil;
  tecnicoId: number;
  ativo: boolean;
}

// Tipo intermediário usado no form (criação e edição)
export interface UsuarioFormData {
  username: string;
  password: string;
  perfil: Perfil;
  tecnicoId: number;
}
