// src/modules/atendimentos/components/ProntuariosPage.tsx
'use client'

import { useState } from 'react'
import { useProntuariosDaFamilia } from '@/modules/atendimentos/hooks/useProntuarios'
import { useAtendimentosDoProntuario } from '@/modules/atendimentos/hooks/useAtendimentos'
import { FormAbrirProntuario } from '@/modules/atendimentos/components/FormAbrirProntuario'
import { FormEncerrarProntuario } from '@/modules/atendimentos/components/FormEncerrarProntuario'
import { FormAtendimento } from '@/modules/atendimentos/components/FormAtendimento'
import { TIPO_ATENDIMENTO_LABELS, MODALIDADE_ATENDIMENTO_LABELS } from '@/modules/atendimentos/types/enums'
import type { ProntuarioRespostaDTO } from '@/modules/atendimentos/types/prontuario'
import type { AtendimentoRespostaDTO } from '@/modules/atendimentos/types/atendimento'
import type { FamiliaDTO } from '@/modules/familias/types/familia'

interface Props {
  familiaId: number
  familia: FamiliaDTO
}

const STATUS_STYLES: Record<string, string> = {
  ABERTO:    'bg-green-100 text-green-700',
  SUSPENSO:  'bg-amber-100 text-amber-700',
  ENCERRADO: 'bg-gray-200 text-gray-600',
}

const STATUS_LABELS: Record<string, string> = {
  ABERTO:    'Aberto',
  SUSPENSO:  'Suspenso',
  ENCERRADO: 'Encerrado',
}

export function ProntuariosPage({ familiaId, familia }: Props) {
  const { data: prontuarios, isLoading } = useProntuariosDaFamilia(familiaId)

  const [prontuarioSelecionado, setProntuarioSelecionado] =
    useState<ProntuarioRespostaDTO | null>(null)
  const [abrindoNovo, setAbrindoNovo]                     = useState(false)
  const [encerrando, setEncerrando]                       = useState(false)
  const [registrandoAtendimento, setRegistrandoAtendimento] = useState(false)

  const { data: atendimentos } = useAtendimentosDoProntuario(
    prontuarioSelecionado?.id ?? 0
  )

  if (isLoading) {
    return <p className="text-sm text-gray-400">Carregando prontuários...</p>
  }

  const prontuarioAberto = prontuarioSelecionado?.status === 'ABERTO'

  return (
    <div className="flex flex-col gap-6 max-w-4xl">

      {/* Header */}
      <div className="flex items-center justify-between">
        <h2 className="text-lg font-semibold text-gray-700">Prontuários</h2>
        <button
          onClick={() => { setAbrindoNovo(v => !v) }}
          className="bg-blue-600 text-white rounded px-4 py-2 text-sm hover:bg-blue-700"
        >
          + Abrir Prontuário
        </button>
      </div>

      {/* Form novo prontuário */}
      {abrindoNovo && (
        <div className="border rounded p-4 bg-gray-50">
          <h3 className="font-semibold text-gray-700 mb-3">Novo Prontuário</h3>
          <FormAbrirProntuario
            familiaId={familiaId}
            onSucesso={() => setAbrindoNovo(false)}
          />
        </div>
      )}

      {/* Lista de prontuários */}
      <div className="flex flex-col gap-3">
        {prontuarios?.map((p: ProntuarioRespostaDTO) => (
          <div
            key={p.id}
            onClick={() => {
              setProntuarioSelecionado(p)
              setEncerrando(false)
              setRegistrandoAtendimento(false)
            }}
            className={`border rounded p-4 cursor-pointer transition ${
              prontuarioSelecionado?.id === p.id
                ? 'border-blue-500 bg-blue-50'
                : 'hover:bg-gray-50'
            }`}
          >
            <div className="flex justify-between items-center">
              <div>
                <span className="font-medium text-gray-800">{p.equipamentoNome}</span>
                <span className="ml-2 text-xs text-gray-500">
                  Técnico: {p.tecnicoNome}
                </span>
              </div>
              <span className={`text-xs px-2 py-1 rounded-full font-medium ${STATUS_STYLES[p.status]}`}>
                {STATUS_LABELS[p.status]}
              </span>
            </div>
            <div className="text-xs text-gray-400 mt-1">
              Abertura: {new Date(p.dataAbertura).toLocaleDateString('pt-BR')}
              {p.dataFechamento &&
                ` · Fechamento: ${new Date(p.dataFechamento).toLocaleDateString('pt-BR')}`}
            </div>
          </div>
        ))}

        {!prontuarios?.length && (
          <p className="text-sm text-gray-500">Nenhum prontuário encontrado.</p>
        )}
      </div>

      {/* Detalhe do prontuário selecionado */}
      {prontuarioSelecionado && (
        <div className="border rounded p-4 bg-white flex flex-col gap-4">
          <div className="flex items-center justify-between">
            <h3 className="font-semibold text-gray-700">
              {prontuarioSelecionado.equipamentoNome}
            </h3>

            {prontuarioAberto && (
              <div className="flex gap-2">
                <button
                  onClick={() => {
                    setRegistrandoAtendimento(v => !v)
                    setEncerrando(false)
                  }}
                  className="text-sm bg-green-600 text-white rounded px-3 py-1 hover:bg-green-700"
                >
                  + Atendimento
                </button>
                <button
                  onClick={() => {
                    setEncerrando(v => !v)
                    setRegistrandoAtendimento(false)
                  }}
                  className="text-sm bg-red-100 text-red-600 rounded px-3 py-1 hover:bg-red-200"
                >
                  Encerrar
                </button>
              </div>
            )}
          </div>

          {/* Form novo atendimento */}
          {registrandoAtendimento && (
            <div className="border rounded p-4 bg-gray-50">
              <h4 className="text-sm font-semibold text-gray-700 mb-3">
                Novo Atendimento
              </h4>
              <FormAtendimento
                prontuarioId={prontuarioSelecionado.id}
                familia={familia}
                onSucesso={() => setRegistrandoAtendimento(false)}
              />
            </div>
          )}

          {/* Form encerrar prontuário */}
          {encerrando && (
            <div className="border rounded p-4 bg-gray-50">
              <h4 className="text-sm font-semibold text-gray-700 mb-3">
                Encerrar Prontuário
              </h4>
              <FormEncerrarProntuario
                prontuarioId={prontuarioSelecionado.id}
                familiaId={familiaId}
                onSucesso={() => {
                  setEncerrando(false)
                  setProntuarioSelecionado(null)
                }}
              />
            </div>
          )}

          {/* Lista de atendimentos */}
          <div className="flex flex-col gap-2">
            <h4 className="text-sm font-semibold text-gray-600">Atendimentos</h4>
            {atendimentos?.length ? (
              atendimentos.map((a: AtendimentoRespostaDTO) => (
                <div key={a.id} className="border rounded p-3 text-sm bg-gray-50">
                  <div className="flex justify-between items-center">
                    <span className="font-medium">
                      {new Date(a.data).toLocaleDateString('pt-BR')}
                    </span>
                    <div className="flex gap-2 text-xs text-gray-500">
                      <span>{TIPO_ATENDIMENTO_LABELS[a.tipo]}</span>
                      <span>·</span>
                      <span>{MODALIDADE_ATENDIMENTO_LABELS[a.modalidade]}</span>
                    </div>
                  </div>
                  {a.pessoaId && (
                    <p className="text-xs text-blue-600 mt-1">
                      Pessoa: {
                        [familia.pessoaReferencia, ...familia.membrosDaFamilia]
                          .find(p => p.id === a.pessoaId)?.nome ?? `ID ${a.pessoaId}`
                      }
                    </p>
                  )}
                  <p className="text-gray-700 mt-1">{a.descricao}</p>
                  <p className="text-xs text-gray-400 mt-1">Técnico: {a.tecnicoNome}</p>
                </div>
              ))
            ) : (
              <p className="text-xs text-gray-400">Nenhum atendimento registrado.</p>
            )}
          </div>
        </div>
      )}
    </div>
  )
}
