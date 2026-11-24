"use client";

import { buscarListaPacientes, deletarPaciente } from "@/app/services/pacienteService";
import { Paciente } from "@/app/types/pacientes";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

// FLUXO: consulta pacientes da empresa autenticada; edição e exclusão lógica passam pelo pacienteService.
export default function Pacientes() {
  const router = useRouter();
  const [pacientes, setPacientes] = useState<Paciente[]>([]);

  const carregarDados = async () => {
    try {
      setPacientes(await buscarListaPacientes());
    } catch {
      alert("Erro ao carregar pacientes!");
    }
  };

  useEffect(() => {
    buscarListaPacientes()
      .then(setPacientes)
      .catch(() => alert("Erro ao carregar pacientes!"));
  }, []);

  const handlerDeletarPaciente = async (paciente: Paciente) => {
    if (!confirm(`Excluir o paciente ${paciente.nome}?`)) return;

    try {
      const status = await deletarPaciente(paciente.id ?? 0);
      if (status !== 200) {
        alert("Erro ao excluir paciente!");
        return;
      }
      alert("Paciente excluído com sucesso!");
      await carregarDados();
    } catch {
      alert("Erro ao excluir paciente!");
    }
  };

  return (
    <main className="min-h-full bg-[#f4faf8] px-4 py-8 sm:px-6 lg:px-10">
      <div className="mx-auto max-w-7xl space-y-6">
        <div className="flex flex-col gap-4 sm:flex-row sm:items-end sm:justify-between">
          <div>
            <p className="mb-1 text-sm font-semibold uppercase tracking-[0.18em] text-emerald-700">
              Cadastros
            </p>
            <h1 className="text-3xl font-bold tracking-tight text-slate-900">Pacientes</h1>
            <p className="mt-1 text-sm text-slate-500">
              Consulte e gerencie os pacientes cadastrados pela empresa.
            </p>
          </div>
          <button
            type="button"
            onClick={() => router.push("/pacientes/novo")}
            className="rounded-lg bg-emerald-600 px-4 py-2.5 text-sm font-semibold text-white shadow-sm transition-all duration-200 hover:bg-emerald-700 hover:shadow-md focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:ring-offset-2"
          >
            + Novo Paciente
          </button>
        </div>

        <div className="overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-[0_12px_35px_rgba(15,118,110,0.08)]">
          <div className="overflow-x-auto">
            <table className="w-full min-w-[800px] border-collapse">
              <thead>
                <tr className="border-b border-emerald-100 bg-emerald-50/70">
                  <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-emerald-800">Código</th>
                  <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-emerald-800">Nome</th>
                  <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-emerald-800">CPF</th>
                  <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-emerald-800">Telefone</th>
                  <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-emerald-800">Status</th>
                  <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-emerald-800">Ações</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {pacientes.map((paciente) => (
                  <tr key={paciente.id} className="transition-colors duration-150 hover:bg-emerald-50/40">
                    <td className="px-5 py-4 text-sm font-semibold text-slate-700">{paciente.id}</td>
                    <td className="px-5 py-4 text-sm font-semibold text-slate-800">{paciente.nome}</td>
                    <td className="px-5 py-4 text-sm text-slate-600">{paciente.cpf}</td>
                    <td className="px-5 py-4 text-sm text-slate-600">{paciente.telefone || "-"}</td>
                    <td className="px-5 py-4 text-sm">
                      <span className={paciente.status === "BLOQUEADO"
                        ? "inline-flex rounded-full bg-orange-50 px-2.5 py-1 text-xs font-semibold text-orange-700 ring-1 ring-orange-200"
                        : "inline-flex rounded-full bg-emerald-50 px-2.5 py-1 text-xs font-semibold text-emerald-700 ring-1 ring-emerald-200"}>
                        {paciente.status}
                      </span>
                    </td>
                    <td className="px-5 py-4">
                      <div className="flex flex-wrap items-center gap-2">
                        <Link
                          href={`/pacientes/${paciente.id}/editar`}
                          className="rounded-md border border-emerald-200 bg-emerald-50 px-3 py-1.5 text-xs font-semibold text-emerald-700 transition-all duration-200 hover:border-emerald-300 hover:bg-emerald-100"
                        >
                          Editar
                        </Link>
                        <button
                          type="button"
                          onClick={() => handlerDeletarPaciente(paciente)}
                          className="rounded-md border border-orange-200 bg-orange-50 px-3 py-1.5 text-xs font-semibold text-orange-700 transition-all duration-200 hover:border-orange-300 hover:bg-orange-100"
                        >
                          Excluir
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
                {pacientes.length === 0 && (
                  <tr>
                    <td colSpan={6} className="px-6 py-14 text-center text-sm text-slate-500">
                      Nenhum paciente encontrado!
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </main>
  );
}
