"use client";

import { Button } from "@/components/ui/button";
import type { Usuario } from "./../types/usuario";

interface Props {
  usuario: Usuario | null;
  open: boolean;
  onClose: () => void;
  onConfirm: () => Promise<void>;
  loading?: boolean;
}

export function UsuarioModalMudarStatus({
  usuario,
  open,
  onClose,
  onConfirm,
  loading,
}: Props) {
  if (!open || !usuario) return null;

  const acao = usuario.ativo ? "desativar" : "reativar";
  const acaoCapitalizada = usuario.ativo ? "Desativar" : "Reativar";

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center">
      <div className="absolute inset-0 bg-black/50" onClick={onClose} />

      <div className="relative z-10 w-full max-w-md rounded-xl bg-white p-6 shadow-xl">
        <h2 className="text-lg font-semibold text-gray-900">
          {acaoCapitalizada} Usuário
        </h2>

        <p className="mt-2 text-sm text-gray-600">
          Tem certeza que deseja <strong>{acao}</strong> o usuário{" "}
          <strong>{usuario.username}</strong>?
        </p>

        {usuario.ativo && (
          <p className="mt-2 text-sm text-amber-600">
            O usuário não conseguirá mais acessar o sistema enquanto estiver
            inativo.
          </p>
        )}

        <div className="mt-6 flex justify-end gap-3">
          <Button
            type="button"
            variant="outline"
            onClick={onClose}
            disabled={loading}
          >
            Cancelar
          </Button>
          <Button
            onClick={onConfirm}
            disabled={loading}
            variant={usuario.ativo ? "destructive" : "default"}
          >
            {loading ? "Processando..." : acaoCapitalizada}
          </Button>
        </div>
      </div>
    </div>
  );
}
