'use client'

import { useRouter } from "next/navigation";

export default function Home() {

    const router = useRouter();

    return (
        <div className="min-h-screen bg-[#f4faf8] px-4 py-12 sm:px-6 lg:px-10">
            <h1 className="mx-auto max-w-3xl text-center text-3xl font-bold tracking-tight text-slate-900 sm:text-4xl">
                Bem vindo ao sistema de Gestão de Farmacias
            </h1>

            <form className="mx-auto mt-10 grid max-w-3xl grid-cols-1 gap-4 sm:grid-cols-2">
            <button 
                type="button"
                onClick={() => router.push("/usuarios")}
                className="rounded-xl border border-emerald-700 bg-emerald-700 px-5 py-4 text-left text-sm font-semibold text-white shadow-[0_10px_24px_rgba(4,120,87,0.18)] transition hover:-translate-y-0.5 hover:bg-emerald-800 focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:ring-offset-2"
            >
                Usuarios
            </button>

             <button
                     type="button"
                     onClick={() => router.push("/receitas")}
                className="rounded-xl border border-slate-200 bg-white px-5 py-4 text-left text-sm font-semibold text-slate-800 shadow-[0_10px_24px_rgba(15,118,110,0.08)] transition hover:-translate-y-0.5 hover:border-emerald-300 hover:text-emerald-800 focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:ring-offset-2"
            >
                Receitas
            </button>

             <button
                     type="button"
                     onClick={() => router.push("/pacientes")}
                className="rounded-xl border border-slate-200 bg-white px-5 py-4 text-left text-sm font-semibold text-slate-800 shadow-[0_10px_24px_rgba(15,118,110,0.08)] transition hover:-translate-y-0.5 hover:border-emerald-300 hover:text-emerald-800 focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:ring-offset-2"
            >
                Pacientes
            </button>

            <button
                     type="button"
                     onClick={() => router.push("/medicamentos")}
                className="rounded-xl border border-slate-200 bg-white px-5 py-4 text-left text-sm font-semibold text-slate-800 shadow-[0_10px_24px_rgba(15,118,110,0.08)] transition hover:-translate-y-0.5 hover:border-emerald-300 hover:text-emerald-800 focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:ring-offset-2"
            >
                Medicamentos
            </button>
            </form>
        </div>
    );
}