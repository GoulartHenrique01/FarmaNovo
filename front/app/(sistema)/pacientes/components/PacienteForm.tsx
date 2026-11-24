"use client";

import { atualizarPaciente, salvarPaciente } from "@/app/services/pacienteService";
import { Paciente, PacienteFormProps } from "@/app/types/pacientes";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";

export default function PacienteForm({ pacienteExistente }: PacienteFormProps) {
  const router = useRouter();
  const [paciente, setPaciente] = useState<Paciente>(
    pacienteExistente
      ? new Paciente(
          pacienteExistente.id,
          pacienteExistente.nome,
          pacienteExistente.cpf,
          pacienteExistente.telefone ?? "",
          pacienteExistente.status
        )
      : new Paciente(null, "", "", "", "ATIVO")
  );

  const atualizar = (campo: "nome" | "cpf" | "telefone", valor: string) => {
    setPaciente((anterior) => new Paciente(
      anterior.id,
      campo === "nome" ? valor : anterior.nome,
      campo === "cpf" ? valor : anterior.cpf,
      campo === "telefone" ? valor : anterior.telefone,
      anterior.status
    ));
  };

  // Decide entre POST (novo paciente) e PUT (paciente existente); após sucesso volta à listagem.
  const handlerSalvar = async () => {
    try {
      const status = pacienteExistente
        ? await atualizarPaciente(paciente)
        : await salvarPaciente(paciente);

      if (status !== 200) {
        alert("Erro ao salvar paciente!");
        return;
      }

      alert("Paciente salvo com sucesso!");
      router.push("/pacientes");
    } catch {
      alert("Erro ao salvar paciente. Verifique o nome e o CPF informado.");
    }
  };

  return (
    <form action={handlerSalvar} className="mx-auto max-w-2xl">
      <div className="space-y-5 rounded-2xl border border-teal-100 bg-white p-6 shadow-md">
        <div className="space-y-2">
          <label htmlFor="nome" className="block text-sm font-medium text-teal-700">
            Nome completo:
          </label>
          <input
            id="nome"
            name="nome"
            value={paciente.nome}
            required
            onChange={(e) => atualizar("nome", e.target.value)}
            className="w-full rounded-lg border border-teal-200 bg-white px-4 py-2.5 text-gray-800 focus:border-teal-500 focus:outline-none focus:ring-2 focus:ring-teal-500"
          />
        </div>
        <div className="space-y-2">
          <label htmlFor="cpf" className="block text-sm font-medium text-teal-700">
            CPF:
          </label>
          <input
            id="cpf"
            name="cpf"
            value={paciente.cpf}
            required
            placeholder="000.000.000-00"
            onChange={(e) => atualizar("cpf", e.target.value)}
            className="w-full rounded-lg border border-teal-200 bg-white px-4 py-2.5 text-gray-800 focus:border-teal-500 focus:outline-none focus:ring-2 focus:ring-teal-500"
          />
        </div>
        <div className="space-y-2">
          <label htmlFor="telefone" className="block text-sm font-medium text-teal-700">
            Telefone:
          </label>
          <input
            id="telefone"
            name="telefone"
            type="tel"
            value={paciente.telefone}
            onChange={(e) => atualizar("telefone", e.target.value)}
            className="w-full rounded-lg border border-teal-200 bg-white px-4 py-2.5 text-gray-800 focus:border-teal-500 focus:outline-none focus:ring-2 focus:ring-teal-500"
          />
        </div>
        <div className="flex items-center justify-between border-t border-teal-50 pt-4">
          <Link
            href="/pacientes"
            className="rounded-lg border border-red-500/40 bg-red-500/10 px-4 py-2 text-sm font-semibold text-red-700 transition hover:bg-red-500/20"
          >
            Cancelar
          </Link>
          <button
            type="submit"
            className="rounded-lg bg-gradient-to-r from-teal-700 to-emerald-600 px-5 py-2.5 text-sm font-medium text-white shadow-sm transition hover:from-teal-800 hover:to-emerald-700"
          >
            Salvar
          </button>
        </div>
      </div>
    </form>
  );
}
