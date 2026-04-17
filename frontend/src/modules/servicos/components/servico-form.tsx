// modules/servicos/components/servico-form.tsx

"use client";

import { useState, useRef, useEffect, useCallback } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { ChevronDown, Check } from "lucide-react";
import type { Servico, ServicoFormData } from "../types/servico";

interface EquipamentoOption {
  id: number;
  nome: string;
}

interface Props {
  servico: Servico | null;
  open: boolean;
  onClose: () => void;
  onSubmit: (data: ServicoFormData) => Promise<void>;
  equipamentosDisponiveis: EquipamentoOption[];
  loading?: boolean;
}

function ServicoFormContent({
  servico,
  onClose,
  onSubmit,
  equipamentosDisponiveis,
  loading,
}: Omit<Props, "open">) {
  const isEditing = !!servico;

  const [equipamentoId, setEquipamentoId] = useState<number | null>(
    servico?.equipamentoId ?? null
  );
  const [nome, setNome] = useState(servico?.nome ?? "");
  const [descricao, setDescricao] = useState(servico?.descricao ?? "");
  const [publicoAlvo, setPublicoAlvo] = useState(servico?.publicoAlvo ?? "");
  const [faixaEtariaMin, setFaixaEtariaMin] = useState(
    servico?.faixaEtariaMin?.toString() ?? ""
  );
  const [faixaEtariaMax, setFaixaEtariaMax] = useState(
    servico?.faixaEtariaMax?.toString() ?? ""
  );
  const [diaSemana, setDiaSemana] = useState(servico?.diaSemana ?? "");
  const [horario, setHorario] = useState(servico?.horario ?? "");

  // Dropdown equipamento (single select)
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

  function getEquipamentoNome(): string {
    if (!equipamentoId) return "";
    return (
      equipamentosDisponiveis.find((e) => e.id === equipamentoId)?.nome ?? ""
    );
  }

  function handleSubmit(e: React.FormEvent) {
    e.preventDefault();

    if (!equipamentoId) return;

    onSubmit({
      equipamentoId,
      nome,
      descricao,
      publicoAlvo,
      faixaEtariaMin,
      faixaEtariaMax,
      diaSemana,
      horario,
    });
  }

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center">
      <div className="absolute inset-0 bg-black/50" onClick={onClose} />

      <div className="relative z-10 w-full max-w-lg rounded-xl bg-white p-6 shadow-xl max-h-[90vh] overflow-y-auto">
        <h2 className="text-lg font-semibold text-gray-900">
          {isEditing ? "Editar Serviço" : "Novo Serviço"}
        </h2>

        <form onSubmit={handleSubmit} className="mt-4 space-y-4">
          {/* Equipamento (single select com busca) */}
          <div className="space-y-1.5">
            <Label>Equipamento *</Label>
            <div ref={dropdownRef} className="relative">
              <div
                className="flex h-10 w-full cursor-pointer items-center justify-between rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-within:ring-2 focus-within:ring-ring focus-within:ring-offset-2"
                onClick={() => setDropdownOpen(!dropdownOpen)}
              >
                <span
                  className={
                    equipamentoId
                      ? "text-gray-900"
                      : "text-muted-foreground"
                  }
                >
                  {equipamentoId
                    ? getEquipamentoNome()
                    : "Selecione o equipamento..."}
                </span>
                <ChevronDown
                  className={`h-4 w-4 shrink-0 text-muted-foreground transition-transform ${
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
                        const isSelected = equipamentoId === eq.id;
                        return (
                          <li
                            key={eq.id}
                            onClick={(e) => {
                              e.stopPropagation();
                              setEquipamentoId(eq.id);
                              setDropdownOpen(false);
                              setSearchTerm("");
                            }}
                            className={`flex cursor-pointer items-center gap-2 px-3 py-2 text-sm transition-colors hover:bg-gray-100 ${
                              isSelected ? "bg-blue-50" : ""
                            }`}
                          >
                            <div
                              className={`flex h-4 w-4 shrink-0 items-center justify-center rounded-full border ${
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
                </div>
              )}
            </div>
          </div>

          {/* Nome */}
          <div className="space-y-1.5">
            <Label htmlFor="nome">Nome do Serviço *</Label>
            <Input
              id="nome"
              value={nome}
              onChange={(e) => setNome(e.target.value)}
              placeholder="Ex: SCFV Manhã, Gerando Vidas"
              required
            />
          </div>

          {/* Descrição */}
          <div className="space-y-1.5">
            <Label htmlFor="descricao">Descrição</Label>
            <textarea
              id="descricao"
              value={descricao}
              onChange={(e) => setDescricao(e.target.value)}
              placeholder="Descreva o objetivo do serviço..."
              rows={3}
              className="flex w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 resize-none"
            />
          </div>

          {/* Público-alvo */}
          <div className="space-y-1.5">
            <Label htmlFor="publicoAlvo">Público-alvo</Label>
            <Input
              id="publicoAlvo"
              value={publicoAlvo}
              onChange={(e) => setPublicoAlvo(e.target.value)}
              placeholder="Ex: Crianças de 6 a 15 anos"
            />
          </div>

          {/* Faixa etária */}
          <div className="grid grid-cols-2 gap-4">
            <div className="space-y-1.5">
              <Label htmlFor="faixaMin">Idade Mínima</Label>
              <Input
                id="faixaMin"
                type="number"
                min={0}
                value={faixaEtariaMin}
                onChange={(e) => setFaixaEtariaMin(e.target.value)}
                placeholder="Ex: 6"
              />
            </div>
            <div className="space-y-1.5">
              <Label htmlFor="faixaMax">Idade Máxima</Label>
              <Input
                id="faixaMax"
                type="number"
                min={0}
                value={faixaEtariaMax}
                onChange={(e) => setFaixaEtariaMax(e.target.value)}
                placeholder="Ex: 15"
              />
            </div>
          </div>

          {/* Dia e Horário */}
          <div className="grid grid-cols-2 gap-4">
            <div className="space-y-1.5">
              <Label htmlFor="diaSemana">Dia da Semana</Label>
              <Input
                id="diaSemana"
                value={diaSemana}
                onChange={(e) => setDiaSemana(e.target.value)}
                placeholder="Ex: Segunda, Quarta"
              />
            </div>
            <div className="space-y-1.5">
              <Label htmlFor="horario">Horário</Label>
              <Input
                id="horario"
                value={horario}
                onChange={(e) => setHorario(e.target.value)}
                placeholder="Ex: 14h às 16h"
              />
            </div>
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
            <Button type="submit" disabled={loading || !equipamentoId}>
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

export function ServicoForm({ open, servico, ...rest }: Props) {
  if (!open) return null;

  return (
    <ServicoFormContent
      key={servico?.id ?? "novo"}
      servico={servico}
      {...rest}
    />
  );
}
