// modules/programas/page.tsx (ou app/(painel)/administracao/programas/page.tsx)

"use client";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Plus,
  Search,
  Pencil,
  Power,
  PowerOff,
  BookOpen,
  Loader2,
} from "lucide-react";
import { Breadcrumb } from "@/components/ui/breadcrumb";
import { ProgramaForm } from "@/modules/programas/components/programa-form";
import { ProgramaModalMudarStatus } from "@/modules/programas/components/programa-modal-mudar-status";
import { useProgramasPage } from "@/modules/programas/hooks/use-programas-page";
import { StatusBadge } from "@/components/ui/status-badge";

export default function ProgramasPage() {
  const {
    filtrados,
    loading,
    error,
    busca,
    formOpen,
    editando,
    mudandoStatus,
    submitting,
    setBusca,
    setMudandoStatus,
    handleNovo,
    handleEditar,
    handleSubmit,
    handleMudarStatus,
    handleFecharForm,
    handleFecharMudarStatus,
  } = useProgramasPage();

  return (
    <div className="space-y-6">
      {/* Breadcrumb */}
      <Breadcrumb
        items={[
          { label: "Administração", href: "/administracao" },
          { label: "Programas Sociais" },
        ]}
      />

      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">
            Programas Sociais
          </h1>
          <p className="text-sm text-muted-foreground mt-1">
            Gerencie os programas sociais disponíveis na rede
          </p>
        </div>
        <Button onClick={handleNovo} className="gap-2">
          <Plus className="h-4 w-4" />
          Novo Programa
        </Button>
      </div>

      {/* Busca */}
      <div className="relative max-w-sm">
        <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-gray-400" />
        <Input
          placeholder="Buscar por nome, órgão gestor..."
          value={busca}
          onChange={(e) => setBusca(e.target.value)}
          className="pl-10"
        />
      </div>

      {/* Loading */}
      {loading && (
        <div className="flex items-center justify-center h-64">
          <Loader2 className="h-8 w-8 animate-spin text-gray-400" />
        </div>
      )}

      {/* Erro */}
      {!loading && error && (
        <div className="rounded-xl border border-red-200 bg-red-50 p-6 text-center">
          <p className="text-sm text-red-600">{error}</p>
        </div>
      )}

      {/* Vazio */}
      {!loading && !error && filtrados.length === 0 && (
        <div className="rounded-xl border border-gray-200 bg-white p-12 text-center">
          <BookOpen className="mx-auto h-12 w-12 text-gray-300" />
          <h3 className="mt-4 text-lg font-medium text-gray-900">
            {busca
              ? "Nenhum programa encontrado"
              : "Nenhum programa cadastrado"}
          </h3>
          <p className="mt-1 text-sm text-muted-foreground">
            {busca
              ? "Tente alterar os termos da busca."
              : "Comece cadastrando o primeiro programa social."}
          </p>
          {!busca && (
            <Button onClick={handleNovo} className="mt-4 gap-2">
              <Plus className="h-4 w-4" />
              Cadastrar Programa
            </Button>
          )}
        </div>
      )}

      {/* Tabela */}
      {!loading && !error && filtrados.length > 0 && (
        <div className="rounded-xl border border-gray-200 bg-white shadow-sm overflow-hidden">
          <table className="w-full">
            <thead>
              <tr className="border-b border-gray-100 bg-gray-50/50">
                <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                  Nome
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                  Órgão Gestor
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                  Critérios
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                  Status
                </th>
                <th className="px-6 py-3 text-right text-xs font-medium uppercase tracking-wider text-gray-500">
                  Ações
                </th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {filtrados.map((prog) => (
                <tr
                  key={prog.id}
                  className="hover:bg-gray-50/50 transition-colors"
                >
                  <td className="px-6 py-4 text-sm font-medium text-gray-900">
                    {prog.nome}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-500">
                    {prog.orgaoGestor || "—"}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-500 max-w-xs">
                    {prog.criterios ? (
                      <span
                        className="line-clamp-2"
                        title={prog.criterios}
                      >
                        {prog.criterios}
                      </span>
                    ) : (
                      "—"
                    )}
                  </td>
                  <td className="px-6 py-4">
                    <StatusBadge ativo={prog.ativo} />
                  </td>
                  <td className="px-6 py-4 text-right">
                    <div className="flex items-center justify-end gap-2">
                      <button
                        onClick={() => handleEditar(prog)}
                        className="rounded-lg p-2 text-gray-400 hover:bg-gray-100 hover:text-gray-600 transition-colors cursor-pointer"
                        title="Editar"
                      >
                        <Pencil className="h-4 w-4" />
                      </button>
                      {prog.ativo ? (
                        <button
                          onClick={() => setMudandoStatus(prog)}
                          className="rounded-lg p-2 text-gray-400 hover:bg-red-50 hover:text-red-600 transition-colors cursor-pointer"
                          title="Desativar"
                        >
                          <Power className="h-4 w-4" />
                        </button>
                      ) : (
                        <button
                          onClick={() => setMudandoStatus(prog)}
                          className="rounded-lg p-2 text-gray-400 hover:bg-green-50 hover:text-green-600 transition-colors cursor-pointer"
                          title="Reativar"
                        >
                          <PowerOff className="h-4 w-4" />
                        </button>
                      )}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* Modais */}
      <ProgramaForm
        open={formOpen}
        programa={editando}
        onClose={handleFecharForm}
        onSubmit={handleSubmit}
        loading={submitting}
      />

      <ProgramaModalMudarStatus
        programa={mudandoStatus}
        open={!!mudandoStatus}
        onClose={handleFecharMudarStatus}
        onConfirm={handleMudarStatus}
        loading={submitting}
      />
    </div>
  );
}
