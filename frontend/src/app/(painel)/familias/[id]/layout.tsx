// src/app/(painel)/familias/[id]/layout.tsx
"use client"

import { useParams, usePathname, useRouter } from "next/navigation"
import { useQuery } from "@tanstack/react-query"
import { familiaService } from "@/modules/familias/services/familia-service"
import { ArrowLeft } from "lucide-react"

const ABAS = [
  { label: "Cadastro",     href: "" },
  { label: "Prontuários",  href: "/prontuarios" },
]

export default function FamiliaLayout({ children }: { children: React.ReactNode }) {
  const { id } = useParams<{ id: string }>()
  const pathname = usePathname()
  const router = useRouter()

  const { data: familia } = useQuery({
    queryKey: ["familia", Number(id)],
    queryFn: () => familiaService.buscarPorId(Number(id)),
    enabled: !!id,
  })

  const base = `/familias/${id}`

  return (
    <div className="space-y-0">
      {/* Voltar */}
      <button
        onClick={() => router.push("/familias")}
        className="flex items-center gap-1 text-sm text-gray-500 hover:text-gray-700 mb-4"
      >
        <ArrowLeft className="h-4 w-4" />
        Voltar para famílias
      </button>

      {/* Header */}
      <div className="mb-4">
        <h1 className="text-2xl font-bold text-gray-900">
          {familia?.pessoaReferencia?.nome ?? "Carregando..."}
        </h1>
        <p className="text-sm text-gray-500">CPF: {familia?.pessoaReferencia?.cpf}</p>
      </div>

      {/* Abas */}
      <div className="flex border-b border-gray-200 mb-6">
        {ABAS.map((aba) => {
          const href = `${base}${aba.href}`
          const ativo = aba.href === ""
            ? pathname === base
            : pathname.startsWith(href)

          return (
            <button
              key={aba.href}
              onClick={() => router.push(href)}
              className={`px-5 py-2.5 text-sm font-medium border-b-2 transition -mb-px ${
                ativo
                  ? "border-blue-600 text-blue-600"
                  : "border-transparent text-gray-500 hover:text-gray-700"
              }`}
            >
              {aba.label}
            </button>
          )
        })}
      </div>

      {children}
    </div>
  )
}
