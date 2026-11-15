'use client'

import { useRouter } from "next/navigation";
import { useDispatch } from "react-redux";
import { login } from "../redux/slices/authSlice";
import { buscarIdentidadeAutenticada } from "../services/usuarioService";
import { loginBack } from "../services/authService";
import Link from "next/link";
import axios from "axios";
import { useState } from "react";

export default function Login(){
    const router = useRouter();
    const dispatch = useDispatch();
    const [erro, setErro] = useState("");

    const handleLogin = async(formData:FormData) =>{
        setErro("");
        try {
            const emailTela = formData.get("email")?.toString() ?? "";
            const senhaTela = formData.get("senha")?.toString() ?? "";

            const loginResposta = await loginBack(emailTela, senhaTela);

            if(loginResposta!=null){
                const identidade = await buscarIdentidadeAutenticada(loginResposta.token);
                dispatch(login({
                    token: loginResposta.token,
                    usuario: identidade
                }));

                router.push("/home")
            }else{
                setErro("E-mail ou senha inválidos.");
            }
        } catch (error: unknown) {
            const resposta = axios.isAxiosError(error) ? error.response?.data : null;
            setErro(typeof resposta === "string" && resposta.trim()
                ? resposta
                : "Não foi possível conectar ao servidor. Verifique se a API está em execução.");
        }
    }



    return(
        <div className="min-h-screen w-full flex items-center justify-center bg-slate-950 px-4">
            <div className="w-full max-w-md bg-slate-900 border border-slate-800 rounded-2xl shadow-2xl p-8 space-y-6">
                <div className="text-center space-y-2">
                    <h1 className="text-2xl font-bold tracking-tight text-white">
                        Entrar no sistema
                    </h1>
                    <p className="text-sm text-slate-400">Insira suas credenciais para acessar o painel</p>
                </div>
                <form action={handleLogin} className="space-y-4" >
                    <div className="space-y-2">
                        <label className="block text-sm font-medium text-slate-300">
                            E-Mail
                        </label>
                        <input
                            name="email"
                            type="email"
                            placeholder="seu@email.com"
                            className="w-full px-4 py-2.5 bg-slate-950 border border-slate-800 rounded-lg text-white placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-blue-600 focus:border-transparent transition-all"
                        >
                        </input>
                    </div>
                    <div className="space-y-2">
                        <label className="block text-sm font-medium text-slate-300">
                            senha
                        </label>
                        <input
                            name="senha"
                            type="password"
                            placeholder="••••••••"
                            className="w-full px-4 py-2.5 bg-slate-950 border border-slate-800 rounded-lg text-white placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-blue-600 focus:border-transparent transition-all"
                        >
                        </input>
                    </div>
                    <button 
                        type="submit"
                        className="w-full py-2.5 px-4 bg-blue-600 hover:bg-blue-500 text-white font-medium rounded-lg shadow-lg shadow-blue-900/30 transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-blue-600 focus:ring-offset-2 focus:ring-offset-slate-900"
                    >
                        Entrar
                    </button>
                    {erro && <p role="alert" className="text-sm text-red-400">{erro}</p>}
                </form>
                <p className="text-center text-sm text-slate-400">
                    Sua empresa ainda não tem acesso?{" "}
                    <Link href="/cadastro-empresa" className="text-blue-400 hover:text-blue-300">
                        Cadastre-se
                    </Link>
                </p>
            </div>
        </div>
    );
}