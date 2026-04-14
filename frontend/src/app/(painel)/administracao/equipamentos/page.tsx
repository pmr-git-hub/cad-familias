"use client";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Plus,
  Search,
  Pencil,
  Power,
  PowerOff,
  MapPin,
  Loader2,
} from "lucide-react";
import { EquipamentoForm } from "@/modules/equipamentos/components/equipamento-form";
import { EquipamentoModalMudarStatus } from "@/modules/equipamentos/components/equipamento-modal-mudarStatus";
import { EquipamentoStatusBadge } from "@/modules/equipamentos/components/equipamento-status-badge";
import { useEquipamentosPage } from "@/modules/equipamentos/hooks/use-equipamentos-page";
import { Breadcrumb } from "@/components/ui/breadcrumb";

export default function EquipamentosPage() {
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
    enderecoFormatado,
  } = useEquipamentosPage();

  return (

      
      <div className="space-y-6">
        <Breadcrumb items={[
            { label: "Administração", href: "/administracao" },
            { label: "Equipamentos" },
        ]}/>
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Equipamentos</h1>
          <p className="text-sm text-muted-foreground mt-1">
            Gerencie os equipamentos da rede socioassistencial
          </p>
        </div>
        <Button onClick={handleNovo} className="gap-2">
          <Plus className="h-4 w-4" />
          Novo Equipamento
        </Button>
      </div>

      {/* Busca */}
      <div className="relative max-w-sm">
        <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-gray-400" />
        <Input
          placeholder="Buscar por nome, tipo, bairro ou cidade..."
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
          <MapPin className="mx-auto h-12 w-12 text-gray-300" />
          <h3 className="mt-4 text-lg font-medium text-gray-900">
            {busca
              ? "Nenhum equipamento encontrado"
              : "Nenhum equipamento cadastrado"}
          </h3>
          <p className="mt-1 text-sm text-muted-foreground">
            {busca
              ? "Tente alterar os termos da busca."
              : "Comece cadastrando o primeiro equipamento da rede."}
          </p>
          {!busca && (
            <Button onClick={handleNovo} className="mt-4 gap-2">
              <Plus className="h-4 w-4" />
              Cadastrar Equipamento
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
                  Tipo
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                  Endereço
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                  Contato
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
              {filtrados.map((equip) => (
                <tr
                  key={equip.id}
                  className="hover:bg-gray-50/50 transition-colors"
                >
                  <td className="px-6 py-4 text-sm font-medium text-gray-900">
                    {equip.nome}
                  </td>
                  <td className="px-6 py-4">
                    <span className="inline-flex items-center rounded-md bg-blue-50 px-2 py-1 text-xs font-medium text-blue-700 ring-1 ring-blue-700/10">
                      {equip.tipo}
                    </span>
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-500 max-w-xs truncate">
                    {enderecoFormatado(equip)}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-500">
                    <div className="flex flex-col">
                      {equip.telefone && <span>{equip.telefone}</span>}
                      {equip.email && (
                        <span className="text-xs text-gray-400 truncate max-w-[180px]">
                          {equip.email}
                        </span>
                      )}
                      {!equip.telefone && !equip.email && "—"}
                    </div>
                  </td>
                  <td className="px-6 py-4">
                    <EquipamentoStatusBadge ativo={equip.ativo} />
                  </td>
                  <td className="px-6 py-4 text-right">
                    <div className="flex items-center justify-end gap-2">
                      <button
                        onClick={() => handleEditar(equip)}
                        className="rounded-lg p-2 text-gray-400 hover:bg-gray-100 hover:text-gray-600 transition-colors cursor-pointer"
                        title="Editar"
                      >
                        <Pencil className="h-4 w-4" />
                      </button>
                      {equip.ativo ? (
                        <button
                          onClick={() => setMudandoStatus(equip)}
                          className="rounded-lg p-2 text-gray-400 hover:bg-red-50 hover:text-red-600 transition-colors cursor-pointer"
                          title="Desativar"
                        >
                          <Power className="h-4 w-4" />
                        </button>
                      ) : (
                        <button
                          onClick={() => setMudandoStatus(equip)}
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
      <EquipamentoForm
        equipamento={editando}
        open={formOpen}
        onClose={handleFecharForm}
        onSubmit={handleSubmit}
        loading={submitting}
      />

      <EquipamentoModalMudarStatus
        equipamento={mudandoStatus}
        open={!!mudandoStatus}
        onClose={handleFecharMudarStatus}
        onConfirm={handleMudarStatus}
        loading={submitting}
      />
     
    </div>
  );
}
