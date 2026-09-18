// src/modules/atendimentos/components/FormAtendimento.tsx
'use client'

import { useState } from 'react'
import { useCadastrarAtendimento } from '../hooks/useAtendimentos'
import { SeletorPessoa } from './SeletorPessoa'
import { SeletorServico } from './SeletorServico'
import {
  TIPO_ATENDIMENTO_LABELS,
  MODALIDADE_ATENDIMENTO_LABELS,
  type TipoAtendimento,
  type ModalidadeAtendimento,
} from '../types/enums'
import type { FamiliaDTO } from '@/modules/familias/types/familia'
import type { ProntuarioRespostaDTO } from '../types/prontuario'

interface Props {
  prontuario: ProntuarioRespostaDTO
  familia: FamiliaDTO
  onSucesso?: () => void
}


export function FormAtendimento({ prontuario, familia, onSucesso }: Props) {
  const { mutate, isPending } = useCadastrarAtendimento(prontuario.id)

  const [pessoaId, setPessoaId]     = useState<number | null>(null)
  const [servicoId, setServicoId]   = useState<number | null>(null)
  const [data, setData]             = useState('')
  const [tipo, setTipo]             = useState<TipoAtendimento>('ATENDIMENTO_PRESENCIAL')
  const [modalidade, setModalidade] = useState<ModalidadeAtendimento>('INDIVIDUAL')
  const [descricao, setDescricao]   = useState('')

  const exigeServico = modalidade === 'GRUPO'
  function handleSubmit(e: React.FormEvent) {
    e.preventDefault()
    mutate(
      {
        prontuarioId: prontuario.id,
        pessoaId,
        servicoId: exigeServico ? servicoId : null,
        programaId: null,
        data: new Date(data),
        tipo,
        modalidade,
        descricao,
      },
      { onSuccess: onSucesso }
    )
  }

  return (
    <form onSubmit={handleSubmit} className="flex flex-col gap-4">

      <SeletorPessoa familia={familia} value={pessoaId} onChange={setPessoaId} />

      <div className="flex flex-col gap-1">
        <label className="text-sm font-medium text-gray-700">Data</label>
        <input
          type="date"
          value={data}
          onChange={e => setData(e.target.value)}
          required
          className="border rounded px-3 py-2 text-sm"
        />
      </div>

      <div className="flex flex-col gap-1">
        <label className="text-sm font-medium text-gray-700">Tipo</label>
        <select
          value={tipo}
          onChange={e => setTipo(e.target.value as TipoAtendimento)}
          className="border rounded px-3 py-2 text-sm"
        >
          {(Object.entries(TIPO_ATENDIMENTO_LABELS) as [TipoAtendimento, string][]).map(
            ([val, label]) => <option key={val} value={val}>{label}</option>
          )}
        </select>
      </div>

      <div className="flex flex-col gap-1">
        <label className="text-sm font-medium text-gray-700">Modalidade</label>
        <select
          value={modalidade}
          onChange={e => {
            const novaModalidade = e.target.value as ModalidadeAtendimento
            setModalidade(novaModalidade)
            if (novaModalidade !== 'GRUPO') setServicoId(null)
          }}
          className="border rounded px-3 py-2 text-sm"
        >
          {(Object.entries(MODALIDADE_ATENDIMENTO_LABELS) as [ModalidadeAtendimento, string][]).map(
            ([val, label]) => <option key={val} value={val}>{label}</option>
          )}
        </select>
      </div>

      {exigeServico && (
        <SeletorServico
          equipamentoId={prontuario.equipamentoId}
          value={servicoId}
          onChange={setServicoId}
        />
      )}

      <div className="flex flex-col gap-1">
        <label className="text-sm font-medium text-gray-700">Descrição</label>
        <textarea
          value={descricao}
          onChange={e => setDescricao(e.target.value)}
          required
          rows={4}
          placeholder="Descreva o atendimento realizado..."
          className="border rounded px-3 py-2 text-sm resize-none"
        />
      </div>

      <button
        type="submit"
        disabled={isPending || (exigeServico && !servicoId)}
        className="bg-blue-600 text-white rounded px-4 py-2 text-sm hover:bg-blue-700 disabled:opacity-50"
      >
        {isPending ? 'Salvando...' : 'Registrar Atendimento'}
      </button>

    </form>
  )
}
