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
  Shield,
  User,
} from "lucide-react";
import { Breadcrumb } from "@/components/ui/breadcrumb";
import { StatusBadge } from "@/components/ui/status-badge";
import { UsuarioForm } from "@/modules/usuarios/components/usuario-form";
import { UsuarioModalMudarStatus } from "@/modules/usuarios/components/usuario-modal-mudar-status";
import { useUsuariosPage } from "@/modules/usuarios/hooks/use-usuarios-page";
import { useTecnicos } from "@/modules/tecnicos/hooks/use-tecnicos";

export default function UsuariosPage() {
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
    perfilFormatado,
  } = useUsuariosPage();

  const { tecnicos } = useTecnicos();
  const tecnicosOptions = tecnicos
    .filter((t) => t.ativo)
    .map((t) => ({
      id: t.id,
      nome: t.nome,
      especialidade: t.especialidade,
    }));

  return (
    <div className="space-y-6">
      {/* Breadcrumb */}
      <Breadcrumb
        items={[
          { label: "Administração", href: "/administracao" },
          { label: "Usuários" },
        ]}
      />

      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Usuários</h1>
          <p className="text-sm text-muted-foreground mt-1">
            Gerencie os usuários de acesso ao sistema
          </p>
        </div>
        <Button onClick={handleNovo} className="gap-2">
          <Plus className="h-4 w-4" />
          Novo Usuário
        </Button>
      </div>

      {/* Busca */}
      <div className="relative max-w-sm">
        <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-gray-400" />
        <Input
          placeholder="Buscar por usuário, técnico, perfil..."
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
              ? "Nenhum usuário encontrado"
              : "Nenhum usuário cadastrado"}
          </h3>
          <p className="mt-1 text-sm text-muted-foreground">
            {busca
              ? "Tente alterar os termos da busca."
              : "Comece cadastrando o primeiro usuário do sistema."}
          </p>
          {!busca && (
            <Button onClick={handleNovo} className="mt-4 gap-2">
              <Plus className="h-4 w-4" />
              Cadastrar Usuário
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
                  Usuário
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                  Técnico Vinculado
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                  Perfil
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                  Último Acesso
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
              {filtrados.map((usr) => (
                <tr
                  key={usr.id}
                  className="hover:bg-gray-50/50 transition-colors"
                >
                  <td className="px-6 py-4 text-sm font-medium text-gray-900">
                    {usr.username}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-500">
                    {usr.tecnico?.nome ?? "—"}
                  </td>
                  <td className="px-6 py-4">
                    <span
                      className={`inline-flex items-center gap-1.5 rounded-md px-2 py-1 text-xs font-medium ring-1 ${
                        usr.perfil === "ADMIN"
                          ? "bg-amber-50 text-amber-700 ring-amber-700/10"
                          : "bg-blue-50 text-blue-700 ring-blue-700/10"
                      }`}
                    >
                      {usr.perfil === "ADMIN" ? (
                        <Shield className="h-3 w-3" />
                      ) : (
                        <User className="h-3 w-3" />
                      )}
                      {perfilFormatado(usr.perfil)}
                    </span>
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-500">
                    {usr.ultimoAcesso
                      ? new Date(usr.ultimoAcesso).toLocaleString("pt-BR")
                      : "Nunca acessou"}
                  </td>
                  <td className="px-6 py-4">
                    <StatusBadge ativo={usr.ativo} />
                  </td>
                  <td className="px-6 py-4 text-right">
                    <div className="flex items-center justify-end gap-2">
                      <button
                        onClick={() => handleEditar(usr)}
                        className="rounded-lg p-2 text-gray-400 hover:bg-gray-100 hover:text-gray-600 transition-colors cursor-pointer"
                        title="Editar"
                      >
                        <Pencil className="h-4 w-4" />
                      </button>
                      {usr.ativo ? (
                        <button
                          onClick={() => setMudandoStatus(usr)}
                          className="rounded-lg p-2 text-gray-400 hover:bg-red-50 hover:text-red-600 transition-colors cursor-pointer"
                          title="Desativar"
                        >
                          <Power className="h-4 w-4" />
                        </button>
                      ) : (
                        <button
                          onClick={() => setMudandoStatus(usr)}
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
      <UsuarioForm
        open={formOpen}
        usuario={editando}
        onClose={handleFecharForm}
        onSubmit={handleSubmit}
        tecnicosDisponiveis={tecnicosOptions}
        loading={submitting}
      />

      <UsuarioModalMudarStatus
        usuario={mudandoStatus}
        open={!!mudandoStatus}
        onClose={handleFecharMudarStatus}
        onConfirm={handleMudarStatus}
        loading={submitting}
      />
    </div>
  );
}
