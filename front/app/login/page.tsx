'use client'

import { useRouter } from "@/node_modules/next/navigation";
import axios from "axios";
import { LoginResponse } from "../types/auth";

export default function Login() {
    const router = useRouter();

    const handlerLogin = async (formData: FormData) => {
        try{
        debugger
        const emailTela = formData.get("email")?.toString() ?? ""
        const senhaTela = formData.get("senha")?.toString() ?? ""

        var loginResposta = await axios.post<LoginResponse>("http://localhost:8080/auth/login",
            { email: emailTela, senha: senhaTela })

        if (loginResposta.status == 200) {
            router.push("/home")
        }else{
            alert("Login ou senha Invalido!")
        }
    }catch(error){
        alert("Login ou senha Invalido!")
     }
}

    return (
        <>
            <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-white via-emerald-50 to-teal-100 p-4">
                <div className="w-full max-w-md bg-white rounded-2xl shadow-xl border border-emerald-100/50 p-8">
                    <div className="mb-8 text-center">
                        <h1 className="text-2xl font-bold text-teal-800">
                            Entrar no sistema
                        </h1>
                    </div>
                    <form
                        action={handlerLogin}
                        className="space-y-5">
                        <div className="space-y-2">
                            <label className="block text-sm font-medium text-teal-700">
                                E-mail
                            </label>
                            <input
                                placeholder="Informe seu e-mail"
                                name="email"
                                className="w-full px-4 py-2.5 border border-teal-200 rounded-lg bg-white text-gray-800 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500 transition"
                            />
                        </div>
                        <div className="space-y-2">
                            <label className="block text-sm font-medium text-teal-700">
                                Senha
                            </label>
                            <input
                                placeholder="Informe sua senha"
                                name="senha"
                                className="w-full px-4 py-2.5 border border-teal-200 rounded-lg bg-white text-gray-800 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500 transition"
                            />
                        </div>
                        <button
                            type="submit"
                            className="w-full bg-gradient-to-r from-teal-700 to-emerald-600 hover:from-teal-800 hover:to-emerald-700 text-white font-semibold py-2.5 rounded-lg shadow-md transition-all duration-200"
                        >
                            Entrar
                        </button>
                    </form>
                </div>
            </div>
        </>
    );
}