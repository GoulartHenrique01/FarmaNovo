"use client";

import { Medicamento, MedicamentoFormProps } from "@/app/types/medicamentos";
import axios from "axios";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";

// Formulário reutilizável para cadastrar ou editar um medicamento.
export default function MedicamentoForm({ medicamentoExistente}: MedicamentoFormProps) {
  const router = useRouter();

  const [medicamento, setMedicamento] = useState<Medicamento>(
    medicamentoExistente ||
      new Medicamento(
        null as unknown as number, "", "", "", "", "", "", "", "NAO", ""
)
  );

  const handlerChange = (
    campo:
      | "nome"
      | "tipo"
      | "dataValidade"
      | "dosagem"
      | "unidadeDosagem"
      | "quantidade"
      | "marca"
      | "precisaReceita",
    valor: string
  ) => {
    setMedicamento(
      (valorAnterior) =>
        new Medicamento(
          valorAnterior.id,
          campo === "nome" ? valor : valorAnterior.nome,
          campo === "tipo" ? valor : valorAnterior.tipo,
          campo === "dataValidade" ? valor : valorAnterior.dataValidade,
          campo === "dosagem" ? valor : valorAnterior.dosagem,
          campo === "unidadeDosagem" ? valor : valorAnterior.unidadeDosagem,
          campo === "quantidade" ? valor : valorAnterior.quantidade,
          campo === "marca" ? valor : valorAnterior.marca,
          campo === "precisaReceita" ? valor : valorAnterior.precisaReceita,
          valorAnterior.status
        )
    );
  };

  // Envia POST para cadastro ou PUT para edição e apresenta o resultado da operação.
  const handlerSalvar = async (formData : FormData) => {
    if (medicamentoExistente) {
      var dadosRetorno = await axios.put<number>(
        "http://localhost:8080/medicamentos/" + medicamento.id,
        medicamento
      );

      if (dadosRetorno.status == 200){
        alert("Medicamento salvo com sucesso");
      }else{
        alert(dadosRetorno.data);
        return;
      }
    }else{
      var dadosRetorno = await axios.post<number>(
        "http://localhost:8080/medicamentos", medicamento
        );

        if(dadosRetorno.status == 200){
          alert("Medicamento salvo com sucesso");
        }else{
          alert(dadosRetorno.data);
          return;
        }
    }

    router.push("/medicamentos")
  };

  return (
    <form action={handlerSalvar} className="max-w-2xl mx-auto">
      <div className="bg-white rounded-2xl shadow-md border border-teal-100 p-6 space-y-5">
        <div className="space-y-2">
          <label className="block text-sm font-medium text-teal-700">
            Nome:
          </label>
          <input
            name="nome"
            value={medicamento.nome}
            required
            onChange={(e) => handlerChange("nome", e.target.value)}
            className="w-full px-4 py-2.5 border border-teal-200 rounded-lg bg-white text-gray-800 focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500 transition"
          />
        </div>

        <div className="space-y-2">
          <label className="block text-sm font-medium text-teal-700">
            Tipo:
          </label>
          <input
            name="tipo"
            value={medicamento.tipo}
            required
            onChange={(e) => handlerChange("tipo", e.target.value)}
            className="w-full px-4 py-2.5 border border-teal-200 rounded-lg bg-white text-gray-800 focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500 transition"
          />
        </div>

        <div className="space-y-2">
          <label className="block text-sm font-medium text-teal-700">
            Data de Validade:
          </label>
          <input
            type="date"
            name="dataValidade"
            value={medicamento.dataValidade}
            required
            onChange={(e) => handlerChange("dataValidade", e.target.value)}
            className="w-full px-4 py-2.5 border border-teal-200 rounded-lg bg-white text-gray-800 focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500 transition"
          />
        </div>

        <div className="space-y-2">
          <label className="block text-sm font-medium text-teal-700">
            Dosagem:
          </label>
          <input
            type="number"
            step="0.01"
            name="dosagem"
            value={medicamento.dosagem}
            required
            onChange={(e) => handlerChange("dosagem", e.target.value)}
            className="w-full px-4 py-2.5 border border-teal-200 rounded-lg bg-white text-gray-800 focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500 transition"
          />
        </div>

        <div className="space-y-2">
          <label className="block text-sm font-medium text-teal-700">
            Unidade de Dosagem:
          </label>
          <input
            name="unidadeDosagem"
            value={medicamento.unidadeDosagem}
            required
            onChange={(e) => handlerChange("unidadeDosagem", e.target.value)}
            className="w-full px-4 py-2.5 border border-teal-200 rounded-lg bg-white text-gray-800 focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500 transition"
          />
        </div>

        <div className="space-y-2">
          <label className="block text-sm font-medium text-teal-700">
            Quantidade:
          </label>
          <input
            type="number"
            name="quantidade"
            value={medicamento.quantidade}
            required
            onChange={(e) => handlerChange("quantidade", e.target.value)}
            className="w-full px-4 py-2.5 border border-teal-200 rounded-lg bg-white text-gray-800 focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500 transition"
          />
        </div>

        <div className="space-y-2">
          <label className="block text-sm font-medium text-teal-700">
            Marca:
          </label>
          <input
            name="marca"
            value={medicamento.marca}
            required
            onChange={(e) => handlerChange("marca", e.target.value)}
            className="w-full px-4 py-2.5 border border-teal-200 rounded-lg bg-white text-gray-800 focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500 transition"
          />
        </div>

        <div className="flex items-center gap-3 rounded-xl border border-teal-100 bg-teal-50 px-4 py-3">
          <input
            type="checkbox"
            name="precisaReceita"
            value={medicamento.precisaReceita}
            onChange={(e) =>
              handlerChange("precisaReceita", e.target.checked ? "SIM" : "NAO")
            }
            className="h-4 w-4 rounded border-teal-300 text-teal-600 focus:ring-teal-500"
          />
          <label className="text-sm font-medium text-teal-700">
            Precisa de Receita
          </label>
        </div>

        <div className="flex items-center justify-between pt-4 border-t border-teal-50">
          <Link
            href="/medicamentos"
            className="px-5 py-2.5 text-sm font-medium text-teal-700 bg-teal-50 hover:bg-teal-100 rounded-lg transition-colors"
          >
            Cancelar
          </Link>
          <button
            type="submit"
            className="px-5 py-2.5 text-sm font-medium text-white bg-gradient-to-r from-teal-700 to-emerald-600 hover:from-teal-800 hover:to-emerald-700 rounded-lg shadow-sm transition-all duration-200"
          >
            Salvar
          </button>
        </div>
      </div>
    </form>
  );
}
