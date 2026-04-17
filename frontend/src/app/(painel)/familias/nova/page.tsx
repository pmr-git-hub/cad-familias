// src/app/familias/nova/page.tsx

"use client";

import { ArrowLeft } from "lucide-react";
import { useRouter } from "next/navigation";
import { FamiliaForm } from "@/modules/familias/components/familia-form"; 

export default function NovaFamiliaPage() {
  const router = useRouter();

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
          <h1 className="text-2xl font-bold text-gray-900">Nova Família</h1>
          <p className="text-sm text-muted-foreground mt-1">
            Preencha os dados para cadastrar uma nova família
          </p>
        </div>
      </div>

      <FamiliaForm />
    </div>
  );
}
