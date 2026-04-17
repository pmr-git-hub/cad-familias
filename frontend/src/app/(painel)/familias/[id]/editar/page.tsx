// src/app/painel/[id]/editar/page.tsx

"use client";

import { useParams, useRouter } from "next/navigation";
import { ArrowLeft } from "lucide-react";
import { useFamilia } from "@/modules/familias/hooks/use-familias"; 
import { FamiliaForm } from "@/modules/familias/components/familia-form"; 

export default function EditarFamiliaPage() {
  const router = useRouter();
  const { id } = useParams();
  const { familia, loading } = useFamilia(Number(id));

  if (loading) {
    return (
      <div className="flex justify-center py-20">
        <div className="h-8 w-8 animate-spin rounded-full border-4 border-blue-500 border-t-transparent" />
      </div>
    );
  }

  if (!familia) {
    return (
      <div className="text-center py-20 text-gray-500">
        Família não encontrada.
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center gap-3">
        <button
          onClick={() => router.push("/familias")}
          className="rounded-lg p-2 hover:bg-gray-100 transition"
        >
          <ArrowLeft className="h-5 w-5 text-gray-600" />
        </button>
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Editar Família</h1>
          <p className="text-sm text-muted-foreground mt-1">
            {familia.pessoaReferencia?.nome} — #{familia.id}
          </p>
        </div>
      </div>

      <FamiliaForm familiaInicial={familia} />
    </div>
  );
}
