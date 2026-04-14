"use client";

import Link from "next/link";
import { KeyRound, ArrowLeft, ShieldAlert } from "lucide-react";

export default function EsqueciSenhaPage() {
  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 px-4">
      <div className="w-full max-w-md space-y-6">
        {/* Header */}
        <div className="text-center space-y-2">
          <div className="mx-auto w-16 h-16 bg-amber-100 rounded-full flex items-center justify-center">
            <KeyRound className="w-8 h-8 text-amber-600" />
          </div>
          <h1 className="text-2xl font-bold text-gray-900">
            Esqueceu sua senha?
          </h1>
          <p className="text-gray-500 text-sm">
            Não se preocupe, vamos te ajudar a recuperar o acesso.
          </p>
        </div>

        {/* Card de instrução */}
        <div className="bg-white rounded-2xl shadow-sm border border-gray-200 p-6 space-y-4">
          <div className="flex gap-3 items-start bg-amber-50 border border-amber-200 rounded-lg p-4">
            <ShieldAlert className="w-5 h-5 text-amber-600 mt-0.5 shrink-0" />
            <div className="text-sm text-amber-800 space-y-2">
              <p className="font-medium">
                A redefinição de senha é feita pelo administrador do sistema.
              </p>
              <p>
                Entre em contato com o administrador e solicite a redefinição.
                Após o reset, você receberá uma senha temporária e deverá
                trocá-la no primeiro acesso.
              </p>
            </div>
          </div>

          {/* Passos */}
          <div className="space-y-3 pt-2">
            <h2 className="text-sm font-semibold text-gray-700">
              Como proceder:
            </h2>
            <ol className="space-y-3 text-sm text-gray-600">
              {[
                "Entre em contato com o administrador do sistema",
                "Informe seu nome de usuário",
                "Aguarde a redefinição da senha",
                "Faça login com a senha temporária fornecida",
                "Troque a senha no primeiro acesso",
              ].map((step, i) => (
                <li key={i} className="flex gap-3 items-start">
                  <span className="shrink-0 w-6 h-6 rounded-full bg-blue-100 text-blue-700 text-xs font-bold flex items-center justify-center">
                    {i + 1}
                  </span>
                  <span>{step}</span>
                </li>
              ))}
            </ol>
          </div>
        </div>

        {/* Voltar */}
        <Link
          href="/login"
          className="flex items-center justify-center gap-2 text-sm font-medium text-blue-600 hover:text-blue-800 transition-colors"
        >
          <ArrowLeft className="w-4 h-4" />
          Voltar para o login
        </Link>
      </div>
    </div>
  );
}
