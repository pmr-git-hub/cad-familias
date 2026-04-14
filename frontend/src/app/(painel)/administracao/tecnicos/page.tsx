"use client";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Plus,
  Search,
  Pencil,
  Power,
  PowerOff,
  Users,
  Loader2,
} from "lucide-react";
import { Breadcrumb } from "@/components/ui/breadcrumb";
import { TecnicoForm } from "@/modules/tecnicos/components/tecnico-form";
import { TecnicoModalMudarStatus } from "@/modules/tecnicos/components/tecnico-modal-mudar-status";

import { useTecnicosPage } from "@/modules/tecnicos/hooks/use-tecnicos-page";
import { StatusBadge } from "@/components/ui/status-badge";
import { useEquipamentos } from "@/modules/equipamentos/hooks/use-equipamentos";

export default function TecnicosPage() {
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
    especialidadeFormatada,
  } = useTecnicosPage();

  const { equipamentos } = useEquipamentos();
  const equipamentosOptions = equipamentos.map((e) => ({
    id: e.id,
    nome: e.nome,
    }));

  return (
    <div className="space-y-6">
      {/* Breadcrumb */}
      <Breadcrumb
        items={[
          { label: "Administração", href: "/administracao" },
          { label: "Técnicos" },
        ]}
      />

      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Técnicos</h1>
          <p className="text-sm text-muted-foreground mt-1">
            Gerencie os técnicos da rede socioassistencial
          </p>
        </div>
        <Button onClick={handleNovo} className="gap-2">
          <Plus className="h-4 w-4" />
          Novo Técnico
        </Button>
      </div>

      {/* Busca */}
      <div className="relative max-w-sm">
        <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-gray-400" />
        <Input
          placeholder="Buscar por nome, CPF, especialidade..."
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
          <Users className="mx-auto h-12 w-12 text-gray-300" />
          <h3 className="mt-4 text-lg font-medium text-gray-900">
            {busca
              ? "Nenhum técnico encontrado"
              : "Nenhum técnico cadastrado"}
          </h3>
          <p className="mt-1 text-sm text-muted-foreground">
            {busca
              ? "Tente alterar os termos da busca."
              : "Comece cadastrando o primeiro técnico da rede."}
          </p>
          {!busca && (
            <Button onClick={handleNovo} className="mt-4 gap-2">
              <Plus className="h-4 w-4" />
              Cadastrar Técnico
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
                  CPF
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                  Especialidade
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                  Registro
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                  Equipamento
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
              {filtrados.map((tec) => (
                <tr
                  key={tec.id}
                  className="hover:bg-gray-50/50 transition-colors"
                >
                  <td className="px-6 py-4 text-sm font-medium text-gray-900">
                    {tec.nome}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-500">
                    {tec.cpf}
                  </td>
                  <td className="px-6 py-4">
                    <span className="inline-flex items-center rounded-md bg-purple-50 px-2 py-1 text-xs font-medium text-purple-700 ring-1 ring-purple-700/10">
                      {especialidadeFormatada(tec.especialidade)}
                    </span>
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-500">
                    {tec.registroProfissional || "—"}
                  </td>
                  <td className="px-6 py-4">
                    {tec.equipamentos.length > 0 ? (
                        <div className="flex flex-wrap gap-1">
                        {tec.equipamentos.map((eq) => (
                            <span
                            key={eq.id}
                            className="inline-flex items-center rounded-md bg-blue-50 px-2 py-0.5 text-xs font-medium text-blue-700 ring-1 ring-blue-700/10"
                            >
                            {eq.nomeEquipamento}
                            </span>
                        ))}
                        </div>
                    ) : (
                        <span className="text-sm text-gray-400">Sem vínculo</span>
                    )}
                    </td>

                  <td className="px-6 py-4">
                    <StatusBadge ativo={tec.ativo} />
                  </td>
                  <td className="px-6 py-4 text-right">
                    <div className="flex items-center justify-end gap-2">
                      <button
                        onClick={() => handleEditar(tec)}
                        className="rounded-lg p-2 text-gray-400 hover:bg-gray-100 hover:text-gray-600 transition-colors cursor-pointer"
                        title="Editar"
                      >
                        <Pencil className="h-4 w-4" />
                      </button>
                      {tec.ativo ? (
                        <button
                          onClick={() => setMudandoStatus(tec)}
                          className="rounded-lg p-2 text-gray-400 hover:bg-red-50 hover:text-red-600 transition-colors cursor-pointer"
                          title="Desativar"
                        >
                          <Power className="h-4 w-4" />
                        </button>
                      ) : (
                        <button
                          onClick={() => setMudandoStatus(tec)}
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
      <TecnicoForm
        open={formOpen}
        tecnico={editando}
        onClose={handleFecharForm}
        onSubmit={handleSubmit}
        equipamentosDisponiveis={equipamentosOptions}
        loading={submitting}
        />

      <TecnicoModalMudarStatus
        tecnico={mudandoStatus}
        open={!!mudandoStatus}
        onClose={handleFecharMudarStatus}
        onConfirm={handleMudarStatus}
        loading={submitting}
      />
    </div>
  );
}
