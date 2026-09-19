import Link from "next/link";

export default function Sidebar() {

    return (
        <aside className="w-64 min-h-screen bg-white border-r border-teal-100 flex flex-col">
    <div className="px-6 py-5 text-xl font-bold text-teal-800 tracking-tight border-b border-teal-100">
        FarmaControll
    </div>
    <nav className="flex-1 px-3 py-4 space-y-1">
        <Link href="/home" className="block px-4 py-2.5 rounded-lg text-sm font-medium text-teal-700 hover:bg-teal-50 hover:text-teal-900 transition-colors">
            Home
        </Link>
        <Link href="/usuarios" className="block px-4 py-2.5 rounded-lg text-sm font-medium text-teal-700 hover:bg-teal-50 hover:text-teal-900 transition-colors">
            Usuários
        </Link>
        <Link href="/medicamentos" className="block px-4 py-2.5 rounded-lg text-sm font-medium text-teal-700 hover:bg-teal-50 hover:text-teal-900 transition-colors">
            Medicamentos
        </Link>
        <Link href="/receitas" className="block px-4 py-2.5 rounded-lg text-sm font-medium text-teal-700 hover:bg-teal-50 hover:text-teal-900 transition-colors">
            Receitas
        </Link>
        <Link href="/pacientes" className="block px-4 py-2.5 rounded-lg text-sm font-medium text-teal-700 hover:bg-teal-50 hover:text-teal-900 transition-colors">
            pacientes
        </Link>
    </nav>
</aside>
    );
}