"use client";

import { Paciente } from "@/app/types/pacientes";
import axios from "axios";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

export default function Pacientes() {
    const router = useRouter();

    const [pacientes, setPacientes] = useState<Paciente[]>([]);

    useEffect(() => {
        carregarDados();
    }, []);

    const carregarDados = async () => {
        try {
            const dados = await axios.get<Paciente[]>("http://localhost:8080/pacientes");

            setPacientes(dados.data);
        } catch (error) {
            alert("Erro ao carregar pacientes!");
        }
    };

    return (
        <main className="min-h-screen bg-[#f4faf8] px-4 py-8 sm:px-6 lg:px-10">
            <div className="mx-auto max-w-7xl space-y-6">
                <div className="flex flex-col gap-4 sm:flex-row sm:items-end sm:justify-between">
                    <div>
                        <p className="mb-1 text-sm font-semibold uppercase tracking-[0.18em] text-emerald-700">
                            Atendimento
                        </p>
                        <h1 className="text-3xl font-bold tracking-tight text-slate-900">
                            Pacientes
                        </h1>
                        <p className="mt-1 text-sm text-slate-500">
                            Acesse os dados e o status dos pacientes cadastrados.
                        </p>
                    </div>
                    <span className="w-fit rounded-full border border-emerald-200 bg-white px-3 py-1.5 text-sm font-medium text-emerald-800 shadow-sm">
                        {pacientes.length} cadastrados
                    </span>
                </div>

                <div className="overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-[0_12px_35px_rgba(15,118,110,0.08)]">
                    <div className="overflow-x-auto">
                        <table className="w-full min-w-[1100px] border-collapse">
        <thead>
            <tr className="border-b border-slate-200 bg-slate-50">
                <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-slate-500">
                    ID
                </th>
                <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-slate-500">
                    Nome
                </th>
                <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-slate-500">
                    CPF
                </th>
                <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-slate-500">
                    Data de Nascimento
                </th>
                <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-slate-500">
                    Sexo
                </th>
                <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-slate-500">
                    Telefone
                </th>
                <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-slate-500">
                    Email
                </th>
                <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-slate-500">
                    Observações
                </th>
                <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-slate-500">
                    Status
                </th>
            </tr>
        </thead>
                <tbody className="divide-y divide-slate-100">
                    {pacientes.map((paciente) => (
                        <tr key={paciente.id} className="transition-colors hover:bg-emerald-50/40">
                            <td className="px-5 py-4 text-sm font-semibold text-slate-700">{paciente.id}</td>
                            <td className="px-5 py-4 text-sm font-medium text-slate-800">{paciente.nome}</td>
                            <td className="px-5 py-4 text-sm text-slate-600">{paciente.cpf}</td>
                            <td className="px-5 py-4 text-sm text-slate-600">{paciente.dataNascimento}</td>
                            <td className="px-5 py-4 text-sm text-slate-600">{paciente.sexo}</td>
                            <td className="px-5 py-4 text-sm text-slate-600">{paciente.telefone}</td>
                            <td className="px-5 py-4 text-sm text-slate-600">{paciente.email}</td>
                            <td className="px-5 py-4 text-sm text-slate-600">{paciente.observacoes}</td>
                            <td className="px-5 py-4 text-sm text-slate-600">
                                <span className="inline-flex rounded-full bg-emerald-50 px-2.5 py-1 text-xs font-semibold text-emerald-700">
                                    {paciente.status}
                                </span>
                            </td>
                        </tr>
                    ))}

                    {pacientes.length === 0 && (
                        <tr>
                            <td colSpan={9} className="px-6 py-14 text-center text-sm text-slate-500">
                                Nenhum paciente encontrado!
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