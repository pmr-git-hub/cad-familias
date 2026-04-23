// src/components/prontuario/FormAbrirProntuario.tsx

import { useState } from 'react'
import { useCadastrarProntuario } from '../hooks/useProntuarios' 
import { SeletorEquipamento } from './SeletorEquipamento'

interface Props {
  familiaId: number
  onSucesso?: () => void
}

export function FormAbrirProntuario({ familiaId, onSucesso }: Props) {
  const { mutate, isPending } = useCadastrarProntuario()
  const [equipamentoId, setEquipamentoId] = useState<number | null>(null)

  function handleSubmit(e: React.FormEvent) {
    e.preventDefault()
    if (!equipamentoId) return
    mutate({ familiaId, equipamentoId }, { onSuccess: onSucesso })
  }

  return (
    <form onSubmit={handleSubmit} className="flex flex-col gap-4">
      <SeletorEquipamento
        value={equipamentoId}
        onChange={setEquipamentoId}
      />
      <button
        type="submit"
        disabled={isPending || !equipamentoId}
        className="bg-blue-600 text-white rounded px-4 py-2 text-sm hover:bg-blue-700 disabled:opacity-50"
      >
        {isPending ? 'Abrindo...' : 'Abrir Prontuário'}
      </button>
    </form>
  )
}
