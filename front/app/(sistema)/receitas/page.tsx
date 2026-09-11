"use client";

import { Receita } from "@/app/types/receitas";
import axios from "axios";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

export default function Receitas() {
    const router = useRouter();

    const [receitas, setReceitas] = useState<Receita[]>([]);

    useEffect(() => {
        carregarDados();
    }, []);

    const carregarDados = async () => {
        try {
            const dados = await axios.get<Receita[]>("http://localhost:8080/receitas");

            setReceitas(dados.data);
        } catch (error) {
            alert("Erro ao carregar receitas!");
        }
    };

    return (
        <main className="min-h-screen bg-[#f4faf8] px-4 py-8 sm:px-6 lg:px-10">
            <div className="mx-auto max-w-7xl space-y-6">
                <div className="flex flex-col gap-4 sm:flex-row sm:items-end sm:justify-between">
                    <div>
                        <p className="mb-1 text-sm font-semibold uppercase tracking-[0.18em] text-emerald-700">
                            Prescrições
                        </p>
                        <h1 className="text-3xl font-bold tracking-tight text-slate-900">
                            Receitas
                        </h1>
                        <p className="mt-1 text-sm text-slate-500">
                            Consulte as receitas emitidas e seus períodos de validade.
                        </p>
                    </div>
                    <span className="w-fit rounded-full border border-emerald-200 bg-white px-3 py-1.5 text-sm font-medium text-emerald-800 shadow-sm">
                        {receitas.length} cadastradas
                    </span>
                </div>

                <div className="overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-[0_12px_35px_rgba(15,118,110,0.08)]">
                    <div className="overflow-x-auto">
                        <table className="w-full min-w-[850px] border-collapse">
        <thead>
            <tr className="border-b border-slate-200 bg-slate-50">
                <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-slate-500">
                    Código
                </th>
                <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-slate-500">
                    Data de Emissão
                </th>
                <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-slate-500">
                    Data de Validade
                </th>
                <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-slate-500">
                    Diagnóstico
                </th>
                <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-slate-500">
                    Observações
                </th>
                <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-slate-500">
                    Tipo
                </th>
            </tr>
        </thead>
                <tbody className="divide-y divide-slate-100">
                    {receitas.map((receita) => (
                        <tr key={receita.id} className="transition-colors hover:bg-emerald-50/40">
                            <td className="px-5 py-4 text-sm font-semibold text-slate-700">{receita.id}</td>
                            <td className="px-5 py-4 text-sm text-slate-600">{receita.dataEmissao}</td>
                            <td className="px-5 py-4 text-sm text-slate-600">{receita.dataValidade}</td>
                            <td className="px-5 py-4 text-sm font-medium text-slate-800">{receita.diagnostico}</td>
                            <td className="px-5 py-4 text-sm text-slate-600">{receita.observacoes}</td>
                            <td className="px-5 py-4 text-sm text-slate-600">{receita.tipo}</td>
                        </tr>
                    ))}

                    {receitas.length === 0 && (
                        <tr>
                            <td colSpan={6} className="px-6 py-14 text-center text-sm text-slate-500">
                                Nenhuma receita encontrada!
                            </td>
                        </tr>
                    )}
                </tbody>
                    </table>
                    </div>
                </div>
                <button
                    type="button"
                    onClick={() => router.push("/home")}
                    className="rounded-lg border border-slate-300 bg-white px-4 py-2.5 text-sm font-semibold text-slate-700 shadow-sm transition hover:border-emerald-300 hover:text-emerald-700 focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:ring-offset-2"
                >
                    Voltar para início
                </button>
            </div>
        </main>
    );
}