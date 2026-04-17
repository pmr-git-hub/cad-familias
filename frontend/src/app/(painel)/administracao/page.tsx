// app/(painel)/administracao/page.tsx

import Link from "next/link";
import { Building2, Users, BookOpen, Shield, Shapes } from "lucide-react";
import { Breadcrumb } from "@/components/ui/breadcrumb";

const adminModules = [
  {
    label: "Equipamentos",
    description: "CRAS, CREAS, SCFV e outros equipamentos da rede",
    href: "/administracao/equipamentos",
    icon: Building2,
    color: "bg-blue-50 text-blue-600",
  },
  {
    label: "Técnicos",
    description: "Assistentes sociais, psicólogos e vínculos",
    href: "/administracao/tecnicos",
    icon: Users,
    color: "bg-green-50 text-green-600",
  },
  {
    label: "Programas Sociais",
    description: "Bolsa Família, BPC e outros programas",
    href: "/administracao/programas",
    icon: BookOpen,
    color: "bg-purple-50 text-purple-600",
  },
  {
    label: "Serviços",
    description: "SCFV, grupos, oficinas e atividades dos equipamentos",
    href: "/administracao/servicos",
    icon: Shapes,
    color: "bg-indigo-50 text-indigo-600",
  },
  {
    label: "Usuários",
    description: "Contas de acesso e permissões",
    href: "/administracao/usuarios",
    icon: Shield,
    color: "bg-amber-50 text-amber-600",
  },
];

export default function AdministracaoPage() {
  return (
    <div className="space-y-6">
      <Breadcrumb items={[{ label: "Administração" }]} />
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Administração</h1>
        <p className="text-sm text-muted-foreground mt-1">
          Gerencie os cadastros e configurações do sistema
        </p>
      </div>

      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        {adminModules.map((mod) => (
          <Link
            key={mod.href}
            href={mod.href}
            className="group rounded-xl border border-gray-200 bg-white p-6 shadow-sm hover:shadow-md hover:border-gray-300 transition-all"
          >
            <div className={`inline-flex rounded-lg p-3 ${mod.color}`}>
              <mod.icon className="h-6 w-6" />
            </div>
            <h3 className="mt-4 text-base font-semibold text-gray-900 group-hover:text-blue-700 transition-colors">
              {mod.label}
            </h3>
            <p className="mt-1 text-sm text-muted-foreground">
              {mod.description}
            </p>
          </Link>
        ))}
      </div>
    </div>
  );
}
