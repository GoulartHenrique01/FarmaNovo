import Link from "next/link";
import { useSelector } from "react-redux";
import { RootState } from "../redux/store";

// Navegação principal entre as áreas autenticadas do sistema.
export default function Sidebar() {
    const identidade = useSelector((state: RootState) => state.auth.usuario);
    const administrador = identidade?.role === "ROLE_ADMIN";
    const empresa = identidade !== null
        && identidade.usuarioId === null
        && identidade.empresaId !== null;
    const usuarioEmpresa = identidade !== null
        && identidade.usuarioId !== null
        && identidade.empresaId !== null
        && identidade.role === "ROLE_USER";

    return (
        <aside className="w-64 min-h-screen bg-white border-r border-teal-100 flex flex-col">
    <div className="px-6 py-5 text-xl font-bold text-teal-800 tracking-tight border-b border-teal-100">
        FarmaControll
    </div>
    <nav className="flex-1 px-3 py-4 space-y-1">
        <Link href="/home" className="block px-4 py-2.5 rounded-lg text-sm font-medium text-teal-700 hover:bg-teal-50 hover:text-teal-900 transition-colors">
            Home
        </Link>
        {(administrador || empresa) && (
            <Link href="/usuarios" className="block px-4 py-2.5 rounded-lg text-sm font-medium text-teal-700 hover:bg-teal-50 hover:text-teal-900 transition-colors">
                Usuários
            </Link>
        )}
        <Link href="/medicamentos" className="block px-4 py-2.5 rounded-lg text-sm font-medium text-teal-700 hover:bg-teal-50 hover:text-teal-900 transition-colors">
            Medicamentos
        </Link>
        <Link href="/receitas" className="block px-4 py-2.5 rounded-lg text-sm font-medium text-teal-700 hover:bg-teal-50 hover:text-teal-900 transition-colors">
            Receitas
        </Link>
        {usuarioEmpresa && (
            <Link href="/pacientes" className="block px-4 py-2.5 rounded-lg text-sm font-medium text-teal-700 hover:bg-teal-50 hover:text-teal-900 transition-colors">
                Pacientes
            </Link>
        )}
    </nav>
</aside>
    );
}