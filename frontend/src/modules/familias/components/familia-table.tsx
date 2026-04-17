// src/features/familias/components/familia-table.tsx

"use client";

import { Fragment, useState } from "react";
import { useRouter } from "next/navigation";
import { Pencil, Users, ChevronRight, ChevronDown, User } from "lucide-react";
import {
  FamiliaDTO,
  SITUACAO_LABELS,
  SITUACAO_COLORS,
} from "../types/familia";

interface Props {
  familias: FamiliaDTO[];
  loading: boolean;
}

export function FamiliaTable({ familias, loading }: Props) {
  const router = useRouter();
  const [expandedIds, setExpandedIds] = useState<Set<number>>(new Set());

  const toggleExpand = (id: number) => {
    setExpandedIds((prev) => {
      const next = new Set(prev);
      if (next.has(id)) {
        next.delete(id);
      } else {
        next.add(id);
      }
      return next;
    });
  };

  if (loading) {
    return (
      <div className="flex justify-center py-12">
        <div className="h-8 w-8 animate-spin rounded-full border-4 border-blue-500 border-t-transparent" />
      </div>
    );
  }

  if (familias.length === 0) {
    return (
      <div className="flex flex-col items-center justify-center py-12 text-gray-500">
        <Users className="h-12 w-12 mb-3 text-gray-300" />
        <p className="text-lg font-medium">Nenhuma família cadastrada</p>
        <p className="text-sm">Clique em Nova Família para começar.</p>
      </div>
    );
  }

  const formatCpf = (cpf: string) =>
    cpf?.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, "$1.$2.$3-$4") ?? "—";

  const formatRenda = (valor?: number) =>
    valor != null
      ? valor.toLocaleString("pt-BR", { style: "currency", currency: "BRL" })
      : "—";

  const calcularIdade = (dataNascimento?: string) => {
    if (!dataNascimento) return null;
    const hoje = new Date();
    const nascimento = new Date(dataNascimento);
    let idade = hoje.getFullYear() - nascimento.getFullYear();
    const m = hoje.getMonth() - nascimento.getMonth();
    if (m < 0 || (m === 0 && hoje.getDate() < nascimento.getDate())) {
      idade--;
    }
    return idade;
  };

  return (
    <div className="overflow-x-auto rounded-xl border border-gray-200 bg-white shadow-sm">
      <table className="min-w-full text-sm">
        <thead className="bg-gray-50 text-left text-xs font-semibold uppercase text-gray-500">
          <tr>
            <th className="w-10 px-2 py-3" />
            <th className="px-4 py-3">Pessoa Referência</th>
            <th className="px-4 py-3">CPF</th>
            <th className="px-4 py-3">Telefone</th>
            <th className="px-4 py-3">Membros</th>
            <th className="px-4 py-3">Renda Familiar</th>
            <th className="px-4 py-3">CadÚnico</th>
            <th className="px-4 py-3">Situação</th>
            <th className="px-4 py-3 text-right">Ações</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-gray-100">
          {familias.map((f) => {
            const isExpanded = f.id != null && expandedIds.has(f.id);
            const totalMembros = (f.membrosDaFamilia?.length ?? 0) + 1;

            return (
              <Fragment key={f.id ?? f.pessoaReferencia?.cpf}>
                {/* Linha principal */}
                <tr className="hover:bg-gray-50 transition-colors">
                  <td className="px-2 py-3 text-center">
                    <button
                      onClick={() => f.id != null && toggleExpand(f.id)}
                      className="rounded-md p-1 text-gray-400 hover:bg-gray-100 hover:text-gray-600 transition"
                      title={isExpanded ? "Recolher" : "Ver membros"}
                    >
                      {isExpanded ? (
                        <ChevronDown className="h-4 w-4" />
                      ) : (
                        <ChevronRight className="h-4 w-4" />
                      )}
                    </button>
                  </td>
                  <td className="px-4 py-3 font-medium text-gray-900">
                    {f.pessoaReferencia?.nome ?? "—"}
                  </td>
                  <td className="px-4 py-3 text-gray-600 font-mono">
                    {formatCpf(f.pessoaReferencia?.cpf)}
                  </td>
                  <td className="px-4 py-3 text-gray-600">
                    {f.pessoaReferencia?.telefone ?? "—"}
                  </td>
                  <td className="px-4 py-3 text-gray-600">
                    <button
                      onClick={() => f.id != null && toggleExpand(f.id)}
                      className="inline-flex items-center gap-1 hover:text-blue-600 transition"
                    >
                      <Users className="h-3.5 w-3.5" />
                      {totalMembros}
                    </button>
                  </td>
                  <td className="px-4 py-3 text-gray-600">
                    {formatRenda(f.rendaFamiliar)}
                  </td>
                  <td className="px-4 py-3 text-gray-600 font-mono">
                    {f.codigoCadunico || "—"}
                  </td>
                  <td className="px-4 py-3">
                    <span
                      className={`inline-block rounded-full px-2.5 py-0.5 text-xs font-semibold ${
                        SITUACAO_COLORS[f.situacao] ??
                        "bg-gray-100 text-gray-800"
                      }`}
                    >
                      {SITUACAO_LABELS[f.situacao] ?? f.situacao}
                    </span>
                  </td>
                  <td className="px-4 py-3 text-right">
                    <button
                      onClick={() => router.push(`/familias/${f.id}/editar`)}
                      className="rounded-lg p-1.5 text-gray-400 hover:bg-gray-100 hover:text-blue-600 transition"
                      title="Editar"
                    >
                      <Pencil className="h-4 w-4" />
                    </button>
                  </td>
                </tr>

                {/* Linha expandida — membros */}
                {isExpanded && (
                  <tr className="bg-blue-50/40">
                    <td colSpan={9} className="px-6 py-3">
                      <div className="grid gap-2 sm:grid-cols-2 lg:grid-cols-3">
                        {/* Pessoa Referência */}
                        <div className="flex items-center gap-3 rounded-lg bg-white px-3 py-2 border border-blue-200 shadow-sm">
                          <div className="flex h-8 w-8 items-center justify-center rounded-full bg-blue-100 text-blue-600">
                            <User className="h-4 w-4" />
                          </div>
                          <div className="min-w-0">
                            <p className="text-sm font-medium text-gray-900 truncate">
                              {f.pessoaReferencia?.nome ?? "—"}
                            </p>
                            <p className="text-xs text-blue-600 font-medium">
                              Referência
                              {f.pessoaReferencia?.dataNascimento &&
                                ` · ${calcularIdade(f.pessoaReferencia.dataNascimento)} anos`}
                              {f.pessoaReferencia?.rendaMensal != null &&
                                ` · ${formatRenda(f.pessoaReferencia.rendaMensal)}`}
                            </p>
                          </div>
                        </div>

                        {/* Demais membros */}
                        {f.membrosDaFamilia?.map((m) => (
                          <div
                            key={m.id}
                            className="flex items-center gap-3 rounded-lg bg-white px-3 py-2 border border-gray-200 shadow-sm"
                          >
                            <div className="flex h-8 w-8 items-center justify-center rounded-full bg-gray-100 text-gray-500">
                              <User className="h-4 w-4" />
                            </div>
                            <div className="min-w-0">
                              <p className="text-sm font-medium text-gray-900 truncate">
                                {m.nome}
                              </p>
                              <p className="text-xs text-gray-500">
                                {m.parentesco ?? "Membro"}
                                {m.dataNascimento &&
                                  ` · ${calcularIdade(m.dataNascimento)} anos`}
                                {m.rendaMensal != null &&
                                  ` · ${formatRenda(m.rendaMensal)}`}
                              </p>
                            </div>
                          </div>
                        ))}

                        {(!f.membrosDaFamilia ||
                          f.membrosDaFamilia.length === 0) && (
                          <p className="text-xs text-gray-400 italic col-span-full">
                            Nenhum outro membro cadastrado.
                          </p>
                        )}
                      </div>
                    </td>
                  </tr>
                )}
              </Fragment>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}
