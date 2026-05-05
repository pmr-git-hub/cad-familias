// src/modules/atendimentos/components/FormAtendimento.tsx
'use client'

import { useState } from 'react'
import { useCadastrarAtendimento } from '../hooks/useAtendimentos'
import { SeletorPessoa } from './SeletorPessoa'
import {
  TIPO_ATENDIMENTO_LABELS,
  MODALIDADE_ATENDIMENTO_LABELS,
  type TipoAtendimento,
  type ModalidadeAtendimento,
} from '../types/enums'
import type { FamiliaDTO } from '@/modules/familias/types/familia'

interface Props {
  prontuarioId: number
  familia: FamiliaDTO
  onSucesso?: () => void
}

export function FormAtendimento({ prontuarioId, familia, onSucesso }: Props) {
  const { mutate, isPending } = useCadastrarAtendimento(prontuarioId)

  const [pessoaId, setPessoaId]     = useState<number | null>(null)
  const [data, setData]             = useState('')
  const [tipo, setTipo]             = useState<TipoAtendimento>('ATENDIMENTO_PRESENCIAL')
  const [modalidade, setModalidade] = useState<ModalidadeAtendimento>('INDIVIDUAL')
  const [descricao, setDescricao]   = useState('')

  function handleSubmit(e: React.FormEvent) {
    e.preventDefault()
    mutate(
      {
        prontuarioId,
        pessoaId,
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
            ([val, label]) => (
              <option key={val} value={val}>{label}</option>
            )
          )}
        </select>
      </div>

      <div className="flex flex-col gap-1">
        <label className="text-sm font-medium text-gray-700">Modalidade</label>
        <select
          value={modalidade}
          onChange={e => setModalidade(e.target.value as ModalidadeAtendimento)}
          className="border rounded px-3 py-2 text-sm"
        >
          {(Object.entries(MODALIDADE_ATENDIMENTO_LABELS) as [ModalidadeAtendimento, string][]).map(
            ([val, label]) => (
              <option key={val} value={val}>{label}</option>
            )
          )}
        </select>
      </div>

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
        disabled={isPending}
        className="bg-blue-600 text-white rounded px-4 py-2 text-sm hover:bg-blue-700 disabled:opacity-50"
      >
        {isPending ? 'Salvando...' : 'Registrar Atendimento'}
      </button>

    </form>
  )
}
