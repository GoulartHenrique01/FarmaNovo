"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";
import axios from "axios";
import { cadastrarEmpresa } from "../services/empresaService";

// FLUXO DE PRIMEIRO ACESSO DA EMPRESA: formulário -> empresaService -> POST /empresas -> retorno ao login.
export default function CadastroEmpresa() {
    const router = useRouter();
    const [erro, setErro] = useState("");

    const salvarEmpresa = async (formData: FormData) => {
        setErro("");
        try {
            await cadastrarEmpresa({
                razaoSocial: String(formData.get("razaoSocial") ?? ""),
                nomeFantasia: String(formData.get("nomeFantasia") ?? ""),
                cnpj: String(formData.get("cnpj") ?? ""),
                administradorNome: String(formData.get("administradorNome") ?? ""),
                administradorEmail: String(formData.get("administradorEmail") ?? ""),
                administradorSenha: String(formData.get("administradorSenha") ?? ""),
                administradorCpf: String(formData.get("administradorCpf") ?? ""),
            });
            alert("Empresa cadastrada. Agora você pode entrar com o e-mail e a senha informados.");
            router.push("/login");
        } catch (error: unknown) {
            const resposta = axios.isAxiosError(error) ? error.response?.data : null;
            setErro(
                typeof resposta === "string" && resposta.trim()
                    ? resposta
                    : "Não foi possível cadastrar a empresa. Verifique os dados e tente novamente."
            );
        }
    };

    return (
        <main className="min-h-screen w-full flex items-center justify-center bg-slate-950 px-4 py-8">
            <section className="w-full max-w-2xl bg-slate-900 border border-slate-800 rounded-2xl shadow-2xl p-8 space-y-6">
                <header className="text-center space-y-2">
                    <h1 className="text-2xl font-bold tracking-tight text-white">Cadastrar empresa</h1>
                    <p className="text-sm text-slate-400">
                        Crie a empresa e suas credenciais de acesso ao sistema.
                    </p>
                </header>

                <form action={salvarEmpresa} className="grid gap-4 sm:grid-cols-2">
                    <label className="space-y-2 text-sm text-slate-300">
                        Razão social
                        <input name="razaoSocial" required className="w-full rounded-lg border border-slate-700 bg-slate-950 px-3 py-2 text-white" />
                    </label>
                    <label className="space-y-2 text-sm text-slate-300">
                        Nome fantasia
                        <input name="nomeFantasia" required className="w-full rounded-lg border border-slate-700 bg-slate-950 px-3 py-2 text-white" />
                    </label>
                    <label className="space-y-2 text-sm text-slate-300">
                        CNPJ
                        <input name="cnpj" required className="w-full rounded-lg border border-slate-700 bg-slate-950 px-3 py-2 text-white" />
                    </label>
                    <div className="sm:col-span-2 border-t border-slate-800 pt-4">
                        <h2 className="font-semibold text-white">Contato da empresa e acesso</h2>
                    </div>
                    <label className="space-y-2 text-sm text-slate-300">
                        Nome
                        <input name="administradorNome" required className="w-full rounded-lg border border-slate-700 bg-slate-950 px-3 py-2 text-white" />
                    </label>
                    <label className="space-y-2 text-sm text-slate-300">
                        CPF
                        <input name="administradorCpf" required className="w-full rounded-lg border border-slate-700 bg-slate-950 px-3 py-2 text-white" />
                    </label>
                    <label className="space-y-2 text-sm text-slate-300">
                        E-mail de acesso
                        <input name="administradorEmail" type="email" required className="w-full rounded-lg border border-slate-700 bg-slate-950 px-3 py-2 text-white" />
                    </label>
                    <label className="space-y-2 text-sm text-slate-300">
                        Senha
                        <input name="administradorSenha" type="password" required className="w-full rounded-lg border border-slate-700 bg-slate-950 px-3 py-2 text-white" />
                    </label>
                    {erro && <p role="alert" className="sm:col-span-2 text-sm text-red-400">{erro}</p>}
                    <div className="sm:col-span-2 flex items-center justify-between pt-2">
                        <Link href="/login" className="text-sm text-slate-300 hover:text-white">
                            Voltar ao login
                        </Link>
                        <button type="submit" className="rounded-lg bg-blue-600 px-5 py-2.5 font-medium text-white hover:bg-blue-500">
                            Cadastrar empresa
                        </button>
                    </div>
                </form>
            </section>
        </main>
    );
}
