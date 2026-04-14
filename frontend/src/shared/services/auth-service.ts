import { api, ApiError } from "@/lib/api";
import type { LoginRequest, LoginResponse, UsuarioLogado } from "./../types/auth";

const TOKEN_KEY = "cad_token";
const REFRESH_KEY = "cad_refresh_token";
const USER_KEY = "cad_user";
const REMEMBER_KEY = "cad_remember";

function getStorage(): Storage | null {
  if (typeof window === "undefined") return null;
  const remember = localStorage.getItem(REMEMBER_KEY) === "true";
  return remember ? localStorage : sessionStorage;
}

export const authService = {
  // ─── Login ───
  async login(
    nomeUsuario: string,
    senha: string,
    remember: boolean = false
  ): Promise<LoginResponse> {
    // Salva a preferência ANTES de salvar os tokens
    if (typeof window !== "undefined") {
      localStorage.setItem(REMEMBER_KEY, String(remember));
    }

    const data = await api<LoginResponse>("/api /auth/login", {
      method: "POST",
      body: { nomeUsuario, senha } satisfies LoginRequest,
    });

    this.saveTokens(data);
    return data;
  },

  // ─── Refresh Token ───
  async refreshToken(): Promise<LoginResponse | null> {
    const current = this.getTokens();
    if (!current.refreshToken) return null;

    try {
      const data = await api<LoginResponse>("/api/auth/atualizar-token", {
        method: "POST",
        body: {
          id: null,
          nomeUsuario: null,
          token: current.token,
          refreshToken: current.refreshToken,
        },
      });

      this.saveTokens(data);
      return data;
    } catch {
      this.logout();
      return null;
    }
  },

  // ─── Verificar se está logado ───
  async verificarLogin(): Promise<UsuarioLogado | null> {
    const { token } = this.getTokens();
    if (!token) return null;

    try {
      return await api<UsuarioLogado>("/api/auth/isLogado", {
        headers: { authtoken: token },
      });
    } catch (error) {
      if (error instanceof ApiError && error.status === 401) {
        const refreshed = await this.refreshToken();
        if (refreshed) {
          return api<UsuarioLogado>("/api/auth/isLogado", {
            headers: { authtoken: refreshed.token },
          });
        }
      }
      this.logout();
      return null;
    }
  },

  // ─── Storage ───
  saveTokens(data: LoginResponse): void {
    const storage = getStorage();
    if (!storage) return;
    storage.setItem(TOKEN_KEY, data.token);
    storage.setItem(REFRESH_KEY, data.refreshToken);
    storage.setItem(
      USER_KEY,
      JSON.stringify({ id: data.id, nomeUsuario: data.nomeUsuario })
    );
  },

  getTokens(): { token: string | null; refreshToken: string | null } {
    const storage = getStorage();
    if (!storage) return { token: null, refreshToken: null };
    return {
      token: storage.getItem(TOKEN_KEY),
      refreshToken: storage.getItem(REFRESH_KEY),
    };
  },

  getUser(): UsuarioLogado | null {
    const storage = getStorage();
    if (!storage) return null;
    const raw = storage.getItem(USER_KEY);
    return raw ? JSON.parse(raw) : null;
  },

  getAccessToken(): string | null {
    return this.getTokens().token;
  },

  isAuthenticated(): boolean {
    return !!this.getTokens().token;
  },

  logout(): void {
    if (typeof window === "undefined") return;
    // Limpa dos dois storages pra garantir
    [localStorage, sessionStorage].forEach((s) => {
      s.removeItem(TOKEN_KEY);
      s.removeItem(REFRESH_KEY);
      s.removeItem(USER_KEY);
    });
    localStorage.removeItem(REMEMBER_KEY);
  },
};
