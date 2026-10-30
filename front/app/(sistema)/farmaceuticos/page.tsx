"use client";

import {
  atualizarStatusFarmaceutico,
  buscarListaFarmaceuticos,
  deletarFarmaceutico,
} from "@/app/services/farmaceuticoService";
import { Farmaceutico } from "@/app/types/farmaceuticos";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

export default function Farmaceuticos() {
  const router = useRouter();
  const [farmaceuticos, setFarmaceuticos] = useState<Farmaceutico[]>([]);

  const carregarDados = async () => {
    try {
      setFarmaceuticos(await buscarListaFarmaceuticos());
    } catch {
      alert("Erro ao carregar farmacêuticos!");
    }
  };

  useEffect(() => {
    buscarListaFarmaceuticos()
      .then(setFarmaceuticos)
      .catch(() => alert("Erro ao carregar farmacêuticos!"));
  }, []);

  const handlerDeletarFarmaceutico = async (farmaceutico: Farmaceutico) => {
    if (!confirm(`Excluir o farmacêutico ${farmaceutico.nome}?`)) return;

    try {
      const status = await deletarFarmaceutico(farmaceutico.id || 0);
      if (status !== 200) {
        alert("Erro ao excluir farmacêutico!");
        return;
      }
      alert("Farmacêutico excluído com sucesso!");
      await carregarDados();
    } catch {
      alert("Erro ao excluir farmacêutico!");
    }
  };

  const handlerAlterarStatusFarmaceutico = async (farmaceutico: Farmaceutico) => {
    try {
      const status = await atualizarStatusFarmaceutico(farmaceutico);
      if (status !== 200) {
        alert("Erro ao atualizar status do farmacêutico!");
        return;
      }
      await carregarDados();
    } catch {
      alert("Erro ao atualizar status do farmacêutico!");
    }
  };

  return (
    <main className="min-h-full bg-[#f4faf8] px-4 py-8 sm:px-6 lg:px-10">
      <div className="mx-auto max-w-7xl space-y-6">
        <div className="flex flex-col gap-4 sm:flex-row sm:items-end sm:justify-between">
          <div>
            <p className="mb-1 text-sm font-semibold uppercase tracking-[0.18em] text-emerald-700">
              Administração
            </p>
            <h1 className="text-3xl font-bold tracking-tight text-slate-900">
              Farmacêuticos
            </h1>
            <p className="mt-1 text-sm text-slate-500">
              Consulte os farmacêuticos cadastrados no sistema.
            </p>
          </div>
          <button
            type="button"
            onClick={() => router.push("/farmaceuticos/novo")}
            className="rounded-lg bg-emerald-600 px-4 py-2.5 text-sm font-semibold text-white shadow-sm transition-all duration-200 hover:bg-emerald-700 hover:shadow-md focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:ring-offset-2"
          >
            + Novo Farmacêutico
          </button>
        </div>

        <div className="overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-[0_12px_35px_rgba(15,118,110,0.08)]">
          <div className="overflow-x-auto">
            <table className="w-full min-w-[900px] border-collapse">
              <thead>
                <tr className="border-b border-emerald-100 bg-emerald-50/70">
                  <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-emerald-800">Código</th>
                  <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-emerald-800">Nome</th>
                  <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-emerald-800">CPF</th>
                  <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-emerald-800">Telefone</th>
                  <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-emerald-800">E-mail</th>
                  <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-emerald-800">Status</th>
                  <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-emerald-800">Ações</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {farmaceuticos.map((farmaceutico) => (
                  <tr key={farmaceutico.id} className="transition-colors duration-150 hover:bg-emerald-50/40">
                    <td className="px-5 py-4 text-sm font-semibold text-slate-700">{farmaceutico.id}</td>
                    <td className="px-5 py-4 text-sm font-semibold text-slate-800">{farmaceutico.nome}</td>
                    <td className="px-5 py-4 text-sm text-slate-600">{farmaceutico.cpf}</td>
                    <td className="px-5 py-4 text-sm text-slate-600">{farmaceutico.telefone || "-"}</td>
                    <td className="px-5 py-4 text-sm text-slate-600">{farmaceutico.email}</td>
                    <td className="px-5 py-4 text-sm">
                      <span className={farmaceutico.status === "BLOQUEADO"
                        ? "inline-flex rounded-full bg-orange-50 px-2.5 py-1 text-xs font-semibold text-orange-700 ring-1 ring-orange-200"
                        : "inline-flex rounded-full bg-emerald-50 px-2.5 py-1 text-xs font-semibold text-emerald-700 ring-1 ring-emerald-200"}>
                        {farmaceutico.status}
                      </span>
                    </td>
                    <td className="px-5 py-4">
                      <div className="flex flex-wrap items-center gap-2">
                        <Link
                          href={`/farmaceuticos/${farmaceutico.id}/editar`}
                          className="rounded-md border border-emerald-200 bg-emerald-50 px-3 py-1.5 text-xs font-semibold text-emerald-700 transition-all duration-200 hover:border-emerald-300 hover:bg-emerald-100"
                        >
                          Editar
                        </Link>
                        <button
                          type="button"
                          onClick={() => handlerDeletarFarmaceutico(farmaceutico)}
                          className="rounded-md border border-orange-200 bg-orange-50 px-3 py-1.5 text-xs font-semibold text-orange-700 transition-all duration-200 hover:border-orange-300 hover:bg-orange-100"
                        >
                          Deletar
                        </button>
                        <button
                          type="button"
                          onClick={() => handlerAlterarStatusFarmaceutico(farmaceutico)}
                          className="rounded-md border border-slate-200 bg-white px-3 py-1.5 text-xs font-semibold text-slate-700 transition-all duration-200 hover:bg-slate-50"
                        >
                          {farmaceutico.status === "BLOQUEADO" ? "Desbloquear" : "Bloquear"}
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
                {farmaceuticos.length === 0 && (
                  <tr>
                    <td colSpan={7} className="px-6 py-14 text-center text-sm text-slate-500">
                      Nenhum farmacêutico encontrado!
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
          className="rounded-lg border border-slate-300 bg-white px-4 py-2.5 text-sm font-semibold text-slate-700 shadow-sm transition-all duration-200 hover:border-emerald-300 hover:bg-emerald-50 hover:text-emerald-700 focus:outline-none focus:ring-2 focus:ring-emerald-400 focus:ring-offset-2"
        >
          Voltar para Home
        </button>
      </div>
    </main>
  );
}
