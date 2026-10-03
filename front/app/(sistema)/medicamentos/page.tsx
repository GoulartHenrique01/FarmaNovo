"use client";

import axios from "axios";
import { useEffect, useState } from "react";
import { Medicamento } from "@/app/types/medicamentos";
import { useRouter } from "next/navigation";
import Link from "next/link";

export default function Medicamentos() {
  const router = useRouter();

  const formatarData = (data: string) => {
    const dataNormalizada = data?.slice(0, 10);
    if (!dataNormalizada) return "-";

    const [ano, mes, dia] = dataNormalizada.split("-");
    return `${dia}/${mes}/${ano}`;
  };

  // Dados que alimentam a tabela de estoque.
  const [medicamentos, setMedicamentos] = useState<Medicamento[]>([]);

  // Carrega os medicamentos assim que a listagem é montada.
  useEffect(() => {
    carregarDados();
  }, []);

  // Busca a lista atualizada na API.
  const carregarDados = async () => {
    try {
      const dados = await axios.get<Medicamento[]>(
        "http://localhost:8080/medicamentos",
      );

      setMedicamentos(dados.data);
    } catch (error) {
      alert("Erro ao carregar medicamentos!");
    }
  };

  // Solicita confirmação antes da exclusão e atualiza a listagem ao concluir.
  const handlerDeletarMedicamento = async (medicamento: Medicamento) => {
    var dadosRetorno = await axios.delete(
      "http://localhost:8080/medicamentos/" + medicamento.id+ "/excluir"
    );

    if(dadosRetorno.status == 200){
      alert("Medicamento excluido com sucesso");
    }else{
      alert(dadosRetorno.data);
      return;
    }
    carregarDados();
  };


  // Exibe os medicamentos e as ações de manutenção de cada registro.
  return (
    <main className="min-h-screen bg-[#f4faf8] px-4 py-8 sm:px-6 lg:px-10">
      <div className="mx-auto max-w-7xl space-y-6">
        <div className="flex flex-col gap-4 sm:flex-row sm:items-end sm:justify-between">
          <div>
            <p className="mb-1 text-sm font-semibold uppercase tracking-[0.18em] text-emerald-700">
              Estoque
            </p>
            <h1 className="text-3xl font-bold tracking-tight text-slate-900">
              Medicamentos
            </h1>
            <p className="mt-1 text-sm text-slate-500">
              Consulte o estoque e as condições dos medicamentos.
            </p>
          </div>

          <button
            type="button"
            onClick={() => router.push("/medicamentos/novo")}
            className="rounded-lg bg-emerald-600 px-4 py-2.5 text-sm font-semibold text-white shadow-sm transition hover:bg-emerald-700 focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:ring-offset-2"
          >
            + Novo Medicamento
          </button>
        </div>

        <div className="overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-[0_12px_35px_rgba(15,118,110,0.08)]">
          <div className="overflow-x-auto">
            <table className="w-full min-w-[1100px] border-collapse">
              <thead>
                <tr className="border-b border-slate-200 bg-emerald-50/70">
                  <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-emerald-800">
                    ID
                  </th>
                  <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-emerald-800">
                    Nome
                  </th>
                  <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-emerald-800">
                    Tipo
                  </th>
                  <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-emerald-800">
                    Data de Validade
                  </th>
                  <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-emerald-800">
                    Dosagem
                  </th>
                  <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-emerald-800">
                    Unidade de Dosagem
                  </th>
                  <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-emerald-800">
                    Quantidade
                  </th>
                  <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-emerald-800">
                    Marca
                  </th>
                  <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-emerald-800">
                    Precisa de Receita
                  </th>
                  <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-emerald-800">
                    Ações
                  </th>
                </tr>
              </thead>

              <tbody className="divide-y divide-slate-100">
                {medicamentos.map((medicamento) => (
                  <tr
                    key={medicamento.id}
                    className="transition-colors hover:bg-emerald-50/40"
                  >
                    <td className="px-5 py-4 text-sm font-semibold text-slate-700">
                      {medicamento.id}
                    </td>

                    <td className="px-5 py-4 text-sm font-semibold text-slate-800">
                      {medicamento.nome}
                    </td>

                    <td className="px-5 py-4 text-sm text-slate-600">
                      {medicamento.tipo}
                    </td>

                    <td className="px-5 py-4 text-sm text-slate-600">
                      {formatarData(medicamento.dataValidade)}
                    </td>

                    <td className="px-5 py-4 text-sm text-slate-600">
                      {medicamento.dosagem}
                    </td>

                    <td className="px-5 py-4 text-sm text-slate-600">
                      {medicamento.unidadeDosagem}
                    </td>

                    <td className="px-5 py-4 text-sm font-semibold text-slate-700">
                      {medicamento.quantidade}
                    </td>

                    <td className="px-5 py-4 text-sm text-slate-600">
                      {medicamento.marca}
                    </td>

                    <td className="px-5 py-4 text-sm text-slate-600">
                      {medicamento.status}
                    </td>

                    <td className="px-5 py-4 text-sm text-slate-600">
                      <span
                        className={`inline-flex rounded-full px-2.5 py-1 text-xs font-semibold ${
                          medicamento.precisaReceita
                            ? "bg-amber-50 text-amber-700 ring-1 ring-amber-200"
                            : "bg-emerald-50 text-emerald-700 ring-1 ring-emerald-200"
                        }`}
                      >
                        {medicamento.precisaReceita ? "Sim" : "Não"}
                      </span>
                    </td>

                    <td className="px-5 py-4">
                      <div className="flex flex-wrap gap-2">
                        <Link
                          href={`/medicamentos/${medicamento.id}/editar`}
                          className="rounded-md border border-emerald-200 bg-emerald-50 px-3 py-1.5 text-xs font-semibold text-emerald-700 transition hover:border-emerald-300 hover:bg-emerald-100"
                        >
                          Editar
                        </Link>

                        <button
                          onClick={() => handlerDeletarMedicamento(medicamento)}
                          className="rounded-md border border-orange-200 bg-orange-50 px-3 py-1.5 text-xs font-semibold text-orange-700 transition hover:border-orange-300 hover:bg-orange-100"
                        >
                          Deletar
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}

                {medicamentos.length === 0 && (
                  <tr>
                    <td
                      colSpan={10}
                      className="px-6 py-14 text-center text-sm text-slate-500"
                    >
                      Nenhum medicamento encontrado!
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
          className="rounded-lg border border-slate-300 bg-white px-4 py-2.5 text-sm font-semibold text-slate-700 shadow-sm transition hover:border-emerald-300 hover:bg-emerald-50 hover:text-emerald-700 focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:ring-offset-2"
        >
           ← Voltar para início
        </button>
      </div>
    </main>
  );
}
