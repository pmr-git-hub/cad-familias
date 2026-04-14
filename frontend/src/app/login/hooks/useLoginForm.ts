"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { authService } from "@/shared/services/auth-service";
import { ApiError } from "@/lib/api";

interface FormState {
  username: string;
  password: string;
  remember: boolean;
}

interface FormErrors {
  username?: string;
  password?: string;
}

export function useLoginForm() {
  const router = useRouter();
  const [showPassword, setShowPassword] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [loginError, setLoginError] = useState<string | null>(null);
  const [form, setForm] = useState<FormState>({
    username: "",
    password: "",
    remember: false,
  });
  const [errors, setErrors] = useState<FormErrors>({});

  function handleUsernameChange(e: React.ChangeEvent<HTMLInputElement>) {
    const value = e.target.value;
    setForm((prev) => ({ ...prev, username: value }));
    if (errors.username) setErrors((prev) => ({ ...prev, username: undefined }));
  }

  function handlePasswordChange(e: React.ChangeEvent<HTMLInputElement>) {
    setForm((prev) => ({ ...prev, password: e.target.value }));
    if (errors.password) setErrors((prev) => ({ ...prev, password: undefined }));
  }

  function handleRememberChange(checked: boolean) {
    setForm((prev) => ({ ...prev, remember: checked }));
  }

  function toggleShowPassword() {
    setShowPassword((prev) => !prev);
  }

  function validate(): boolean {
    const newErrors: FormErrors = {};

    if (!form.username.trim()) {
      newErrors.username = "Usuário é obrigatório";
    } else if (form.username.trim().length < 3) {
      newErrors.username = "Usuário deve ter pelo menos 3 caracteres";
    }

    if (!form.password) {
      newErrors.password = "Senha é obrigatória";
    } 
    // else if (form.password.length < 6) {
    //   newErrors.password = "Senha deve ter pelo menos 6 caracteres";
    // }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    if (!validate()) return;

    setIsLoading(true);
    setLoginError(null);

    try {
      await authService.login(form.username.trim(), form.password, form.remember);
      router.push("/");
    } catch (error) {
      if (error instanceof ApiError) {
        if (error.status === 401 || error.status === 403) {
          setLoginError("Usuário ou senha inválidos.");
        } else {
          setLoginError("Erro no servidor. Tente novamente.");
        }
      } else {
        setLoginError("Não foi possível conectar ao servidor.");
      }
    } finally {
      setIsLoading(false);
    }
  }

  return {
    form,
    errors,
    loginError,
    isLoading,
    showPassword,
    handleUsernameChange,
    handlePasswordChange,
    handleRememberChange,
    toggleShowPassword,
    handleSubmit,
  };
}
