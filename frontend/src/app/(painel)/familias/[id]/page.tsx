// src/app/(painel)/familias/[id]/page.tsx
"use client"

import { useParams } from "next/navigation"
import { useQuery } from "@tanstack/react-query"
import { familiaService } from "@/modules/familias/services/familia-service"
import { SITUACAO_LABELS, SITUACAO_COLORS, SEXO_LABELS, PARENTESCO_LABELS } from "@/modules/familias/types/familia"

export default function FamiliaDetalhePage() {
  const { id } = useParams<{ id: string }>()

  const { data: familia, isLoading } = useQuery({
    queryKey: ["familia", Number(id)],
    queryFn: () => familiaService.buscarPorId(Number(id)),
    enabled: !!id,
  })

  if (isLoading || !familia) return <p className="text-sm text-gray-400">Carregando...</p>

  const { pessoaReferencia, membrosDaFamilia, situacao, codigoCadunico, rendaFamiliar } = familia

  return (
    <div className="space-y-6 max-w-3xl">

      {/* Situação */}
      <div className="flex items-center gap-3">
        <span className={`text-xs px-2 py-1 rounded-full font-medium ${SITUACAO_COLORS[situacao]}`}>
          {SITUACAO_LABELS[situacao]}
        </span>
        {codigoCadunico && (
          <span className="text-xs text-gray-500">CadÚnico: {codigoCadunico}</span>
        )}
        {rendaFamiliar !== undefined && (
          <span className="text-xs text-gray-500">
            Renda familiar: R$ {rendaFamiliar.toFixed(2)}
          </span>
        )}
      </div>

      {/* Pessoa de referência */}
      <section className="border rounded-lg p-4 space-y-2">
        <h2 className="font-semibold text-gray-700">Pessoa de Referência</h2>
        <PessoaInfo pessoa={pessoaReferencia} />
      </section>

      {/* Membros */}
      {membrosDaFamilia.length > 0 && (
        <section className="border rounded-lg p-4 space-y-4">
          <h2 className="font-semibold text-gray-700">Membros da Família</h2>
          {membrosDaFamilia.map((m, i) => (
            <div key={m.id ?? i} className="border-t pt-3 first:border-t-0 first:pt-0">
              <PessoaInfo pessoa={m} />
            </div>
          ))}
        </section>
      )}
    </div>
  )
}

function PessoaInfo({ pessoa }: { pessoa: import("@/modules/familias/types/familia").PessoaDTO }) {
  return (
    <div className="grid grid-cols-2 gap-x-6 gap-y-1 text-sm text-gray-600">
      <Campo label="Nome"       valor={pessoa.nome} />
      <Campo label="CPF"        valor={pessoa.cpf} />
      <Campo label="Sexo"       valor={SEXO_LABELS[pessoa.sexo]} />
      <Campo label="Parentesco" valor={PARENTESCO_LABELS[pessoa.parentesco]} />
      <Campo label="Nascimento" valor={new Date(pessoa.dataNascimento).toLocaleDateString("pt-BR")} />
      {pessoa.telefone && <Campo label="Telefone" valor={pessoa.telefone} />}
      {pessoa.rendaMensal !== undefined && (
        <Campo label="Renda" valor={`R$ ${pessoa.rendaMensal.toFixed(2)}`} />
      )}
    </div>
  )
}

function Campo({ label, valor }: { label: string; valor: string }) {
  return (
    <div>
      <span className="text-xs text-gray-400">{label}</span>
      <p className="font-medium text-gray-800">{valor}</p>
    </div>
  )
}
