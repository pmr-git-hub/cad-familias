// src/components/atendimento/FormAtendimento.tsx

import { useState } from 'react'
import { useCadastrarAtendimento } from '../hooks/useAtendimentos' 
import { SeletorPessoa } from './SeletorPessoa'
import type { FamiliaDTO } from '@/modules/familias/types/familia' 
import type { TipoAtendimento, ModalidadeAtendimento } from  './../types/enums'

interface Props {
  prontuarioId: number
  familia: FamiliaDTO
  onSucesso?: () => void
}

export function FormAtendimento({ prontuarioId, familia, onSucesso }: Props) {
  const { mutate, isPending } = useCadastrarAtendimento(prontuarioId)

  const [pessoaId, setPessoaId] = useState<number | null>(null)
  const [data, setData] = useState('')
  const [tipo, setTipo] = useState<TipoAtendimento>('INDIVIDUAL')
  const [modalidade, setModalidade] = useState<ModalidadeAtendimento>('PRESENCIAL')
  const [descricao, setDescricao] = useState('')

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
          <option value="INDIVIDUAL">Individual</option>
          <option value="FAMILIAR">Familiar</option>
          <option value="GRUPO">Grupo</option>
          <option value="VISITA_DOMICILIAR">Visita Domiciliar</option>
        </select>
      </div>

      <div className="flex flex-col gap-1">
        <label className="text-sm font-medium text-gray-700">Modalidade</label>
        <select
          value={modalidade}
          onChange={e => setModalidade(e.target.value as ModalidadeAtendimento)}
          className="border rounded px-3 py-2 text-sm"
        >
          <option value="PRESENCIAL">Presencial</option>
          <option value="REMOTO">Remoto</option>
          <option value="DOMICILIAR">Domiciliar</option>
        </select>
      </div>

      <div className="flex flex-col gap-1">
        <label className="text-sm font-medium text-gray-700">Descrição</label>
        <textarea
          value={descricao}
          onChange={e => setDescricao(e.target.value)}
          required
          rows={4}
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
