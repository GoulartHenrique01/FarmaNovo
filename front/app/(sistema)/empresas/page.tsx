"use client";

import { useCallback, useEffect, useState } from "react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { Empresa } from "@/app/types/empresas";
import { alterarStatusEmpresa, buscarListaEmpresas, excluirEmpresa } from "@/app/services/empresaService";

export default function Empresas() {
    const router = useRouter();
    const [empresas, setEmpresas] = useState<Empresa[]>([]);
    const carregar = useCallback(async () => {
        try { setEmpresas(await buscarListaEmpresas()); }
        catch { alert("Não foi possível carregar as empresas."); }
    }, []);
    useEffect(() => { void carregar(); }, [carregar]);

    const alternarStatus = async (empresa: Empresa) => {
        const novoStatus = empresa.status === "ATIVO" ? "BLOQUEADO" : "ATIVO";
        if (!confirm(`${novoStatus === "BLOQUEADO" ? "Bloquear" : "Ativar"} ${empresa.nomeFantasia}?`)) return;
        try { await alterarStatusEmpresa(empresa.id, novoStatus); await carregar(); }
        catch { alert("Não foi possível alterar o status da empresa."); }
    };
    const remover = async (empresa: Empresa) => {
        if (!confirm(`Excluir a empresa ${empresa.nomeFantasia}?`)) return;
        try { await excluirEmpresa(empresa.id); await carregar(); }
        catch { alert("Não foi possível excluir a empresa."); }
    };

    return <main className="min-h-full bg-[#f4faf8] px-4 py-8 sm:px-6 lg:px-10">
        <div className="mx-auto max-w-7xl space-y-6">
            <div className="flex flex-col gap-4 sm:flex-row sm:items-end sm:justify-between">
                <div><p className="mb-1 text-sm font-semibold uppercase tracking-[0.18em] text-emerald-700">Administração</p><h1 className="text-3xl font-bold tracking-tight text-slate-900">Empresas</h1><p className="mt-1 text-sm text-slate-500">Gerencie as empresas cadastradas no FarmaControll.</p></div>
                <Link href="/cadastro-empresa" className="rounded-lg bg-emerald-600 px-4 py-2.5 text-sm font-semibold text-white shadow-sm hover:bg-emerald-700">+ Cadastrar empresa</Link>
            </div>
            <div className="overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-sm"><div className="overflow-x-auto">
                <table className="w-full min-w-[900px] border-collapse">
                    <thead className="bg-emerald-50/70"><tr>{["ID","Nome fantasia","Razão social","CNPJ","Responsável","E-mail","Status","Ações"].map((t)=><th key={t} className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-emerald-800">{t}</th>)}</tr></thead>
                    <tbody className="divide-y divide-slate-100">
                        {empresas.map((empresa)=><tr key={empresa.id} className="hover:bg-emerald-50/40">
                            <td className="px-5 py-4 text-sm">{empresa.id}</td><td className="px-5 py-4 text-sm font-semibold">{empresa.nomeFantasia}</td><td className="px-5 py-4 text-sm">{empresa.razaoSocial}</td><td className="px-5 py-4 text-sm">{empresa.cnpj}</td><td className="px-5 py-4 text-sm">{empresa.nomeResponsavel}</td><td className="px-5 py-4 text-sm">{empresa.email}</td><td className="px-5 py-4 text-sm">{empresa.status}</td>
                            <td className="px-5 py-4"><div className="flex flex-wrap gap-2"><Link href={`/empresas/${empresa.id}/editar`} className="rounded-md border border-emerald-200 bg-emerald-50 px-3 py-1.5 text-xs font-semibold text-emerald-700">Editar</Link><button type="button" onClick={()=>void alternarStatus(empresa)} className="rounded-md border border-amber-200 bg-amber-50 px-3 py-1.5 text-xs font-semibold text-amber-700">{empresa.status === "ATIVO" ? "Bloquear" : "Ativar"}</button><button type="button" onClick={()=>void remover(empresa)} className="rounded-md border border-red-200 bg-red-50 px-3 py-1.5 text-xs font-semibold text-red-700">Excluir</button></div></td>
                        </tr>)}
                        {empresas.length===0 && <tr><td colSpan={8} className="px-6 py-14 text-center text-sm text-slate-500">Nenhuma empresa cadastrada.</td></tr>}
                    </tbody>
                </table>
            </div></div>
            <button type="button" onClick={()=>router.push("/home")} className="rounded-lg border border-slate-300 bg-white px-4 py-2.5 text-sm font-semibold text-slate-700">Voltar para início</button>
        </div>
    </main>;
}
