// modules/programas/components/programa-modal-mudar-status.tsx

"use client";

import { Button } from "@/components/ui/button";
import type { ProgramaSocial } from "../types/programas";

interface Props {
  programa: ProgramaSocial | null;
  open: boolean;
  onClose: () => void;
  onConfirm: () => void;
  loading?: boolean;
}

export function ProgramaModalMudarStatus({
  programa,
  open,
  onClose,
  onConfirm,
  loading,
}: Props) {
  if (!open || !programa) return null;

  const isAtivo = programa.ativo;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center">
      <div className="absolute inset-0 bg-black/50" onClick={onClose} />

      <div className="relative z-10 w-full max-w-md rounded-xl bg-white p-6 shadow-xl">
        <h2 className="text-lg font-semibold text-gray-900">
          {isAtivo ? "Desativar" : "Reativar"} Programa
        </h2>
        <p className="mt-2 text-sm text-gray-600">
          {isAtivo ? (
            <>
              Tem certeza que deseja desativar{" "}
              <strong>{programa.nome}</strong>? O programa não será excluído,
              apenas ficará inativo. Famílias vinculadas não serão
              desvinculadas automaticamente.
            </>
          ) : (
            <>
              Deseja reativar o programa <strong>{programa.nome}</strong>? Ele
              voltará a ficar disponível para novos vínculos.
            </>
          )}
        </p>

        <div className="mt-6 flex justify-end gap-3">
          <Button variant="outline" onClick={onClose} disabled={loading}>
            Cancelar
          </Button>
          {isAtivo ? (
            <Button
              variant="destructive"
              onClick={onConfirm}
              disabled={loading}
            >
              {loading ? "Desativando..." : "Desativar"}
            </Button>
          ) : (
            <Button
              onClick={onConfirm}
              disabled={loading}
              className="bg-green-600 hover:bg-green-700"
            >
              {loading ? "Reativando..." : "Reativar"}
            </Button>
          )}
        </div>
      </div>
    </div>
  );
}
