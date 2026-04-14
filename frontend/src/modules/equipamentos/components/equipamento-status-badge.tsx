import { cn } from "@/lib/utils";

interface Props {
  ativo: boolean;
}

export function EquipamentoStatusBadge({ ativo }: Props) {
  return (
    <span
      className={cn(
        "inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-medium",
        ativo
          ? "bg-green-50 text-green-700 ring-1 ring-green-600/20"
          : "bg-red-50 text-red-700 ring-1 ring-red-600/20"
      )}
    >
      {ativo ? "Ativo" : "Inativo"}
    </span>
  );
}
