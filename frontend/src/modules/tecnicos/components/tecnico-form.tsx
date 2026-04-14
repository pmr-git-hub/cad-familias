"use client";

import { useState, useRef, useEffect, useCallback } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { X, ChevronDown, Check } from "lucide-react";
import type {
  Tecnico,
  TecnicoFormData,
  Especialidade,
} from "./../types/tecnicos";

interface EquipamentoOption {
  id: number;
  nome: string;
}

interface Props {
  tecnico: Tecnico | null;
  open: boolean;
  onClose: () => void;
  onSubmit: (data: TecnicoFormData) => Promise<void>;
  equipamentosDisponiveis: EquipamentoOption[];
  loading?: boolean;
}

const ESPECIALIDADES: { value: Especialidade; label: string }[] = [
  { value: "ASSISTENTE_SOCIAL", label: "Assistente Social" },
  { value: "PSICOLOGO", label: "Psicólogo(a)" },
  { value: "PEDAGOGO", label: "Pedagogo(a)" },
  { value: "ADVOGADO", label: "Advogado(a)" },
  { value: "OUTROS", label: "Outros" },
];

function TecnicoFormContent({
  tecnico,
  onClose,
  onSubmit,
  equipamentosDisponiveis,
  loading,
}: Omit<Props, "open">) {
  const isEditing = !!tecnico;

  const [nome, setNome] = useState(tecnico?.nome ?? "");
  const [cpf, setCpf] = useState(tecnico?.cpf ?? "");
  const [registroProfissional, setRegistroProfissional] = useState(
    tecnico?.registroProfissional ?? ""
  );
  const [especialidade, setEspecialidade] = useState<Especialidade>(
    tecnico?.especialidade ?? "ASSISTENTE_SOCIAL"
  );

  // Multi-select state
  const [selectedIds, setSelectedIds] = useState<number[]>(
    tecnico?.equipamentos
      ?.filter((e) => e.ativo)
      .map((e) => e.equipamentoId) ?? []
  );
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");
  const dropdownRef = useRef<HTMLDivElement>(null);
  const searchInputRef = useRef<HTMLInputElement>(null);

  const handleClickOutside = useCallback((event: MouseEvent) => {
    if (
      dropdownRef.current &&
      !dropdownRef.current.contains(event.target as Node)
    ) {
      setDropdownOpen(false);
      setSearchTerm("");
    }
  }, []);

  useEffect(() => {
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, [handleClickOutside]);

  useEffect(() => {
    if (dropdownOpen && searchInputRef.current) {
      searchInputRef.current.focus();
    }
  }, [dropdownOpen]);

  const filteredEquipamentos = equipamentosDisponiveis.filter((eq) =>
    eq.nome.toLowerCase().includes(searchTerm.toLowerCase())
  );

  function toggleEquipamento(id: number) {
    setSelectedIds((prev) =>
      prev.includes(id) ? prev.filter((i) => i !== id) : [...prev, id]
    );
  }

  function removeEquipamento(id: number) {
    setSelectedIds((prev) => prev.filter((i) => i !== id));
  }

  function getEquipamentoNome(id: number): string {
    return equipamentosDisponiveis.find((e) => e.id === id)?.nome ?? "";
  }

  function handleSubmit(e: React.FormEvent) {
    e.preventDefault();

    // Sempre manda os equipamentoIds selecionados
    onSubmit({
      nome,
      cpf,
      registroProfissional,
      especialidade,
      equipamentoIds: selectedIds,
    });
  }

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center">
      <div className="absolute inset-0 bg-black/50" onClick={onClose} />

      <div className="relative z-10 w-full max-w-lg rounded-xl bg-white p-6 shadow-xl max-h-[90vh] overflow-y-auto">
        <h2 className="text-lg font-semibold text-gray-900">
          {isEditing ? "Editar Técnico" : "Novo Técnico"}
        </h2>

        <form onSubmit={handleSubmit} className="mt-4 space-y-4">
          {/* Nome */}
          <div className="space-y-1.5">
            <Label htmlFor="nome">Nome *</Label>
            <Input
              id="nome"
              value={nome}
              onChange={(e) => setNome(e.target.value)}
              placeholder="Nome completo"
              required
            />
          </div>

          {/* CPF */}
          <div className="space-y-1.5">
            <Label htmlFor="cpf">CPF *</Label>
            <Input
              id="cpf"
              value={cpf}
              onChange={(e) => setCpf(e.target.value)}
              placeholder="000.000.000-00"
              required
            />
          </div>

          {/* Registro Profissional */}
          <div className="space-y-1.5">
            <Label htmlFor="registro">Registro Profissional</Label>
            <Input
              id="registro"
              value={registroProfissional}
              onChange={(e) => setRegistroProfissional(e.target.value)}
              placeholder="Ex: CRESS 12345"
            />
          </div>

          {/* Especialidade */}
          <div className="space-y-1.5">
            <Label htmlFor="especialidade">Especialidade *</Label>
            <select
              id="especialidade"
              value={especialidade}
              onChange={(e) =>
                setEspecialidade(e.target.value as Especialidade)
              }
              className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2"
              required
            >
              {ESPECIALIDADES.map((esp) => (
                <option key={esp.value} value={esp.value}>
                  {esp.label}
                </option>
              ))}
            </select>
          </div>

          {/* Multi-select Equipamentos */}
          <div className="space-y-1.5">
            <Label>Equipamentos</Label>
            <div ref={dropdownRef} className="relative">
              <div
                className="flex min-h-10 w-full cursor-pointer flex-wrap items-center gap-1.5 rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-within:ring-2 focus-within:ring-ring focus-within:ring-offset-2"
                onClick={() => setDropdownOpen(!dropdownOpen)}
              >
                {selectedIds.length === 0 && (
                  <span className="text-muted-foreground">
                    Selecione os equipamentos...
                  </span>
                )}

                {selectedIds.map((id) => (
                  <span
                    key={id}
                    className="inline-flex items-center gap-1 rounded-md bg-blue-100 px-2 py-0.5 text-xs font-medium text-blue-800"
                  >
                    {getEquipamentoNome(id)}
                    <button
                      type="button"
                      onClick={(e) => {
                        e.stopPropagation();
                        removeEquipamento(id);
                      }}
                      className="ml-0.5 rounded-full p-0.5 hover:bg-blue-200 transition-colors"
                    >
                      <X className="h-3 w-3" />
                    </button>
                  </span>
                ))}

                <ChevronDown
                  className={`ml-auto h-4 w-4 shrink-0 text-muted-foreground transition-transform ${
                    dropdownOpen ? "rotate-180" : ""
                  }`}
                />
              </div>

              {dropdownOpen && (
                <div className="absolute z-20 mt-1 w-full rounded-md border border-input bg-white shadow-lg">
                  <div className="border-b px-3 py-2">
                    <input
                      ref={searchInputRef}
                      type="text"
                      value={searchTerm}
                      onChange={(e) => setSearchTerm(e.target.value)}
                      placeholder="Buscar equipamento..."
                      className="w-full text-sm outline-none placeholder:text-muted-foreground"
                      onClick={(e) => e.stopPropagation()}
                    />
                  </div>

                  <ul className="max-h-48 overflow-y-auto py-1">
                    {filteredEquipamentos.length === 0 ? (
                      <li className="px-3 py-2 text-sm text-muted-foreground">
                        Nenhum equipamento encontrado
                      </li>
                    ) : (
                      filteredEquipamentos.map((eq) => {
                        const isSelected = selectedIds.includes(eq.id);
                        return (
                          <li
                            key={eq.id}
                            onClick={(e) => {
                              e.stopPropagation();
                              toggleEquipamento(eq.id);
                            }}
                            className={`flex cursor-pointer items-center gap-2 px-3 py-2 text-sm transition-colors hover:bg-gray-100 ${
                              isSelected ? "bg-blue-50" : ""
                            }`}
                          >
                            <div
                              className={`flex h-4 w-4 shrink-0 items-center justify-center rounded border ${
                                isSelected
                                  ? "border-blue-600 bg-blue-600"
                                  : "border-gray-300"
                              }`}
                            >
                              {isSelected && (
                                <Check className="h-3 w-3 text-white" />
                              )}
                            </div>
                            {eq.nome}
                          </li>
                        );
                      })
                    )}
                  </ul>

                  {equipamentosDisponiveis.length > 2 && (
                    <div className="flex gap-2 border-t px-3 py-2">
                      <button
                        type="button"
                        onClick={(e) => {
                          e.stopPropagation();
                          setSelectedIds(
                            equipamentosDisponiveis.map((eq) => eq.id)
                          );
                        }}
                        className="text-xs text-blue-600 hover:underline"
                      >
                        Selecionar todos
                      </button>
                      <button
                        type="button"
                        onClick={(e) => {
                          e.stopPropagation();
                          setSelectedIds([]);
                        }}
                        className="text-xs text-red-600 hover:underline"
                      >
                        Limpar
                      </button>
                    </div>
                  )}
                </div>
              )}
            </div>

            {selectedIds.length > 0 && (
              <p className="text-xs text-muted-foreground">
                {selectedIds.length} equipamento(s) selecionado(s)
              </p>
            )}
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

export function TecnicoForm({ open, tecnico, ...rest }: Props) {
  if (!open) return null;

  return (
    <TecnicoFormContent
      key={tecnico?.id ?? "novo"}
      tecnico={tecnico}
      {...rest}
    />
  );
}
