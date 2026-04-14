import {
  Users,
  ClipboardList,
  ArrowRightLeft,
  AlertTriangle,
} from "lucide-react";

const stats = [
  {
    label: "Famílias Ativas",
    value: "—",
    icon: Users,
    color: "bg-blue-50 text-blue-600",
  },
  {
    label: "Atendimentos (mês)",
    value: "—",
    icon: ClipboardList,
    color: "bg-green-50 text-green-600",
  },
  {
    label: "Encaminhamentos",
    value: "—",
    icon: ArrowRightLeft,
    color: "bg-purple-50 text-purple-600",
  },
  {
    label: "Pendências",
    value: "—",
    icon: AlertTriangle,
    color: "bg-amber-50 text-amber-600",
  },
];

export default function DashboardPage() {
  return (
    <div className="space-y-6">
      {/* Saudação */}
      <div>
        <h1 className="text-2xl font-bold text-gray-900">
          Bem-vindo ao CAD Ribeirão
        </h1>
        <p className="text-sm text-muted-foreground mt-1">
          Painel de acompanhamento da assistência social
        </p>
      </div>

      {/* Cards de resumo */}
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        {stats.map((stat) => (
          <div
            key={stat.label}
            className="rounded-xl border border-gray-200 bg-white p-5 shadow-sm"
          >
            <div className="flex items-center justify-between">
              <p className="text-sm font-medium text-muted-foreground">
                {stat.label}
              </p>
              <div className={`rounded-lg p-2 ${stat.color}`}>
                <stat.icon className="h-4 w-4" />
              </div>
            </div>
            <p className="mt-3 text-3xl font-bold text-gray-900">
              {stat.value}
            </p>
          </div>
        ))}
      </div>

      {/* Placeholder de conteúdo futuro */}
      <div className="grid gap-4 lg:grid-cols-2">
        <div className="rounded-xl border border-gray-200 bg-white p-6 shadow-sm">
          <h2 className="text-lg font-semibold text-gray-900">
            Últimos Atendimentos
          </h2>
          <div className="mt-4 flex items-center justify-center h-48 text-sm text-muted-foreground">
            Os atendimentos recentes aparecerão aqui
          </div>
        </div>

        <div className="rounded-xl border border-gray-200 bg-white p-6 shadow-sm">
          <h2 className="text-lg font-semibold text-gray-900">
            Alertas e Pendências
          </h2>
          <div className="mt-4 flex items-center justify-center h-48 text-sm text-muted-foreground">
            As pendências do sistema aparecerão aqui
          </div>
        </div>
      </div>
    </div>
  );
}
