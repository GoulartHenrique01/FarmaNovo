'use client'

import { Receita, ReceitaFormProps } from "@/app/types/receitas";
import axios from "axios";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";

// Formulário reutilizável para cadastrar ou editar uma receita médica.
export default function ReceitaForm({receitaExistente}:ReceitaFormProps) {
  // Permite voltar à listagem ao concluir o fluxo de gravação.
  const router = useRouter();

    // Armazena os valores dos campos; na edição, parte dos dados já existentes.
    const [receita, setReceita] = useState<Receita>(
        receitaExistente || 
        new Receita(null, "", "", "", "", "")
    );

    // Atualiza o campo escolhido criando uma nova instância com os demais valores preservados.
    const handlerChange = (
        campo: 'dataEmissao' | 'dataValidade' | 'diagnostico' | 'observacoes' | 'tipo',
        valor: string
    ) => {
        setReceita(valorAnterior =>
            new Receita(
                valorAnterior.id,
                campo === 'dataEmissao' ? valor : valorAnterior.dataEmissao,
                campo === 'dataValidade' ? valor : valorAnterior.dataValidade,
                campo === 'diagnostico' ? valor : valorAnterior.diagnostico,
                campo === 'observacoes' ? valor : valorAnterior.observacoes,
                campo === 'tipo' ? valor : valorAnterior.tipo
            )
        );
    }

    // Envia PUT para editar ou POST para cadastrar e informa o resultado da operação.
    // Os dados enviados vêm do estado controlado pelos campos do formulário.
    const hanlderSalvar = async (formData: FormData) => {

        if(receitaExistente){
            var dadosRetorno = await axios.put<number>('http://localhost:8080/receitas/' + receita.id, receita)

        if (dadosRetorno.status == 200) {
            alert("Receita salva com sucesso")
        } else {
            alert(dadosRetorno.data);
            return;
        }
            
        }else{

        var dadosRetorno = await axios.post<number>('http://localhost:8080/receitas', receita)

        if (dadosRetorno.status == 200) {
            alert("Receita salva com sucesso")
        } else {
            alert(dadosRetorno.data);
            return;
        }
        router.push("/receitas");

    }
}

  return (
    <form action={hanlderSalvar} className="max-w-2xl mx-auto">
      <div className="bg-white rounded-2xl shadow-md border border-teal-100 p-6 space-y-5">
        <div className="space-y-2">
          <label className="block text-sm font-medium text-teal-700">
            Data de Emissão
          </label>
          <input
            name = "dataEmissao"
            value={receita.dataEmissao}
            required
            type="date"
            onChange={(e) => handlerChange('dataEmissao', e.target.value)}
            className="w-full px-4 py-2.5 border border-teal-200 rounded-lg bg-white text-gray-800 focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500 transition"
          />
        </div>
        <div className="space-y-2">
          <label className="block text-sm font-medium text-teal-700">
            Data de Validade
          </label>
          <input
            name="dataValidade"
            value={receita.dataValidade}
            required
            type="date"
            onChange={(e) => handlerChange('dataValidade', e.target.value)}
            className="w-full px-4 py-2.5 border border-teal-200 rounded-lg bg-white text-gray-800 focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500 transition"
          />
        </div>
        <div className="space-y-2">
          <label className="block text-sm font-medium text-teal-700">
            Diagnóstico
          </label>
          <input
            name="diagnostico"
            value={receita.diagnostico}
            required
            onChange={(e) => handlerChange('diagnostico', e.target.value)}
            className="w-full px-4 py-2.5 border border-teal-200 rounded-lg bg-white text-gray-800 focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500 transition"
          />
        </div>
        <div className="space-y-2">
          <label className="block text-sm font-medium text-teal-700">
            Observações:
          </label>
        </div>
        <div className="space-y-2">
          <input
            name="observacoes"
            value={receita.observacoes}
            onChange={(e) => handlerChange('observacoes', e.target.value)}
            className="w-full px-4 py-2.5 border border-teal-200 rounded-lg bg-white text-gray-800 focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500 transition"
          />

          <label className="block text-sm font-medium text-teal-700">
            Tipo:
          </label>
          <input
            name="tipo"
            value={receita.tipo}
            onChange={(e) => handlerChange('tipo', e.target.value)}
            className="w-full px-4 py-2.5 border border-teal-200 rounded-lg bg-white text-gray-800 focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500 transition"
          />
        </div>

        <div className="flex items-center justify-between pt-4 border-t border-teal-50">
          <Link
            href="/receitas"
            className="rounded-lg border px-4 py-2 text-sm font-semibold transition-all duration-200 focus:outline-none focus:ring-2 border-red-500/40 bg-red-500/10 text-red-300 hover:bg-red-500/20 focus:ring-red-400"
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
