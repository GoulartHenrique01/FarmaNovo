"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";
import axios from "axios";
import { buscarEnderecoPorCep, cadastrarEmpresa } from "../services/empresaService";

// FLUXO: CEP -> API do backend -> ViaCEP -> preenchimento do endereço -> cadastro da empresa.
export default function CadastroEmpresa() {
    const router = useRouter();
    const [erro, setErro] = useState("");
    const [buscandoCep, setBuscandoCep] = useState(false);
    const [mensagemCep, setMensagemCep] = useState("");
    const [endereco, setEndereco] = useState({
        logradouro: "", bairro: "", cidade: "", uf: ""
    });

    const consultarCep = async (cep: string) => {
        const cepLimpo = cep.replace(/\D/g, "");

        // Limpa o endereço anterior ao iniciar uma nova consulta.
        setEndereco({
            logradouro: "",
            bairro: "",
            cidade: "",
            uf: ""
        });

        setMensagemCep("");

        if (cepLimpo.length !== 8) {
            setBuscandoCep(false);
            return;
        }

        setBuscandoCep(true);

        console.log("Consultando CEP:", cepLimpo);

        try {
            const dados = await buscarEnderecoPorCep(cepLimpo);

            console.log("Resposta ViaCEP:", dados);

            setEndereco({
                logradouro: dados.logradouro ?? "",
                bairro: dados.bairro ?? "",
                cidade: dados.cidade ?? "",
                uf: dados.uf ?? ""
            });

            setMensagemCep("Endereço encontrado.");

        } catch (error) {
            console.error("Erro ao consultar CEP:", error);
            setMensagemCep("Não foi possível encontrar esse CEP. Verifique e tente novamente.");

        } finally {
            setBuscandoCep(false);
        }
    };

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
                cep: String(formData.get("cep") ?? ""),
                logradouro: String(formData.get("logradouro") ?? ""),
                numero: String(formData.get("numero") ?? ""),
                complemento: String(formData.get("complemento") ?? ""),
                bairro: String(formData.get("bairro") ?? ""),
                cidade: String(formData.get("cidade") ?? ""),
                uf: String(formData.get("uf") ?? "")
            });
            alert("Empresa cadastrada. Agora você pode entrar com o e-mail e a senha informados.");
            router.push("/login");
        } catch (error: unknown) {
            const resposta = axios.isAxiosError(error) ? error.response?.data : null;
            setErro(typeof resposta === "string" && resposta.trim()
                ? resposta
                : "Não foi possível cadastrar a empresa. Verifique os dados e tente novamente.");
        }
    };

    const campo = "w-full rounded-lg border border-slate-700 bg-slate-950 px-3 py-2 text-white";
    const rotulo = "space-y-2 text-sm text-slate-300";

    return (
        <main className="min-h-screen w-full flex items-center justify-center bg-slate-950 px-4 py-8">
            <section className="w-full max-w-2xl bg-slate-900 border border-slate-800 rounded-2xl shadow-2xl p-8 space-y-6">
                <header className="text-center space-y-2">
                    <h1 className="text-2xl font-bold tracking-tight text-white">Cadastrar empresa</h1>
                    <p className="text-sm text-slate-400">Crie a empresa e suas credenciais de acesso ao sistema.</p>
                </header>

                <form action={salvarEmpresa} className="grid gap-4 sm:grid-cols-2">
                    <label className={rotulo}>Razão social
                        <input name="razaoSocial" required className={campo} />
                    </label>
                    <label className={rotulo}>Nome fantasia
                        <input name="nomeFantasia" required className={campo} />
                    </label>
                    <label className={rotulo}>CNPJ
                        <input name="cnpj" required className={campo} />
                    </label>

                    <div className="sm:col-span-2 border-t border-slate-800 pt-4">
                        <h2 className="font-semibold text-white">Endereço da empresa</h2>
                    </div>
                    <label className={rotulo}>CEP
                        <input
                            name="cep"
                            required
                            maxLength={9}
                            placeholder="00000-000"
                            className={campo}
                            onChange={(event) => {
                                setEndereco({
                                    logradouro: "",
                                    bairro: "",
                                    cidade: "",
                                    uf: ""
                                });

                                setMensagemCep("");
                            }}
                            onBlur={(event) => consultarCep(event.currentTarget.value)}
                        />
                        {buscandoCep && <span className="text-xs text-slate-400">Consultando CEP...</span>}
                        {mensagemCep && <span className="text-xs text-slate-400">{mensagemCep}</span>}
                    </label>
                    <label className={rotulo}>Logradouro
                        <input name="logradouro" value={endereco.logradouro} onChange={(e) => setEndereco({ ...endereco, logradouro: e.target.value })} className={campo} />
                    </label>
                    <label className={rotulo}>Número
                        <input name="numero" className={campo} />
                    </label>
                    <label className={rotulo}>Complemento
                        <input name="complemento" className={campo} />
                    </label>
                    <label className={rotulo}>Bairro
                        <input name="bairro" value={endereco.bairro} onChange={(e) => setEndereco({ ...endereco, bairro: e.target.value })} className={campo} />
                    </label>
                    <label className={rotulo}>Cidade
                        <input name="cidade" value={endereco.cidade} onChange={(e) => setEndereco({ ...endereco, cidade: e.target.value })} className={campo} />
                    </label>
                    <label className={rotulo}>UF
                        <input name="uf" maxLength={2} value={endereco.uf} onChange={(e) => setEndereco({ ...endereco, uf: e.target.value.toUpperCase() })} className={campo} />
                    </label>

                    <div className="sm:col-span-2 border-t border-slate-800 pt-4">
                        <h2 className="font-semibold text-white">Contato da empresa e acesso</h2>
                    </div>
                    <label className={rotulo}>Nome
                        <input name="administradorNome" required className={campo} />
                    </label>
                    <label className={rotulo}>CPF
                        <input name="administradorCpf" required className={campo} />
                    </label>
                    <label className={rotulo}>E-mail de acesso
                        <input name="administradorEmail" type="email" required className={campo} />
                    </label>
                    <label className={rotulo}>Senha
                        <input name="administradorSenha" type="password" required className={campo} />
                    </label>
                    {erro && <p role="alert" className="sm:col-span-2 text-sm text-red-400">{erro}</p>}
                    <div className="sm:col-span-2 flex items-center justify-between pt-2">
                        <Link href="/login" className="text-sm text-slate-300 hover:text-white">Voltar ao login</Link>
                        <button type="submit" className="rounded-lg bg-blue-600 px-5 py-2.5 font-medium text-white hover:bg-blue-500">Cadastrar empresa</button>
                    </div>
                </form>
            </section>
        </main>
    );
}
