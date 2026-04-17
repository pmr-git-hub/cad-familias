// src/features/familias/components/membro-modal.tsx

"use client";

import { useState } from "react";
import { X } from "lucide-react";
import {
  PessoaDTO,
  SEXO_LABELS,
  PARENTESCO_LABELS,
} from "../types/familia";

interface Props {
  aberto: boolean;
  membroInicial?: PessoaDTO | null;
  onSalvar: (membro: PessoaDTO) => void;
  onFechar: () => void;
}

const membroVazio: PessoaDTO = {
  nome: "",
  cpf: "",
  telefone: "",
  sexo: "MASCULINO",
  parentesco: "FILHO",
  rendaMensal: undefined,
  dataNascimento: "",
  numeroRg: "",
  orgaoExpeditorRg: "",
  dataExpedicaoRg: "",
  referencia: false,
};

export function MembroModal({ aberto, membroInicial, onSalvar, onFechar }: Props) {
  const [form, setForm] = useState<PessoaDTO>(membroInicial ?? membroVazio);

  if (!aberto) return null;

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSalvar(form);
  };

  const parentescoOptions = Object.entries(PARENTESCO_LABELS).filter(
    ([key]) => key !== "RESPONSAVEL"
  );

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50">
      <div className="w-full max-w-2xl rounded-2xl bg-white p-6 shadow-xl max-h-[90vh] overflow-y-auto">
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-lg font-bold text-gray-900">
            {membroInicial?.id ? "Editar Membro" : "Adicionar Membro"}
          </h2>
          <button
            onClick={onFechar}
            className="rounded-lg p-1.5 hover:bg-gray-100 transition"
          >
            <X className="h-5 w-5 text-gray-500" />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="space-y-4">
          {/* Nome e CPF */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Nome *
              </label>
              <input
                name="nome"
                value={form.nome}
                onChange={handleChange}
                required
                className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-blue-500 focus:ring-1 focus:ring-blue-500"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                CPF *
              </label>
              <input
                name="cpf"
                value={form.cpf}
                onChange={handleChange}
                required
                maxLength={11}
                placeholder="00000000000"
                className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-blue-500 focus:ring-1 focus:ring-blue-500 font-mono"
              />
            </div>
          </div>

          {/* Parentesco, Sexo, Nascimento */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Parentesco *
              </label>
              <select
                name="parentesco"
                value={form.parentesco}
                onChange={handleChange}
                required
                className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-blue-500 focus:ring-1 focus:ring-blue-500"
              >
                {parentescoOptions.map(([value, label]) => (
                  <option key={value} value={value}>
                    {label}
                  </option>
                ))}
              </select>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Sexo *
              </label>
              <select
                name="sexo"
                value={form.sexo}
                onChange={handleChange}
                required
                className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-blue-500 focus:ring-1 focus:ring-blue-500"
              >
                {Object.entries(SEXO_LABELS).map(([value, label]) => (
                  <option key={value} value={value}>
                    {label}
                  </option>
                ))}
              </select>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Data de Nascimento *
              </label>
              <input
                name="dataNascimento"
                type="date"
                value={form.dataNascimento}
                onChange={handleChange}
                required
                className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-blue-500 focus:ring-1 focus:ring-blue-500"
              />
            </div>
          </div>

          {/* Telefone e Renda */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Telefone
              </label>
              <input
                name="telefone"
                value={form.telefone ?? ""}
                onChange={handleChange}
                placeholder="(00) 00000-0000"
                className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-blue-500 focus:ring-1 focus:ring-blue-500"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Renda Mensal
              </label>
              <input
                name="rendaMensal"
                type="number"
                min={0}
                value={form.rendaMensal ?? ""}
                onChange={(e) =>
                  setForm((prev) => ({
                    ...prev,
                    rendaMensal: e.target.value ? Number(e.target.value) : undefined,
                  }))
                }
                className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-blue-500 focus:ring-1 focus:ring-blue-500"
              />
            </div>
          </div>

          {/* RG */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Nº RG
              </label>
              <input
                name="numeroRg"
                value={form.numeroRg ?? ""}
                onChange={handleChange}
                className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-blue-500 focus:ring-1 focus:ring-blue-500"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Órgão Expedidor
              </label>
              <input
                name="orgaoExpeditorRg"
                value={form.orgaoExpeditorRg ?? ""}
                onChange={handleChange}
                placeholder="SSP/PE"
                className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-blue-500 focus:ring-1 focus:ring-blue-500"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Data Expedição
              </label>
              <input
                name="dataExpedicaoRg"
                type="date"
                value={form.dataExpedicaoRg ?? ""}
                onChange={handleChange}
                className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-blue-500 focus:ring-1 focus:ring-blue-500"
              />
            </div>
          </div>

          {/* Botões */}
          <div className="flex justify-end gap-3 pt-4 border-t">
            <button
              type="button"
              onClick={onFechar}
              className="rounded-lg border border-gray-300 px-4 py-2 text-sm font-medium text-gray-700 hover:bg-gray-50 transition"
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="rounded-lg bg-blue-600 px-4 py-2 text-sm font-medium text-white hover:bg-blue-700 transition"
            >
              {membroInicial?.id ? "Salvar Alterações" : "Adicionar"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
