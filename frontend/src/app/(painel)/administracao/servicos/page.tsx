// app/(painel)/administracao/servicos/page.tsx

"use client";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Plus,
  Search,
  Pencil,
  Power,
  PowerOff,
  Shapes,
  Loader2,
  Users,
} from "lucide-react";
import { Breadcrumb } from "@/components/ui/breadcrumb";
import { ServicoForm } from "@/modules/servicos/components/servico-form";
import { ServicoModalMudarStatus } from "@/modules/servicos/components/servico-modal-mudar-status";
import { ServicoModalVincularPessoas } from "@/modules/servicos/components/servico-modal-vincular-pessoas";
import { useServicosPage } from "@/modules/servicos/hooks/use-servicos-page";
import { StatusBadge } from "@/components/ui/status-badge";
import { TIPO_ACOMPANHAMENTO_LABELS } from "@/modules/servicos/types/servico";

export default function ServicosPage() {
  const {
    filtrados,
    loading,
    error,
    busca,
    formOpen,
    editando,
    mudandoStatus,
    vinculando,
    submitting,
    equipamentoOpcoes,
    setBusca,
    setMudandoStatus,
    handleNovo,
    handleEditar,
    handleSubmit,
    handleMudarStatus,
    handleFecharForm,
    handleFecharMudarStatus,
    handleAbrirVincular,
    handleFecharVincular,
    getEquipamentoNome,
    formatFaixaEtaria,
  } = useServicosPage();

  return (
    <div className="space-y-6">
      {/* Breadcrumb */}
      <Breadcrumb
        items={[
          { label: "Administração", href: "/administracao" },
          { label: "Serviços" },
        ]}
      />

      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Serviços</h1>
          <p className="text-sm text-muted-foreground mt-1">
            Gerencie os serviços ofertados pelos equipamentos
          </p>
        </div>
        <Button onClick={handleNovo} className="gap-2">
          <Plus className="h-4 w-4" />
          Novo Serviço
        </Button>
      </div>

      {/* Busca */}
      <div className="relative max-w-sm">
        <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-gray-400" />
        <Input
          placeholder="Buscar por nome, equipamento, público-alvo..."
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
          <Shapes className="mx-auto h-12 w-12 text-gray-300" />
          <h3 className="mt-4 text-lg font-medium text-gray-900">
            {busca
              ? "Nenhum serviço encontrado"
              : "Nenhum serviço cadastrado"}
          </h3>
          <p className="mt-1 text-sm text-muted-foreground">
            {busca
              ? "Tente alterar os termos da busca."
              : "Comece cadastrando o primeiro serviço."}
          </p>
          {!busca && (
            <Button onClick={handleNovo} className="mt-4 gap-2">
              <Plus className="h-4 w-4" />
              Cadastrar Serviço
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
                  Equipamento
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500 hidden md:table-cell">
                  Público-alvo
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500 hidden md:table-cell">
                  Tipo
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500 hidden lg:table-cell">
                  Faixa Etária
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500 hidden lg:table-cell">
                  Dia / Horário
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                  Status
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                  Vinculados
                </th>
                <th className="px-6 py-3 text-right text-xs font-medium uppercase tracking-wider text-gray-500">
                  Ações
                </th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {filtrados.map((servico) => (
                <tr
                  key={servico.id}
                  className="hover:bg-gray-50/50 transition-colors"
                >
                  <td className="px-6 py-4 text-sm font-medium text-gray-900">
                    {servico.nome}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-500">
                    {getEquipamentoNome(servico.equipamentoId)}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-500 hidden md:table-cell">
                    {servico.publicoAlvo || "—"}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-500 hidden md:table-cell">
                    <span className="inline-flex items-center rounded-full px-2 py-0.5 text-xs font-medium bg-blue-50 text-blue-700">
                      {TIPO_ACOMPANHAMENTO_LABELS[servico.tipoAcompanhamento]}
                    </span>
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-500 hidden lg:table-cell">
                    {formatFaixaEtaria(
                      servico.faixaEtariaMin,
                      servico.faixaEtariaMax
                    )}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-500 hidden lg:table-cell">
                    {servico.diaSemana || servico.horario
                      ? `${servico.diaSemana ?? ""}${servico.diaSemana && servico.horario ? " • " : ""}${servico.horario ?? ""}`
                      : "—"}
                  </td>
                  <td className="px-6 py-4">
                    <StatusBadge ativo={servico.ativo} />
                  </td>
                  <td className="px-6 py-4">
                    <button
                      onClick={() => handleAbrirVincular(servico)}
                      className={`inline-flex items-center gap-1.5 rounded-full px-2.5 py-1 text-xs font-medium transition-colors cursor-pointer ${
                        servico.totalPessoasVinculadas > 0
                          ? "bg-blue-50 text-blue-700 hover:bg-blue-100"
                          : "bg-gray-100 text-gray-400 hover:bg-gray-200"
                      }`}
                    >
                      <Users className="h-3.5 w-3.5" />
                      {servico.totalPessoasVinculadas ?? 0}
                    </button>
                  </td>

                  <td className="px-6 py-4 text-right">
                    <div className="flex items-center justify-end gap-2">
                      <button
                        onClick={() => handleAbrirVincular(servico)}
                        className="rounded-lg p-2 text-gray-400 hover:bg-blue-50 hover:text-blue-600 transition-colors cursor-pointer"
                        title="Vincular pessoas"
                      >
                        <Users className="h-4 w-4" />
                      </button>
                      <button
                        onClick={() => handleEditar(servico)}
                        className="rounded-lg p-2 text-gray-400 hover:bg-gray-100 hover:text-gray-600 transition-colors cursor-pointer"
                        title="Editar"
                      >
                        <Pencil className="h-4 w-4" />
                      </button>
                      {servico.ativo ? (
                        <button
                          onClick={() => setMudandoStatus(servico)}
                          className="rounded-lg p-2 text-gray-400 hover:bg-red-50 hover:text-red-600 transition-colors cursor-pointer"
                          title="Desativar"
                        >
                          <Power className="h-4 w-4" />
                        </button>
                      ) : (
                        <button
                          onClick={() => setMudandoStatus(servico)}
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
      <ServicoForm
        open={formOpen}
        servico={editando}
        onClose={handleFecharForm}
        onSubmit={handleSubmit}
        equipamentosDisponiveis={equipamentoOpcoes}
        loading={submitting}
      />

      <ServicoModalMudarStatus
        servico={mudandoStatus}
        open={!!mudandoStatus}
        onClose={handleFecharMudarStatus}
        onConfirm={handleMudarStatus}
        loading={submitting}
      />

      <ServicoModalVincularPessoas
        servico={vinculando}
        open={!!vinculando}
        onClose={handleFecharVincular}
      />
    </div>
  );
}
