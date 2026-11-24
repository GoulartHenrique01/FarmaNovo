"use client";

import { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import Link from "next/link";
import { Empresa, EmpresaRequest } from "@/app/types/empresas";
import { atualizarEmpresa, buscarEmpresaPorId } from "@/app/services/empresaService";

// FLUXO: lê o ID da rota, consulta a empresa, preenche o formulário e envia PUT /empresas/{id}.
export default function EditarEmpresa() {
    const params = useParams(); const router = useRouter(); const id = Number(params.codigo);
    const [empresa, setEmpresa] = useState<Empresa | null>(null); const [erro, setErro] = useState("");
    useEffect(() => { buscarEmpresaPorId(id).then(setEmpresa).catch(() => router.replace("/empresas")); }, [id, router]);
    const salvar = async (dados: FormData) => {
        if (!empresa) return;
        const request: EmpresaRequest = {
            razaoSocial: String(dados.get("razaoSocial") ?? ""), nomeFantasia: String(dados.get("nomeFantasia") ?? ""),
            cnpj: String(dados.get("cnpj") ?? ""), administradorNome: String(dados.get("administradorNome") ?? ""),
            administradorCpf: String(dados.get("administradorCpf") ?? ""), administradorEmail: String(dados.get("administradorEmail") ?? ""),
            administradorSenha: String(dados.get("administradorSenha") ?? "") || undefined,
        };
        try { await atualizarEmpresa(empresa.id, request); alert("Empresa atualizada com sucesso."); router.push("/empresas"); }
        catch (e) { const mensagem = (e as {response?:{data?:string}})?.response?.data; setErro(typeof mensagem === "string" ? mensagem : "Não foi possível atualizar a empresa."); }
    };
    if (!empresa) return <div className="p-8">Carregando empresa...</div>;
    return <main className="min-h-full bg-[#f4faf8] px-4 py-8"><section className="mx-auto max-w-2xl space-y-6 rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
        <div><Link href="/empresas" className="text-sm text-emerald-700">Voltar para empresas</Link><h1 className="mt-3 text-2xl font-bold text-slate-900">Editar empresa {id}</h1></div>
        <form action={salvar} className="grid gap-4 sm:grid-cols-2">
            <label className="space-y-1 text-sm">Razão social<input name="razaoSocial" required defaultValue={empresa.razaoSocial} className="w-full rounded-lg border p-2.5" /></label>
            <label className="space-y-1 text-sm">Nome fantasia<input name="nomeFantasia" required defaultValue={empresa.nomeFantasia} className="w-full rounded-lg border p-2.5" /></label>
            <label className="space-y-1 text-sm">CNPJ<input name="cnpj" required defaultValue={empresa.cnpj} className="w-full rounded-lg border p-2.5" /></label>
            <label className="space-y-1 text-sm">Responsável<input name="administradorNome" required defaultValue={empresa.nomeResponsavel} className="w-full rounded-lg border p-2.5" /></label>
            <label className="space-y-1 text-sm">CPF do responsável<input name="administradorCpf" required defaultValue={empresa.cpfResponsavel ?? ""} className="w-full rounded-lg border p-2.5" /></label>
            <label className="space-y-1 text-sm">E-mail de acesso<input name="administradorEmail" type="email" required defaultValue={empresa.email} className="w-full rounded-lg border p-2.5" /></label>
            <label className="space-y-1 text-sm sm:col-span-2">Nova senha (deixe em branco para manter)<input name="administradorSenha" type="password" className="w-full rounded-lg border p-2.5" /></label>
            {erro && <p role="alert" className="text-sm text-red-600 sm:col-span-2">{erro}</p>}
            <div className="flex justify-end gap-3 sm:col-span-2"><Link href="/empresas" className="rounded-lg border px-4 py-2">Cancelar</Link><button type="submit" className="rounded-lg bg-emerald-600 px-5 py-2 font-semibold text-white">Salvar alterações</button></div>
        </form></section></main>;
}
