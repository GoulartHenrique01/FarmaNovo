import Link from "next/link";
import { useSelector } from "react-redux";
import { RootState } from "../redux/store";

// A navegação é montada conforme a identidade Redux: ADMIN vê empresas; USER vê medicamentos, pacientes e receitas.
export default function Sidebar() {
    const identidade = useSelector((state: RootState) => state.auth.usuario);
    const administrador = identidade?.role === "ROLE_ADMIN";
    const empresa = identidade !== null && identidade.role === "ROLE_USER" && identidade.empresaId !== null;

    return (
        <aside className="flex min-h-screen w-64 flex-col border-r border-teal-100 bg-white">
            <div className="border-b border-teal-100 px-6 py-5 text-xl font-bold tracking-tight text-teal-800">FarmaControll</div>
            <nav className="flex-1 space-y-1 px-3 py-4">
                <Link href="/home" className="block rounded-lg px-4 py-2.5 text-sm font-medium text-teal-700 transition-colors hover:bg-teal-50 hover:text-teal-900">Home</Link>
                {administrador && <Link href="/empresas" className="block rounded-lg px-4 py-2.5 text-sm font-medium text-teal-700 transition-colors hover:bg-teal-50 hover:text-teal-900">Empresas</Link>}
                {empresa && <>
                    <Link href="/medicamentos" className="block rounded-lg px-4 py-2.5 text-sm font-medium text-teal-700 transition-colors hover:bg-teal-50 hover:text-teal-900">Medicamentos</Link>
                    <Link href="/pacientes" className="block rounded-lg px-4 py-2.5 text-sm font-medium text-teal-700 transition-colors hover:bg-teal-50 hover:text-teal-900">Pacientes</Link>
                    <Link href="/receitas" className="block rounded-lg px-4 py-2.5 text-sm font-medium text-teal-700 transition-colors hover:bg-teal-50 hover:text-teal-900">Receitas</Link>
                </>}
            </nav>
        </aside>
    );
}
