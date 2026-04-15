"use client";

import Image from "next/image";
import { Eye, EyeOff, LogIn, Loader2, User } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Checkbox } from "@/components/ui/checkbox";
import { useLoginForm } from "../hooks/useLoginForm";
import Link from "next/link";

export function LoginForm() {
  const {
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
  } = useLoginForm();

  return (
    <div className="flex w-full items-center justify-center px-6 py-12 lg:w-[45%] xl:w-[40%] bg-white">
      <div className="w-full max-w-md space-y-8">
        {/* Logo mobile */}
        <div className="flex flex-col items-center lg:hidden">
          <Image
            src="/images/logo-cad-horizontal.png"
            alt="Logo CAD Ribeirão"
            width={250}
            height={80}
            style={{ width: 'auto', height: 'auto' }}
            priority
          />
          <div className="mt-3 h-1 w-16 rounded-full bg-brand-yellow" />
        </div>

        {/* Header */}
        <div className="text-center lg:text-left">
          <h2 className="text-2xl font-bold tracking-tight text-brand-dark-blue">
            Bem-vindo de volta
          </h2>
          <p className="mt-2 text-sm text-muted-foreground">
            Entre com suas credenciais para acessar o sistema
          </p>
        </div>

        {/* Form */}
        <form onSubmit={handleSubmit} className="space-y-5">
          {/* Campo Usuário */}
          <div className="space-y-2">
            <Label htmlFor="username" className="text-brand-dark-blue">
              Usuário
            </Label>
            <div className="relative">
              <Input
                id="username"
                type="text"
                placeholder="Digite seu usuário"
                value={form.username}
                onChange={handleUsernameChange}
                className={`h-11 pl-10 ${
                  errors.username
                    ? "border-red-500 focus-visible:ring-red-500"
                    : "focus-visible:ring-brand-medium-blue"
                }`}
                disabled={isLoading}
                autoComplete="username"
                autoFocus
              />
              <User className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
            </div>
            {errors.username && (
              <p className="text-xs text-red-500">{errors.username}</p>
            )}
          </div>

          {/* Campo Senha */}
          <div className="space-y-2">
            <Label htmlFor="password" className="text-brand-dark-blue">
              Senha
            </Label>
            <div className="relative">
              <Input
                id="password"
                type={showPassword ? "text" : "password"}
                placeholder="Digite sua senha"
                value={form.password}
                onChange={handlePasswordChange}
                className={`h-11 pr-10 ${
                  errors.password
                    ? "border-red-500 focus-visible:ring-red-500"
                    : "focus-visible:ring-brand-medium-blue"
                }`}
                disabled={isLoading}
                autoComplete="current-password"
              />
              <button
                type="button"
                onClick={toggleShowPassword}
                className="absolute right-3 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-brand-dark-blue transition-colors cursor-pointer"
                tabIndex={-1}
              >
                {showPassword ? (
                  <EyeOff className="h-4 w-4" />
                ) : (
                  <Eye className="h-4 w-4" />
                )}
              </button>
            </div>
            {errors.password && (
              <p className="text-xs text-red-500">{errors.password}</p>
            )}
          </div>

          {/* Lembrar + Esqueci */}
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-2">
              <Checkbox
                id="remember"
                checked={form.remember}
                onCheckedChange={(checked) =>
                  handleRememberChange(checked as boolean)
                }
                disabled={isLoading}
              />
              <Label
                htmlFor="remember"
                className="text-sm font-normal text-muted-foreground cursor-pointer"
              >
                Lembrar de mim
              </Label>
            </div>

            <Link
                href="/login/esqueci-senha"
                className="text-sm font-medium text-blue-600 hover:text-blue-800"
                >
                Esqueceu a senha?
            </Link>

          </div>

          {/* Erro do login */}
          {loginError && (
            <div className="rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
              {loginError}
            </div>
          )}

          {/* Botão */}
          <Button
            type="submit"
            className="h-11 w-full bg-brand-dark-blue text-white hover:bg-brand-medium-blue transition-all duration-300 text-base font-semibold"
            disabled={isLoading}
          >
            {isLoading ? (
              <>
                <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                Entrando...
              </>
            ) : (
              <>
                <LogIn className="mr-2 h-4 w-4" />
                Entrar no Sistema
              </>
            )}
          </Button>
        </form>

        {/* Divisor */}
        <div className="relative">
          <div className="absolute inset-0 flex items-center">
            <span className="w-full border-t" />
          </div>
          <div className="relative flex justify-center text-xs uppercase">
            <span className="bg-white px-2 text-muted-foreground">
              CAD Ribeirão
            </span>
          </div>
        </div>

        {/* Footer */}
        <div className="text-center space-y-2">
          <p className="text-xs text-muted-foreground">
            Secretaria de Assistência Social
          </p>
          <p className="text-xs text-muted-foreground/60">
            Prefeitura Municipal de Ribeirão · {new Date().getFullYear()}
          </p>
        </div>
      </div>
    </div>
  );
}
