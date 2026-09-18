// src/modules/atendimentos/components/SeletorServico.tsx

import { useServicosDoEquipamento } from '../hooks/useServicosDoEquipamento'

interface Props {
  equipamentoId: number
  value: number | null
  onChange: (id: number | null) => void
}

export function SeletorServico({ equipamentoId, value, onChange }: Props) {
  const { data: servicos, isLoading } = useServicosDoEquipamento(equipamentoId)

  if (isLoading) {
    return <p className="text-sm text-gray-500">Carregando serviços...</p>
  }

  if (!servicos?.length) {
    return (
      <p className="text-sm text-red-500">
        Nenhum serviço ativo cadastrado neste equipamento.
      </p>
    )
  }

  return (
    <div className="flex flex-col gap-1">
      <label className="text-sm font-medium text-gray-700">Serviço</label>
      <select
        value={value ?? ''}
        onChange={e => onChange(e.target.value ? Number(e.target.value) : null)}
        required
        className="border rounded px-3 py-2 text-sm"
      >
        <option value="">Selecione o serviço</option>
        {servicos.map(s => (
          <option key={s.id} value={s.id}>{s.nome}</option>
        ))}
      </select>
    </div>
  )
}
