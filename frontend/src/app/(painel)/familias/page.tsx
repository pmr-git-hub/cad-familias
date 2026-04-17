// src/app/familias/page.tsx

"use client";

import { useRouter } from "next/navigation";
import { Plus, Search } from "lucide-react";
import { useFamilias } from "@/modules/familias/hooks/use-familias"; 
import { FamiliaTable } from "@/modules/familias/components/familia-table"; 
import { useState } from "react";

export default function FamiliasPage() {
  const router = useRouter();
  const { familias, loading } = useFamilias();
  const [busca, setBusca] = useState("");

  const familiasFiltradas = familias.filter((f) => {
    const termo = busca.toLowerCase();
    return (
      f.pessoaReferencia?.nome?.toLowerCase().includes(termo) ||
      f.pessoaReferencia?.cpf?.includes(termo) ||
      f.codigoCadunico?.toLowerCase().includes(termo)
    );
  });

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Famílias</h1>
          <p className="text-sm text-muted-foreground mt-1">
            Gerencie as famílias cadastradas no sistema
          </p>
        </div>
        <button
          onClick={() => router.push("/familias/nova")}
          className="inline-flex items-center gap-2 rounded-lg bg-blue-600 px-4 py-2.5 text-sm font-medium text-white shadow hover:bg-blue-700 transition"
        >
          <Plus className="h-4 w-4" />
          Nova Família
        </button>
      </div>

      {/* Busca */}
      <div className="relative max-w-md">
        <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-gray-400" />
        <input
          placeholder="Buscar por nome, CPF ou CadÚnico..."
          value={busca}
          onChange={(e) => setBusca(e.target.value)}
          className="w-full rounded-lg border border-gray-300 py-2 pl-10 pr-4 text-sm focus:border-blue-500 focus:ring-1 focus:ring-blue-500 outline-none"
        />
      </div>

      {/* Tabela */}
      <FamiliaTable familias={familiasFiltradas} loading={loading} />
    </div>
  );
}
