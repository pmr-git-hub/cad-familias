// src/lib/types/auth.ts

export interface LoginRequest {
  nomeUsuario: string;
  senha: string;
}

export interface LoginResponse {
  id: number;
  nomeUsuario : string;
  token: string;
  refreshToken: string;
}

export interface UsuarioLogado {
  id: number;
  nomeUsuario: string;
  token: string;
  refreshToken: string;
}
