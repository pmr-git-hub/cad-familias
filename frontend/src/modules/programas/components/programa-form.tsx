// modules/programas/components/programa-form.tsx

"use client";

import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import type { ProgramaSocial, ProgramaFormData } from "../types/programas";

interface Props {
  programa: ProgramaSocial | null;
  open: boolean;
  onClose: () => void;
  onSubmit: (data: ProgramaFormData) => Promise<void>;
  loading?: boolean;
}

function ProgramaFormContent({
  programa,
  onClose,
  onSubmit,
  loading,
}: Omit<Props, "open">) {
  const isEditing = !!programa;

  const [nome, setNome] = useState(programa?.nome ?? "");
  const [criterios, setCriterios] = useState(programa?.criterios ?? "");
  const [orgaoGestor, setOrgaoGestor] = useState(programa?.orgaoGestor ?? "");

  function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    onSubmit({ nome, criterios, orgaoGestor });
  }

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center">
      <div className="absolute inset-0 bg-black/50" onClick={onClose} />

      <div className="relative z-10 w-full max-w-lg rounded-xl bg-white p-6 shadow-xl max-h-[90vh] overflow-y-auto">
        <h2 className="text-lg font-semibold text-gray-900">
          {isEditing ? "Editar Programa Social" : "Novo Programa Social"}
        </h2>

        <form onSubmit={handleSubmit} className="mt-4 space-y-4">
          {/* Nome */}
          <div className="space-y-1.5">
            <Label htmlFor="nome">Nome *</Label>
            <Input
              id="nome"
              value={nome}
              onChange={(e) => setNome(e.target.value)}
              placeholder="Ex: Bolsa Família, BPC..."
              required
              maxLength={300}
            />
          </div>

          {/* Órgão Gestor */}
          <div className="space-y-1.5">
            <Label htmlFor="orgaoGestor">Órgão Gestor</Label>
            <Input
              id="orgaoGestor"
              value={orgaoGestor}
              onChange={(e) => setOrgaoGestor(e.target.value)}
              placeholder="Ex: Ministério do Desenvolvimento Social"
            />
          </div>

          {/* Critérios */}
          <div className="space-y-1.5">
            <Label htmlFor="criterios">Critérios de Entrada</Label>
            <textarea
              id="criterios"
              value={criterios}
              onChange={(e) => setCriterios(e.target.value)}
              placeholder="Descreva os critérios para entrada no programa..."
              rows={4}
              className="flex w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 resize-none"
            />
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

export function ProgramaForm({ open, programa, ...rest }: Props) {
  if (!open) return null;

  return (
    <ProgramaFormContent
      key={programa?.id ?? "novo"}
      programa={programa}
      {...rest}
    />
  );
}
