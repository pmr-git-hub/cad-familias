// modules/servicos/components/servico-modal-vincular-pessoas.tsx

"use client";

import {
  Search,
  Users,
  ArrowLeft,
  CheckCircle2,
  AlertTriangle,
  Loader2,
  UserMinus,
  UserPlus,
  ChevronRight
} from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { useVincularPessoasServico } from "../hooks/use-vincular-pessoas-servico";
import type { Servico } from "../types/servico";

interface Props {
  servico: Servico | null;
  open: boolean;
  onClose: () => void;
}

export function ServicoModalVincularPessoas({ servico, open, onClose }: Props) {
  const {
    vinculosAtivos,
    loadingVinculos,
    vinculoIdsParaDesligar,
    toggleDesligar,
    busca,
    setBusca,
    loadingFamilias,
    familiasFiltradas,
    familiaSelecionada,
    pessoasDaFamilia,
    pessoaIdsParaVincular,
    selecionarFamilia,
    voltarParaBusca,
    togglePessoaVincular,
    selecionarTodosDaFamilia,
    limparSelecaoNovos,
    etapa,
    motivoSaida,
    setMotivoSaida,
    totalAlteracoes,
    submitting,
    resultado,
    erro,
    iniciarSalvar,
    cancelarConfirmacao,
    executarSalvar,
  } = useVincularPessoasServico(servico, open);

  if (!open || !servico) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 p-4">
      <div className="w-full max-w-lg rounded-xl bg-white shadow-lg max-h-[85vh] flex flex-col">
        {/* Header */}
        <div className="flex items-center justify-between border-b border-gray-100 px-6 py-4">
          <div className="flex items-center gap-2">
            <Users className="h-5 w-5 text-gray-500" />
            <h2 className="text-lg font-semibold text-gray-900">
              Pessoas vinculadas — {servico.nome}
            </h2>
          </div>
          <button
            onClick={onClose}
            className="rounded-lg p-1 text-gray-400 hover:bg-gray-100 hover:text-gray-600 cursor-pointer"
          >
            ✕
          </button>
        </div>

        <div className="flex-1 overflow-y-auto px-6 py-4 space-y-5">
          {/* Resultado do último salvamento */}
          {resultado && (
            <div className="space-y-2">
              {resultado.vincular && resultado.vincular.sucesso.length > 0 && (
                <div className="rounded-lg border border-green-200 bg-green-50 p-3">
                  <p className="flex items-center gap-2 text-sm font-medium text-green-700">
                    <CheckCircle2 className="h-4 w-4" />
                    {resultado.vincular.sucesso.length} pessoa(s) vinculada(s)
                  </p>
                </div>
              )}
              {resultado.desligar && resultado.desligar.sucesso.length > 0 && (
                <div className="rounded-lg border border-blue-200 bg-blue-50 p-3">
                  <p className="flex items-center gap-2 text-sm font-medium text-blue-700">
                    <CheckCircle2 className="h-4 w-4" />
                    {resultado.desligar.sucesso.length} pessoa(s) desligada(s)
                  </p>
                </div>
              )}
              {(resultado.vincular?.falhas.length ?? 0) > 0 && (
                <div className="rounded-lg border border-red-200 bg-red-50 p-3">
                  <p className="flex items-center gap-2 text-sm font-medium text-red-700">
                    <AlertTriangle className="h-4 w-4" />
                    Falhas ao vincular
                  </p>
                  <ul className="mt-2 space-y-1 text-xs text-red-700">
                    {resultado.vincular!.falhas.map((f) => (
                      <li key={f.pessoaId}>• {f.pessoaNome}: {f.motivo}</li>
                    ))}
                  </ul>
                </div>
              )}
              {(resultado.desligar?.falhas.length ?? 0) > 0 && (
                <div className="rounded-lg border border-red-200 bg-red-50 p-3">
                  <p className="flex items-center gap-2 text-sm font-medium text-red-700">
                    <AlertTriangle className="h-4 w-4" />
                    Falhas ao desligar
                  </p>
                  <ul className="mt-2 space-y-1 text-xs text-red-700">
                    {resultado.desligar!.falhas.map((f) => (
                      <li key={f.vinculoId}>• Vínculo #{f.vinculoId}: {f.motivo}</li>
                    ))}
                  </ul>
                </div>
              )}
            </div>
          )}

          {erro && (
            <div className="rounded-lg border border-red-200 bg-red-50 p-3 text-sm text-red-600">
              {erro}
            </div>
          )}

          {/* ETAPA: confirmação de desligamento */}
          {etapa === "confirmarDesligamento" && (
            <div className="space-y-4">
              <div className="rounded-lg border border-amber-200 bg-amber-50 p-3">
                <p className="flex items-center gap-2 text-sm font-medium text-amber-700">
                  <AlertTriangle className="h-4 w-4" />
                  Confirmar desligamento de {vinculoIdsParaDesligar.size} pessoa(s)
                </p>
                <ul className="mt-2 space-y-1 text-xs text-amber-800">
                  {vinculosAtivos
                    .filter((v) => vinculoIdsParaDesligar.has(v.id))
                    .map((v) => (
                      <li key={v.id}>• {v.pessoaNome}</li>
                    ))}
                </ul>
                <p className="mt-2 text-xs text-amber-700">
                  O histórico de atendimento será mantido. Apenas o vínculo ativo será encerrado.
                </p>
              </div>

              <div>
                <label className="text-sm font-medium text-gray-700">
                  Motivo da saída
                </label>
                <textarea
                  value={motivoSaida}
                  onChange={(e) => setMotivoSaida(e.target.value)}
                  placeholder="Descreva o motivo do desligamento..."
                  rows={3}
                  autoFocus
                  className="mt-1 w-full rounded-lg border border-gray-200 px-3 py-2 text-sm text-gray-900 placeholder:text-gray-400 focus:border-gray-400 focus:outline-none resize-none"
                />
              </div>
            </div>
          )}

          {/* ETAPA: lista principal */}
          {etapa === "lista" && (
            <>
              {/* Seção 1: pessoas vinculadas atualmente */}
              <section className="space-y-2">
                <p className="text-sm font-medium text-gray-700">
                  Vinculados atualmente ({vinculosAtivos.length})
                </p>

                {loadingVinculos && (
                  <div className="flex justify-center py-4">
                    <Loader2 className="h-5 w-5 animate-spin text-gray-400" />
                  </div>
                )}

                {!loadingVinculos && vinculosAtivos.length === 0 && (
                  <p className="text-sm text-gray-500 py-2">
                    Nenhuma pessoa vinculada a este serviço ainda.
                  </p>
                )}

                <div className="space-y-1 max-h-48 overflow-y-auto">
                  {vinculosAtivos.map((v) => {
                    const marcadoParaSair = vinculoIdsParaDesligar.has(v.id);
                    return (
                      <label
                        key={v.id}
                        className={`flex items-center gap-3 rounded-lg border p-3 cursor-pointer transition-colors ${
                          marcadoParaSair
                            ? "border-red-200 bg-red-50"
                            : "border-gray-100 hover:bg-gray-50"
                        }`}
                      >
                        <input
                          type="checkbox"
                          checked={!marcadoParaSair}
                          onChange={() => toggleDesligar(v.id)}
                          className="h-4 w-4 rounded border-gray-300"
                        />
                        <div className="flex-1">
                          <p className="text-sm font-medium text-gray-900">{v.pessoaNome}</p>
                          <p className="text-xs text-gray-500">
                            Desde {v.dataEntrada}
                          </p>
                        </div>
                        {marcadoParaSair && (
                          <span className="flex items-center gap-1 text-xs font-medium text-red-600">
                            <UserMinus className="h-3.5 w-3.5" />
                            Desligar
                          </span>
                        )}
                      </label>
                    );
                  })}
                </div>
              </section>

              <div className="border-t border-gray-100" />

              {/* Seção 2: adicionar novas pessoas */}
              <section className="space-y-2">
                <p className="text-sm font-medium text-gray-700">Adicionar pessoas</p>

                {!familiaSelecionada && (
                  <>
                    <div className="relative">
                      <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-gray-400" />
                      <Input
                        placeholder="Buscar família por nome, CPF ou CadÚnico..."
                        value={busca}
                        onChange={(e) => setBusca(e.target.value)}
                        className="pl-10"
                      />
                    </div>

                    {loadingFamilias && (
                      <div className="flex justify-center py-4">
                        <Loader2 className="h-5 w-5 animate-spin text-gray-400" />
                      </div>
                    )}

                    {!loadingFamilias && busca.trim() !== "" && familiasFiltradas.length === 0 && (
                      <p className="text-center text-sm text-gray-500 py-3">
                        Nenhuma família encontrada.
                      </p>
                    )}

                    <div className="space-y-1.5">
                      {familiasFiltradas.map((familia) => (
                        <button
                          key={familia.id}
                          onClick={() => selecionarFamilia(familia)}
                          className="w-full rounded-lg border border-gray-100 border-l-[3px] border-l-blue-300 bg-white p-3 text-left hover:bg-gray-50 hover:border-gray-300 hover:border-l-blue-400 transition-colors cursor-pointer flex items-center gap-3"
                        >
                          <div className="flex h-9 w-9 shrink-0 items-center justify-center rounded-full bg-blue-50 text-sm font-semibold text-blue-600">
                            {familia.pessoaReferencia?.nome?.charAt(0).toUpperCase() ?? "?"}
                          </div>

                          <div className="min-w-0 flex-1">
                            <p className="text-[11px] font-medium uppercase tracking-wide text-blue-500">
                              Família de {familia.pessoaReferencia?.nome?.split(" ")[0]}
                            </p>
                            <p className="truncate text-sm font-medium text-gray-900">
                              {familia.pessoaReferencia?.nome}
                            </p>
                            <p className="text-xs text-gray-500">
                              {familia.codigoCadunico
                                ? `CadÚnico: ${familia.codigoCadunico}`
                                : `CPF: ${familia.pessoaReferencia?.cpf ?? "—"}`}
                            </p>
                          </div>

                          <div className="flex shrink-0 items-center gap-1.5 text-gray-400">
                            <span className="flex items-center gap-1 rounded-full bg-gray-100 px-2 py-0.5 text-[11px] font-medium text-gray-500">
                              <Users className="h-3 w-3" />
                              {familia.membrosDaFamilia.length + 1} pessoas
                            </span>
                            <ChevronRight className="h-4 w-4" />
                          </div>
                        </button>
                      ))}
                    </div>


                  </>
                )}

                {familiaSelecionada && (
                  <>
                    <button
                      onClick={voltarParaBusca}
                      className="flex items-center gap-1 text-sm text-gray-500 hover:text-gray-700 cursor-pointer"
                    >
                      <ArrowLeft className="h-4 w-4" />
                      Voltar à busca
                    </button>

                    <div className="rounded-lg bg-gray-50 p-3">
                      <p className="text-sm font-medium text-gray-900">
                        {familiaSelecionada.pessoaReferencia?.nome}
                      </p>
                      <p className="text-xs text-gray-500">
                        {familiaSelecionada.codigoCadunico
                          ? `CadÚnico: ${familiaSelecionada.codigoCadunico}`
                          : ""}
                      </p>
                    </div>

                    {pessoasDaFamilia.length === 0 ? (
                      <p className="text-sm text-gray-500 py-2">
                        Todas as pessoas desta família já estão vinculadas a este serviço.
                      </p>
                    ) : (
                      <>
                        <div className="flex items-center justify-between">
                          <p className="text-xs text-gray-500">Selecione para vincular:</p>
                          <div className="flex gap-2">
                            <button
                              onClick={selecionarTodosDaFamilia}
                              className="text-xs text-blue-600 hover:underline cursor-pointer"
                            >
                              Selecionar todos
                            </button>
                            <button
                              onClick={limparSelecaoNovos}
                              className="text-xs text-gray-500 hover:underline cursor-pointer"
                            >
                              Limpar
                            </button>
                          </div>
                        </div>

                        <div className="space-y-1 max-h-48 overflow-y-auto">
                          {pessoasDaFamilia.map((pessoa) => {
                            const selecionado = pessoaIdsParaVincular.has(pessoa.id!);
                            return (
                              <label
                                key={pessoa.id}
                                className={`flex items-center gap-3 rounded-lg border p-3 cursor-pointer transition-colors ${
                                  selecionado
                                    ? "border-green-200 bg-green-50"
                                    : "border-gray-100 hover:bg-gray-50"
                                }`}
                              >
                                <input
                                  type="checkbox"
                                  checked={selecionado}
                                  onChange={() => togglePessoaVincular(pessoa.id!)}
                                  className="h-4 w-4 rounded border-gray-300"
                                />
                                <div className="flex-1">
                                  <p className="text-sm font-medium text-gray-900">{pessoa.nome}</p>
                                  <p className="text-xs text-gray-500">
                                    {pessoa.referencia ? "Responsável" : pessoa.parentesco} ·{" "}
                                    {pessoa.dataNascimento}
                                  </p>
                                </div>
                                {selecionado && (
                                  <span className="flex items-center gap-1 text-xs font-medium text-green-600">
                                    <UserPlus className="h-3.5 w-3.5" />
                                    Vincular
                                  </span>
                                )}
                              </label>
                            );
                          })}
                        </div>
                      </>
                    )}
                  </>
                )}
              </section>
            </>
          )}
        </div>

        {/* Footer */}
        <div className="flex items-center justify-end gap-2 border-t border-gray-100 px-6 py-4">
          {etapa === "confirmarDesligamento" ? (
            <>
              <Button variant="outline" onClick={cancelarConfirmacao} disabled={submitting}>
                Voltar
              </Button>
              <Button
                onClick={executarSalvar}
                disabled={submitting || motivoSaida.trim() === ""}
                className="gap-2"
              >
                {submitting && <Loader2 className="h-4 w-4 animate-spin" />}
                Confirmar desligamento
              </Button>
            </>
          ) : (
            <>
              <Button variant="outline" onClick={onClose} disabled={submitting}>
                Fechar
              </Button>
              <Button
                onClick={iniciarSalvar}
                disabled={totalAlteracoes === 0 || submitting}
                className="gap-2"
              >
                {submitting && <Loader2 className="h-4 w-4 animate-spin" />}
                Salvar {totalAlteracoes > 0 ? `(${totalAlteracoes})` : ""}
              </Button>
            </>
          )}
        </div>
      </div>
    </div>
  );
}
