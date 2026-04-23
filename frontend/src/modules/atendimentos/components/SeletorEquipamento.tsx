// src/modules/atendimentos/components/SeletorEquipamento.tsx

import { useEquipamentosDoTecnico } from "../hooks/useEquipamentosDoTecnico"

interface Props {
  value: number | null
  onChange: (id: number) => void
  error?: string
}

export function SeletorEquipamento({ value, onChange, error }: Props) {
  const { data: equipamentos, isLoading } = useEquipamentosDoTecnico()

  // Auto-seleciona se só tiver 1
  if (!isLoading && equipamentos?.length === 1 && value !== equipamentos[0].id) {
    onChange(equipamentos[0].id)
  }

  if (isLoading) return <p className="text-sm text-gray-500">Carregando equipamentos...</p>

  if (!equipamentos?.length)
    return <p className="text-sm text-red-500">Nenhum equipamento vinculado ao seu usuário.</p>

  if (equipamentos.length === 1) {
    return (
      <div className="text-sm text-gray-700">
        <span className="font-medium">Equipamento:</span> {equipamentos[0].nome}
      </div>
    )
  }

  return (
    <div className="flex flex-col gap-1">
      <label className="text-sm font-medium text-gray-700">Equipamento</label>
      <select
        value={value ?? ''}
        onChange={e => onChange(Number(e.target.value))}
        className="border rounded px-3 py-2 text-sm"
      >
        <option value="">Selecione o equipamento</option>
        {equipamentos.map(eq => (
          <option key={eq.id} value={eq.id}>{eq.nome}</option>
        ))}
      </select>
      {error && <span className="text-xs text-red-500">{error}</span>}
    </div>
  )
}
