"use client";

import { useMemo, useState, useCallback } from "react";
import { TipoEquipamento } from "../types/equipamento"; 
import type {
  Equipamento,
  EquipamentoCadastroDTO,
  EquipamentoAtualizacaoDTO,
} from "../types/equipamento"; 

interface FormData {
  nome: string;
  tipo: TipoEquipamento;
  cep: string;
  logradouro: string;
  numero: string;
  complemento: string;
  bairro: string;
  cidade: string;
  estado: string;
  telefone: string;
  email: string;
}

function criarFormInicial(equipamento?: Equipamento | null): FormData {
  if (equipamento) {
    return {
      nome: equipamento.nome,
      tipo: equipamento.tipo,
      cep: equipamento.cep ?? "",
      logradouro: equipamento.logradouro ?? "",
      numero: equipamento.numero ?? "",
      complemento: equipamento.complemento ?? "",
      bairro: equipamento.bairro ?? "",
      cidade: equipamento.cidade ?? "",
      estado: equipamento.estado ?? "",
      telefone: equipamento.telefone ?? "",
      email: equipamento.email ?? "",
    };
  }

  return {
    nome: "",
    tipo: TipoEquipamento.CRAS,
    cep: "",
    logradouro: "",
    numero: "",
    complemento: "",
    bairro: "",
    cidade: "",
    estado: "",
    telefone: "",
    email: "",
  };
}

interface UseEquipamentoFormParams {
  equipamento?: Equipamento | null;
  open: boolean;
  onSubmit: (data: EquipamentoCadastroDTO | EquipamentoAtualizacaoDTO) => void;
}

export function useEquipamentoForm({
  equipamento,
  open,
}: UseEquipamentoFormParams) {
  const isEdicao = !!equipamento;

  // Chave que identifica o "contexto" do form
  const formKey = `${equipamento?.id ?? "novo"}-${open}`;
  const [prevFormKey, setPrevFormKey] = useState(formKey);

  const formInicial = useMemo(
    () => criarFormInicial(equipamento),
    [equipamento]
  );

  const [form, setForm] = useState<FormData>(formInicial);

  // Reset síncrono sem useEffect (padrão recomendado pelo React)
  // https://react.dev/learn/you-might-not-need-an-effect#adjusting-some-state-when-a-prop-changes
  if (formKey !== prevFormKey) {
    setPrevFormKey(formKey);
    setForm(criarFormInicial(equipamento));
  }

  const updateField = useCallback(
    <K extends keyof FormData>(field: K, value: FormData[K]) => {
      setForm((prev) => ({ ...prev, [field]: value }));
    },
    []
  );

  return {
    form,
    isEdicao,
    updateField,
    setForm,
  };
}
