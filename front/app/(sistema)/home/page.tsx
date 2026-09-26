'use client'

import { useRouter } from "next/navigation";

// Tela inicial apresentada após a entrada no sistema.
export default function Home() {

    const router = useRouter();

    return (
        <div className="bg-[#f4faf8] px-4 py-12 sm:px-6 lg:px-10">
            <h1 className="mx-auto max-w-3xl text-center text-3xl font-bold tracking-tight text-slate-900 sm:text-4xl">
                Bem vindo ao sistema de Gestão de Farmacias
            </h1>
        </div>
    );
}