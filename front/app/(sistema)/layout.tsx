'use client'

import Footer from "../components/Footer";
import Header from "../components/Header";
import Sidebar from "../components/Sidebar";
import { useSelector } from "react-redux";
import { RootState } from "../redux/store";
import { usePathname, useRouter } from "next/navigation";
import { useEffect } from "react";

export default function SistemaLayout({ children }: { children: React.ReactNode }) {
    const { usuario } = useSelector((state: RootState) => state.auth);
    const router = useRouter();
    const pathname = usePathname();

    useEffect(() => {
        if (usuario == null) router.push("/login");
    }, [usuario, router]);

    if (usuario == null) {
        return <div className="flex min-h-screen items-center justify-center bg-blue-50 text-blue-900">
            Redirecionando para o login...
        </div>;
    }

    const administrador = usuario.role === "ROLE_ADMIN";
    const empresa = usuario.usuarioId === null && usuario.empresaId !== null;
    const usuarioEmpresa = usuario.usuarioId !== null
        && usuario.empresaId !== null
        && usuario.role === "ROLE_USER";
    const usuarioComum = !administrador && !empresa && !usuarioEmpresa;

    const receitasPermitidas = pathname === "/receitas"
        || (pathname.startsWith("/receitas/") && (empresa || usuarioEmpresa));

    const acessoPermitido = (administrador
            && !pathname.startsWith("/pacientes")
            && pathname !== "/receitas/novo")
        || pathname === "/home"
        || (pathname.startsWith("/medicamentos") && (!usuarioComum || pathname === "/medicamentos"))
        || ((administrador || empresa) && pathname.startsWith("/usuarios"))
        || receitasPermitidas
        || (usuarioEmpresa && pathname.startsWith("/pacientes"));

    if (!acessoPermitido) {
        return (
            <main className="flex min-h-screen flex-col items-center justify-center gap-4 bg-blue-50 p-6 text-center text-blue-900">
                <h1 className="text-2xl font-bold">Acesso não disponível</h1>
                <p>Este perfil não tem acesso a esta funcionalidade.</p>
                <button type="button" onClick={() => router.replace("/home")}
                    className="rounded-lg bg-teal-700 px-4 py-2 font-semibold text-white hover:bg-teal-800">
                    Voltar ao início
                </button>
            </main>
        );
    }

    return (
        <div className="flex min-h-screen bg-blue-50">
            <Sidebar />
            <div className="flex min-w-0 flex-1 flex-col overflow-hidden">
                <Header />
                <main className="flex-1 overflow-y-auto bg-blue-50 p-6 text-blue-900">
                    {children}
                </main>
                <Footer />
            </div>
        </div>
    );
}
