import { AuthGuard } from "@/shared/components/auth-guard"; 
import { Sidebar } from "@/layouts/sidebar"; 
import { Header } from "@/layouts/header"; 

export default function PainelLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <AuthGuard>
      <div className="flex h-screen overflow-hidden bg-gray-50">
        <Sidebar />
        <div className="flex flex-1 flex-col overflow-hidden">
          <Header />
          <main className="flex-1 overflow-y-auto p-6">
            {children}
          </main>
        </div>
      </div>
    </AuthGuard>
  );
}
