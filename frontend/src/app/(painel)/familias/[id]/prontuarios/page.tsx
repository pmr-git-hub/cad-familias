// src/app/(painel)/familias/[id]/prontuarios/page.tsx
"use client"

import { useParams } from "next/navigation"
import { useQuery } from "@tanstack/react-query"
import { familiaService } from "@/modules/familias/services/familia-service"
import { ProntuariosPage } from "../../ProntuariosPage"
 
export default function ProntuariosRoutePage() {
  const { id } = useParams<{ id: string }>()

  const { data: familia, isLoading } = useQuery({
    queryKey: ["familia", Number(id)],
    queryFn: () => familiaService.buscarPorId(Number(id)),
    enabled: !!id,
  })

  if (isLoading || !familia) return <p className="text-sm text-gray-400">Carregando...</p>

  return <ProntuariosPage familiaId={Number(id)} familia={familia} />
}
