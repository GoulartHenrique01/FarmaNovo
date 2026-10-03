"use client";

import { Receita } from "@/app/types/receitas";
import axios from "axios";
import { useRouter } from "next/navigation";
import Link from "next/link";
import { useEffect, useState } from "react";

export default function Receitas() {
    const router = useRouter();

    // Registros que serão exibidos na tabela de receitas.
    const [receitas, setReceitas] = useState<Receita[]>([]);

    // Carrega as receitas assim que a listagem é montada.
    useEffect(() => {
        carregarDados();
    }, []);

    // Busca a lista atualizada na API.
    const carregarDados = async () => {
        try {
            const dados = await axios.get<Receita[]>("http://localhost:8080/receitas");

            setReceitas(dados.data);
        } catch (error) {
            alert("Erro ao carregar receitas!");
        }
    };

    // Solicita confirmação antes da exclusão e atualiza a listagem ao concluir.
    const handlerDeletarReceita = async (receita : Receita) => {
        var dadosRetorno = await axios.delete(
            "http://localhost:8080/receitas/" + receita.id+ "/excluir"
        );

        if(dadosRetorno.status == 200){
            alert("Receita excluida com sucesso");
        }else{
            alert(dadosRetorno.data);
            return;
        }
        carregarDados();
    };


    // Exibe as receitas cadastradas e as ações disponíveis em cada linha.
    return (
        <main className="min-h-full bg-[#f4faf8] px-4 py-8 sm:px-6 lg:px-10">
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

            <button
                type="button"
                onClick={() => router.push("/receitas/novo")}
                className="rounded-lg bg-emerald-600 px-4 py-2.5 text-sm font-semibold text-white shadow-sm transition-all duration-200 hover:bg-emerald-700 hover:shadow-md focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:ring-offset-2"
            >
                + Nova Receita
            </button>
        </div>

        <div className="overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-[0_12px_35px_rgba(15,118,110,0.08)]">
            <div className="overflow-x-auto">
                <table className="w-full min-w-[850px] border-collapse">
                    <thead>
                        <tr className="border-b border-emerald-100 bg-emerald-50/70">
                            <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-emerald-800">
                                Código
                            </th>

                            <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-emerald-800">
                                Data de Emissão
                            </th>

                            <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-emerald-800">
                                Data de Validade
                            </th>

                            <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-emerald-800">
                                Diagnóstico
                            </th>

                            <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-emerald-800">
                                Observações
                            </th>

                            <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-emerald-800">
                                Tipo
                            </th>

                            <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-emerald-800">
                                Ações
                            </th>
                        </tr>
                    </thead>

                    <tbody className="divide-y divide-slate-100">
                        {receitas.map((receita) => (
                            <tr
                                key={receita.id}
                                className="transition-colors duration-150 hover:bg-emerald-50/40"
                            >
                                <td className="px-5 py-4 text-sm font-semibold text-slate-700">
                                    {receita.id}
                                </td>

                                <td className="px-5 py-4 text-sm text-slate-600">
                                    {receita.dataEmissao}
                                </td>

                                <td className="px-5 py-4 text-sm text-slate-600">
                                    {receita.dataValidade}
                                </td>

                                <td className="px-5 py-4 text-sm font-semibold text-slate-800">
                                    {receita.diagnostico}
                                </td>

                                <td className="px-5 py-4 text-sm text-slate-600">
                                    {receita.observacoes}
                                </td>

                                <td className="px-5 py-4 text-sm text-slate-600">
                                    <span className="inline-flex rounded-full bg-emerald-50 px-2.5 py-1 text-xs font-semibold text-emerald-700 ring-1 ring-emerald-200">
                                        {receita.tipo}
                                    </span>
                                </td>

                                <td className="px-5 py-4">
                                    <div className="flex flex-wrap gap-2">
                                        <Link
                                            href={`/receitas/${receita.id}/editar`}
                                            className="rounded-md border border-emerald-200 bg-emerald-50 px-3 py-1.5 text-xs font-semibold text-emerald-700 transition-all duration-200 hover:border-emerald-300 hover:bg-emerald-100 hover:text-emerald-800"
                                        >
                                            Editar
                                        </Link>

                                        <button
                                            onClick={() => handlerDeletarReceita(receita)}
                                            className="rounded-md border border-orange-200 bg-orange-50 px-3 py-1.5 text-xs font-semibold text-orange-700 transition-all duration-200 hover:border-orange-300 hover:bg-orange-100 hover:text-orange-800 focus:outline-none focus:ring-2 focus:ring-orange-400 focus:ring-offset-1"
                                        >
                                            Deletar
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        ))}

                        {receitas.length === 0 && (
                            <tr>
                                <td
                                    colSpan={7}
                                    className="px-6 py-14 text-center text-sm text-slate-500"
                                >
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
            className="rounded-lg border border-slate-300 bg-white px-4 py-2.5 text-sm font-semibold text-slate-700 shadow-sm transition-all duration-200 hover:border-emerald-300 hover:bg-emerald-50 hover:text-emerald-700 focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:ring-offset-2"
        >
            ← Voltar para início
        </button>
    </div>
</main>
    );
}