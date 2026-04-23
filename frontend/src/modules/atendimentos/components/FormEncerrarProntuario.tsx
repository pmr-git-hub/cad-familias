// src/components/prontuario/FormEncerrarProntuario.tsx

import { useState } from 'react'
import { useEncerrarProntuario } from '../hooks/useProntuarios' 

interface Props {
  prontuarioId: number
  familiaId: number
  onSucesso?: () => void
}

export function FormEncerrarProntuario({ prontuarioId, familiaId, onSucesso }: Props) {
  const { mutate, isPending } = useEncerrarProntuario(familiaId)
  const [data, setData] = useState('')
  const [motivo, setMotivo] = useState('')

  function handleSubmit(e: React.FormEvent) {
    e.preventDefault()
    mutate(
      { id: prontuarioId, dto: { dataFechamento: data, motivoEncerramento: motivo || undefined } },
      { onSuccess: onSucesso }
    )
  }

  return (
    <form onSubmit={handleSubmit} className="flex flex-col gap-4">
      <div className="flex flex-col gap-1">
        <label className="text-sm font-medium text-gray-700">Data de encerramento</label>
        <input
          type="date"
          value={data}
          onChange={e => setData(e.target.value)}
          required
          className="border rounded px-3 py-2 text-sm"
        />
      </div>
      <div className="flex flex-col gap-1">
        <label className="text-sm font-medium text-gray-700">Motivo (opcional)</label>
        <textarea
          value={motivo}
          onChange={e => setMotivo(e.target.value)}
          rows={3}
          className="border rounded px-3 py-2 text-sm resize-none"
        />
      </div>
      <button
        type="submit"
        disabled={isPending}
        className="bg-red-600 text-white rounded px-4 py-2 text-sm hover:bg-red-700 disabled:opacity-50"
      >
        {isPending ? 'Encerrando...' : 'Encerrar Prontuário'}
      </button>
    </form>
  )
}
