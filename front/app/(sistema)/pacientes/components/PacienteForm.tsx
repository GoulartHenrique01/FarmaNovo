"use client";

import { Paciente, PacienteFormProps } from "@/app/types/pacientes";
import axios from "axios";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";

// Formulário reutilizável para cadastrar ou editar um paciente.
export default function PacienteForm({ pacienteExistente }: PacienteFormProps) {
  // Navegação para a listagem após a gravação bem-sucedida.
  const router = useRouter();
  // Inicia com o registro recebido na edição ou com campos vazios no cadastro.
  const [paciente, setPaciente] = useState<Paciente>(pacienteExistente || new Paciente(null, "", "", "", "", "", "", "", "", "ATIVO"));
  // Atualiza somente o campo informado, preservando os demais valores do objeto.
  const atualizar = (campo: keyof Paciente, valor: string) => setPaciente((anterior) => ({ ...anterior, [campo]: valor }));
  // Escolhe POST ou PUT conforme o modo do formulário e trata sucesso ou falha.
  const salvar = async () => {
    try {
      if (pacienteExistente) await axios.put(`http://localhost:8080/pacientes/${paciente.id}`, paciente);
      else await axios.post("http://localhost:8080/pacientes", paciente);
      alert("Paciente salvo com sucesso");
      router.push("/pacientes");
    } catch (error) { alert("Erro ao salvar paciente"); }
  };

  return (
    <form action={salvar} className="max-w-2xl mx-auto">
      <div className="bg-white rounded-2xl shadow-md border border-teal-100 p-6 space-y-5">
        <div className="space-y-2">
          <label className="block text-sm font-medium text-teal-700">
            Nome Completo:
          </label>
          <input
            name="nome"
            value={paciente.nome}
            required
            onChange={(e) => atualizar("nome", e.target.value)}
            className="w-full px-4 py-2.5 border border-teal-200 rounded-lg bg-white text-gray-800 focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500 transition"
          />
        </div>

        <div className="space-y-2">
          <label className="block text-sm font-medium text-teal-700">
            CPF:
          </label>
          <input
            name="cpf"
            value={paciente.cpf}
            required
            onChange={(e) => atualizar("cpf", e.target.value)}
            className="w-full px-4 py-2.5 border border-teal-200 rounded-lg bg-white text-gray-800 focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500 transition"
          />
        </div>

        <div className="space-y-2">
          <label className="block text-sm font-medium text-teal-700">
            Data de Nascimento:
          </label>
          <input
            type="date"
            name="dataNascimento"
            value={paciente.dataNascimento}
            required
            onChange={(e) => atualizar("dataNascimento", e.target.value)}
            className="w-full px-4 py-2.5 border border-teal-200 rounded-lg bg-white text-gray-800 focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500 transition"
          />
        </div>

        <div className="space-y-2">
          <label className="block text-sm font-medium text-teal-700">
            Sexo:
          </label>
          <input
            name="sexo"
            value={paciente.sexo}
            required
            onChange={(e) => atualizar("sexo", e.target.value)}
            className="w-full px-4 py-2.5 border border-teal-200 rounded-lg bg-white text-gray-800 focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500 transition"
          />
        </div>

        <div className="space-y-2">
          <label className="block text-sm font-medium text-teal-700">
            Telefone:
          </label>
          <input
            name="telefone"
            value={paciente.telefone}
            onChange={(e) => atualizar("telefone", e.target.value)}
            className="w-full px-4 py-2.5 border border-teal-200 rounded-lg bg-white text-gray-800 focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500 transition"
          />
        </div>

        <div className="space-y-2">
          <label className="block text-sm font-medium text-teal-700">
            E-mail:
          </label>
          <input
            type="email"
            name="email"
            value={paciente.email}
            required
            onChange={(e) => atualizar("email", e.target.value)}
            className="w-full px-4 py-2.5 border border-teal-200 rounded-lg bg-white text-gray-800 focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500 transition"
          />
        </div>

        <div className="space-y-2">
          <label className="block text-sm font-medium text-teal-700">
            Alergias:
          </label>
          <input
            name="alergias"
            value={paciente.alergias}
            onChange={(e) => atualizar("alergias", e.target.value)}
            className="w-full px-4 py-2.5 border border-teal-200 rounded-lg bg-white text-gray-800 focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500 transition"
          />
        </div>

        <div className="space-y-2">
          <label className="block text-sm font-medium text-teal-700">
            Observações:
          </label>
          <input
            name="observacoes"
            value={paciente.observacoes}
            onChange={(e) => atualizar("observacoes", e.target.value)}
            className="w-full px-4 py-2.5 border border-teal-200 rounded-lg bg-white text-gray-800 focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500 transition"
          />
        </div>

        <div className="space-y-2">
          <label className="block text-sm font-medium text-teal-700">
            Status:
          </label>
          <select
            name="status"
            value={paciente.status}
            onChange={(e) => atualizar("status", e.target.value)}
            className="w-full px-4 py-2.5 border border-teal-200 rounded-lg bg-white text-gray-800 focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500 transition"
          >
            <option value="ATIVO">ATIVO</option>
            <option value="BLOQUEADO">BLOQUEADO</option>
            <option value="EXCLUIDO">EXCLUIDO</option>
          </select>
        </div>

        <div className="flex items-center justify-between pt-4 border-t border-teal-50">
          <Link
            href="/pacientes"
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
