// src/modules/atendimentos/components/SeletorPessoa.tsx

import type { FamiliaDTO, PessoaDTO } from '@/modules/familias/types/familia'

interface Props {
  familia: FamiliaDTO
  value: number | null   // null = família toda
  onChange: (id: number | null) => void
}

export function SeletorPessoa({ familia, value, onChange }: Props) {
  const todos: PessoaDTO[] = [
    familia.pessoaReferencia,
    ...familia.membrosDaFamilia,
  ]

  const isFamilia = value === null

  return (
    <div className="flex flex-col gap-2">
      <label className="text-sm font-medium text-gray-700">Atendimento para</label>

      <div className="flex gap-4 text-sm">
        <label className="flex items-center gap-2 cursor-pointer">
          <input
            type="radio"
            checked={isFamilia}
            onChange={() => onChange(null)}
          />
          Família toda
        </label>
        <label className="flex items-center gap-2 cursor-pointer">
          <input
            type="radio"
            checked={!isFamilia}
            onChange={() => {
              const primeiro = todos[0]?.id
              if (primeiro != null) onChange(primeiro)
            }}
          />
          Pessoa específica
        </label>
      </div>

      {!isFamilia && (
        <select
          value={value ?? ''}
          onChange={e => onChange(Number(e.target.value))}
          className="border rounded px-3 py-2 text-sm"
        >
          {todos.map(p => (
            <option key={p.id} value={p.id}>
              {p.nome}
            </option>
          ))}
        </select>
      )}
    </div>
  )
}
