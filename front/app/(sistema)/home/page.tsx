'use client'

import { useRouter } from "@/node_modules/next/navigation";

export default function Home() {

    const router = useRouter();

    const handlerLogin = async (formData: FormData) => {
        router.push("/usuarios")
    }

    return (
        <div className="mb-8 text-center">
            <h1 className="text-3xl font-bold text-teal-800 tracking-tight">
                Bem vindo ao sistema de Gestão de Farmacias
            </h1>

            <form action={handlerLogin}>
            <button 
                type="submit"
                className="w-full bg-gradient-to-r from-teal-700 to-emerald-600 hover:from-teal-800 hover:to-emerald-700 text-white font-semibold py-2.5 rounded-lg shadow-md transition-all duration-200"
            >
                Usuarios
            </button>
            </form>
        </div>
    );
}