"use client";

import { useRouter } from "next/navigation";
import { authService } from "@/shared/services/auth-service"; 
import { LogOut, Bell, UserCircle } from "lucide-react";
import { Button } from "@/components/ui/button";

export function Header() {
  const router = useRouter();
  const user = authService.getUser();

  function handleLogout() {
    authService.logout();
    router.replace("/login");
  }

  return (
    <header className="flex h-16 items-center justify-between border-b border-gray-200 bg-white px-6">
      {/* Esquerda — pode virar breadcrumb depois */}
      <div />

      {/* Direita */}
      <div className="flex items-center gap-4">
        {/* Notificações (placeholder) */}
        <button className="relative rounded-lg p-2 text-gray-400 hover:bg-gray-100 hover:text-gray-600 transition-colors cursor-pointer">
          <Bell className="h-5 w-5" />
          <span className="absolute -top-0.5 -right-0.5 h-4 w-4 rounded-full bg-red-500 text-[10px] font-bold text-white flex items-center justify-center">
            3
          </span>
        </button>

        {/* Usuário */}
        <div className="flex items-center gap-3 rounded-lg border border-gray-200 px-3 py-1.5">
          <UserCircle className="h-5 w-5 text-gray-400" />
          <span className="text-sm font-medium text-gray-700">
            {user?.nomeUsuario ?? "Usuário"}
          </span>
        </div>

        {/* Logout */}
        <Button
          variant="ghost"
          size="sm"
          onClick={handleLogout}
          className="text-gray-400 hover:text-red-600 hover:bg-red-50 cursor-pointer"
        >
          <LogOut className="h-4 w-4" />
        </Button>
      </div>
    </header>
  );
}
