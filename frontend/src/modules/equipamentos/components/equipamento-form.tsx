"use client";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { X } from "lucide-react";
import { TipoEquipamento } from "../types/equipamento";
import type {
  Equipamento,
  EquipamentoCadastroDTO,
  EquipamentoAtualizacaoDTO,
} from "../types/equipamento";
import { useEquipamentoForm } from "../hooks/use-equipamento-form"; 

const tipoOptions: { value: TipoEquipamento; label: string }[] = [
  { value: TipoEquipamento.CRAS, label: "CRAS" },
  { value: TipoEquipamento.CREAS, label: "CREAS" },
  { value: TipoEquipamento.SCFV, label: "SCFV" },
  { value: TipoEquipamento.ACOLHIMENTO, label: "Acolhimento" },
  { value: TipoEquipamento.OUTRO, label: "Outro" },
];

const estadosBR = [
  "AC","AL","AP","AM","BA","CE","DF","ES","GO","MA","MT","MS",
  "MG","PA","PB","PR","PE","PI","RJ","RN","RS","RO","RR","SC",
  "SP","SE","TO",
];

interface Props {
  equipamento?: Equipamento | null;
  open: boolean;
  onClose: () => void;
  onSubmit: (data: EquipamentoCadastroDTO | EquipamentoAtualizacaoDTO) => void;
  loading?: boolean;
}

export function EquipamentoForm({
  equipamento,
  open,
  onClose,
  onSubmit,
  loading,
}: Props) {
  const { form, isEdicao, updateField } = useEquipamentoForm({
    equipamento,
    open,
    onSubmit,
  });

  function handleFormSubmit(e: React.FormEvent): void {
    e.preventDefault();
    onSubmit({
      nome: form.nome,
      tipo: form.tipo,
      cep: form.cep || undefined,
      logradouro: form.logradouro || undefined,
      numero: form.numero || undefined,
      complemento: form.complemento || undefined,
      bairro: form.bairro || undefined,
      cidade: form.cidade || undefined,
      estado: form.estado || undefined,
      telefone: form.telefone || undefined,
      email: form.email || undefined,
    });
  }

  if (!open) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center">
      <div className="absolute inset-0 bg-black/50" onClick={onClose} />

      <div className="relative z-10 w-full max-w-2xl max-h-[90vh] overflow-y-auto rounded-xl bg-white p-6 shadow-xl">
        {/* Header */}
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-lg font-semibold text-gray-900">
            {isEdicao ? "Editar Equipamento" : "Novo Equipamento"}
          </h2>
          <button
            onClick={onClose}
            className="rounded-lg p-1.5 text-gray-400 hover:bg-gray-100 hover:text-gray-600 transition-colors cursor-pointer"
          >
            <X className="h-5 w-5" />
          </button>
        </div>

        <form onSubmit={handleFormSubmit} className="space-y-6">
          {/* ── Identificação ── */}
          <fieldset className="space-y-4">
            <legend className="text-sm font-semibold text-gray-700 uppercase tracking-wider">
              Identificação
            </legend>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div className="space-y-2">
                <Label htmlFor="nome">Nome *</Label>
                <Input
                  id="nome"
                  placeholder="Ex: CRAS Norte"
                  value={form.nome}
                  onChange={(e) => updateField("nome", e.target.value)}
                  required
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="tipo">Tipo *</Label>
                <select
                  id="tipo"
                  value={form.tipo}
                  onChange={(e) =>
                    updateField("tipo", e.target.value as TipoEquipamento)
                  }
                  className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring"
                  required
                >
                  {tipoOptions.map((opt) => (
                    <option key={opt.value} value={opt.value}>
                      {opt.label}
                    </option>
                  ))}
                </select>
              </div>
            </div>
          </fieldset>

          {/* ── Endereço ── */}
          <fieldset className="space-y-4">
            <legend className="text-sm font-semibold text-gray-700 uppercase tracking-wider">
              Endereço
            </legend>

            <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
              <div className="space-y-2">
                <Label htmlFor="cep">CEP</Label>
                <Input
                  id="cep"
                  placeholder="00000-000"
                  maxLength={9}
                  value={form.cep}
                  onChange={(e) => updateField("cep", e.target.value)}
                />
              </div>
              <div className="sm:col-span-2 space-y-2">
                <Label htmlFor="logradouro">Logradouro</Label>
                <Input
                  id="logradouro"
                  placeholder="Rua, Avenida, Travessa..."
                  value={form.logradouro}
                  onChange={(e) => updateField("logradouro", e.target.value)}
                />
              </div>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
              <div className="space-y-2">
                <Label htmlFor="numero">Número</Label>
                <Input
                  id="numero"
                  placeholder="123 ou S/N"
                  maxLength={20}
                  value={form.numero}
                  onChange={(e) => updateField("numero", e.target.value)}
                />
              </div>
              <div className="sm:col-span-2 space-y-2">
                <Label htmlFor="complemento">Complemento</Label>
                <Input
                  id="complemento"
                  placeholder="Bloco A, Sala 2..."
                  value={form.complemento}
                  onChange={(e) => updateField("complemento", e.target.value)}
                />
              </div>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
              <div className="space-y-2">
                <Label htmlFor="bairro">Bairro</Label>
                <Input
                  id="bairro"
                  placeholder="Nome do bairro"
                  value={form.bairro}
                  onChange={(e) => updateField("bairro", e.target.value)}
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="cidade">Cidade</Label>
                <Input
                  id="cidade"
                  placeholder="Nome da cidade"
                  value={form.cidade}
                  onChange={(e) => updateField("cidade", e.target.value)}
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="estado">Estado</Label>
                <select
                  id="estado"
                  value={form.estado}
                  onChange={(e) => updateField("estado", e.target.value)}
                  className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring"
                >
                  <option value="">Selecione</option>
                  {estadosBR.map((uf) => (
                    <option key={uf} value={uf}>
                      {uf}
                    </option>
                  ))}
                </select>
              </div>
            </div>
          </fieldset>

          {/* ── Contato ── */}
          <fieldset className="space-y-4">
            <legend className="text-sm font-semibold text-gray-700 uppercase tracking-wider">
              Contato
            </legend>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div className="space-y-2">
                <Label htmlFor="telefone">Telefone</Label>
                <Input
                  id="telefone"
                  placeholder="(11) 99999-9999"
                  maxLength={20}
                  value={form.telefone}
                  onChange={(e) => updateField("telefone", e.target.value)}
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="email">E-mail</Label>
                <Input
                  id="email"
                  type="email"
                  placeholder="equipamento@exemplo.com"
                  value={form.email}
                  onChange={(e) => updateField("email", e.target.value)}
                />
              </div>
            </div>
          </fieldset>

          {/* ── Ações ── */}
          <div className="flex justify-end gap-3 pt-4 border-t border-gray-100">
            <Button
              type="button"
              variant="outline"
              onClick={onClose}
              disabled={loading}
            >
              Cancelar
            </Button>
            <Button type="submit" disabled={loading || !form.nome.trim()}>
              {loading
                ? "Salvando..."
                : isEdicao
                  ? "Salvar Alterações"
                  : "Cadastrar"}
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
}
