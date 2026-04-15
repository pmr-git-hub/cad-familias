"use client";

import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Eye, EyeOff, Sparkles } from "lucide-react";
import type { Usuario, UsuarioFormData, Perfil } from "./../types/usuario";
import type { TecnicoResumo } from "../../tecnicos/types/tecnicos";

interface Props {
  usuario: Usuario | null;
  open: boolean;
  onClose: () => void;
  onSubmit: (data: UsuarioFormData) => Promise<void>;
  tecnicosDisponiveis: TecnicoResumo[];
  loading?: boolean;
}

const PERFIS: { value: Perfil; label: string }[] = [
  { value: "ADMIN", label: "Administrador" },
  { value: "USUARIO", label: "Usuário" },
];

/**
 * Remove acentos e caracteres especiais de uma string.
 * Ex: "José Antônio" → "jose.antonio"
 */
function gerarSugestaoLogin(nomeCompleto: string): string {
  const normalizado = nomeCompleto
    .normalize("NFD")
    .replace(/[\u0300-\u036f]/g, "") // remove acentos
    .toLowerCase()
    .trim();

  const partes = normalizado.split(/\s+/).filter(Boolean);

  if (partes.length === 0) return "";
  if (partes.length === 1) return partes[0];

  // primeiro nome + último sobrenome
  return `${partes[0]}.${partes[partes.length - 1]}`;
}

function UsuarioFormContent({
  usuario,
  onClose,
  onSubmit,
  tecnicosDisponiveis,
  loading,
}: Omit<Props, "open">) {
  const isEditing = !!usuario;

  const [username, setUsername] = useState(usuario?.username ?? "");
  const [password, setPassword] = useState("");
  const [perfil, setPerfil] = useState<Perfil>(usuario?.perfil ?? "USUARIO");
  const [tecnicoId, setTecnicoId] = useState<number | "">(
    usuario?.tecnico?.id ?? ""
  );
  const [showPassword, setShowPassword] = useState(false);
  const [sugestaoLogin, setSugestaoLogin] = useState<string | null>(null);

  function handleTecnicoChange(value: string) {
    const id = value ? Number(value) : "";
    setTecnicoId(id);

    // Gera sugestão apenas na criação
    if (!isEditing && id) {
      const tecnico = tecnicosDisponiveis.find((t) => t.id === id);
      if (tecnico) {
        const sugestao = gerarSugestaoLogin(tecnico.nome);
        setSugestaoLogin(sugestao);
      }
    } else {
      setSugestaoLogin(null);
    }
  }

  function aplicarSugestao() {
    if (sugestaoLogin) {
      setUsername(sugestaoLogin);
      setSugestaoLogin(null);
    }
  }

  function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    if (!tecnicoId) return;

    onSubmit({
      username,
      password,
      perfil,
      tecnicoId: Number(tecnicoId),
    });
  }

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center">
      <div className="absolute inset-0 bg-black/50" onClick={onClose} />

      <div className="relative z-10 w-full max-w-lg rounded-xl bg-white p-6 shadow-xl max-h-[90vh] overflow-y-auto">
        <h2 className="text-lg font-semibold text-gray-900">
          {isEditing ? "Editar Usuário" : "Novo Usuário"}
        </h2>

        <form onSubmit={handleSubmit} className="mt-4 space-y-4">
          {/* Técnico */}
          <div className="space-y-1.5">
            <Label htmlFor="tecnico">Técnico Vinculado *</Label>
            <select
              id="tecnico"
              value={tecnicoId}
              onChange={(e) => handleTecnicoChange(e.target.value)}
              className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2"
              required
            >
              <option value="">Selecione um técnico...</option>
              {tecnicosDisponiveis.map((t) => (
                <option key={t.id} value={t.id}>
                  {t.nome} — {t.especialidade}
                </option>
              ))}
            </select>
          </div>

          {/* Username */}
          <div className="space-y-1.5">
            <Label htmlFor="username">Nome de Usuário *</Label>
            <Input
              id="username"
              value={username}
              onChange={(e) => {
                setUsername(e.target.value);
                // Se o usuário digitar manualmente, esconde a sugestão
                if (sugestaoLogin && e.target.value !== sugestaoLogin) {
                  setSugestaoLogin(null);
                }
              }}
              placeholder="Ex: maria.silva"
              required
            />

            {/* Sugestão de login */}
            {sugestaoLogin && sugestaoLogin !== username && (
              <button
                type="button"
                onClick={aplicarSugestao}
                className="flex items-center gap-1.5 text-xs text-blue-600 hover:text-blue-800 transition-colors mt-1 cursor-pointer"
              >
                <Sparkles className="h-3 w-3" />
                Usar sugestão: <strong>{sugestaoLogin}</strong>
              </button>
            )}
          </div>

          {/* Senha */}
          <div className="space-y-1.5">
            <Label htmlFor="password">
              Senha {isEditing ? "(deixe vazio para manter)" : "*"}
            </Label>
            <div className="relative">
              <Input
                id="password"
                type={showPassword ? "text" : "password"}
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder={isEditing ? "••••••••" : "Mínimo 6 caracteres"}
                required={!isEditing}
                minLength={!isEditing ? 6 : undefined}
                className="pr-10"
              />
              <button
                type="button"
                onClick={() => setShowPassword(!showPassword)}
                className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600 transition-colors"
              >
                {showPassword ? (
                  <EyeOff className="h-4 w-4" />
                ) : (
                  <Eye className="h-4 w-4" />
                )}
              </button>
            </div>
          </div>

          {/* Perfil */}
          <div className="space-y-1.5">
            <Label htmlFor="perfil">Perfil *</Label>
            <select
              id="perfil"
              value={perfil}
              onChange={(e) => setPerfil(e.target.value as Perfil)}
              className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2"
              required
            >
              {PERFIS.map((p) => (
                <option key={p.value} value={p.value}>
                  {p.label}
                </option>
              ))}
            </select>
          </div>

          {/* Actions */}
          <div className="flex justify-end gap-3 pt-4">
            <Button
              type="button"
              variant="outline"
              onClick={onClose}
              disabled={loading}
            >
              Cancelar
            </Button>
            <Button type="submit" disabled={loading}>
              {loading
                ? "Salvando..."
                : isEditing
                  ? "Salvar Alterações"
                  : "Cadastrar"}
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
}

export function UsuarioForm({ open, usuario, ...rest }: Props) {
  if (!open) return null;

  return (
    <UsuarioFormContent
      key={usuario?.id ?? "novo"}
      usuario={usuario}
      {...rest}
    />
  );
}
