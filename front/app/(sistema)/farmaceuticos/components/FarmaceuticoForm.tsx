"use client";

import {
  atualizarFarmaceutico,
  salvarFarmaceutico,
} from "@/app/services/farmaceuticoService";
import { Farmaceutico, FarmaceuticoFormProps } from "@/app/types/farmaceuticos";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";

export default function FarmaceuticoForm({ farmaceuticoExistente }: FarmaceuticoFormProps) {
  const router = useRouter();
  const [farmaceutico, setFarmaceutico] = useState<Farmaceutico>(
    farmaceuticoExistente
      ? new Farmaceutico(
          farmaceuticoExistente.id,
          farmaceuticoExistente.nome,
          farmaceuticoExistente.cpf,
          farmaceuticoExistente.telefone || "",
          farmaceuticoExistente.email,
          "",
          farmaceuticoExistente.status
        )
      : new Farmaceutico(null, "", "", "", "", "", "ATIVO")
  );

  const atualizar = (campo: "nome" | "cpf" | "telefone" | "email" | "senha", valor: string) => {
    setFarmaceutico((anterior) => new Farmaceutico(
      anterior.id,
      campo === "nome" ? valor : anterior.nome,
      campo === "cpf" ? valor : anterior.cpf,
      campo === "telefone" ? valor : anterior.telefone,
      campo === "email" ? valor : anterior.email,
      campo === "senha" ? valor : anterior.senha,
      anterior.status
    ));
  };

  const handlerSalvar = async () => {
    try {
      const status = farmaceuticoExistente
        ? await atualizarFarmaceutico(farmaceutico)
        : await salvarFarmaceutico(farmaceutico);

      if (status !== 200) {
        alert("Erro ao salvar farmacêutico!");
        return;
      }

      alert("Farmacêutico salvo com sucesso!");
      router.push("/farmaceuticos");
    } catch {
      alert("Erro ao salvar farmacêutico. Verifique os dados informados.");
    }
  };

  return (
    <form action={handlerSalvar} className="mx-auto max-w-2xl">
      <div className="space-y-5 rounded-2xl border border-teal-100 bg-white p-6 shadow-md">
        <div className="space-y-2">
          <label className="block text-sm font-medium text-teal-700">Nome completo:</label>
          <input
            name="nome"
            value={farmaceutico.nome}
            required
            onChange={(e) => atualizar("nome", e.target.value)}
            className="w-full rounded-lg border border-teal-200 bg-white px-4 py-2.5 text-gray-800 focus:border-teal-500 focus:outline-none focus:ring-2 focus:ring-teal-500"
          />
        </div>
        <div className="space-y-2">
          <label className="block text-sm font-medium text-teal-700">CPF:</label>
          <input
            name="cpf"
            value={farmaceutico.cpf}
            required
            onChange={(e) => atualizar("cpf", e.target.value)}
            placeholder="000.000.000-00"
            className="w-full rounded-lg border border-teal-200 bg-white px-4 py-2.5 text-gray-800 focus:border-teal-500 focus:outline-none focus:ring-2 focus:ring-teal-500"
          />
        </div>
        <div className="space-y-2">
          <label className="block text-sm font-medium text-teal-700">Telefone:</label>
          <input
            name="telefone"
            type="tel"
            value={farmaceutico.telefone}
            onChange={(e) => atualizar("telefone", e.target.value)}
            className="w-full rounded-lg border border-teal-200 bg-white px-4 py-2.5 text-gray-800 focus:border-teal-500 focus:outline-none focus:ring-2 focus:ring-teal-500"
          />
        </div>
        <div className="space-y-2">
          <label className="block text-sm font-medium text-teal-700">E-mail:</label>
          <input
            name="email"
            type="email"
            value={farmaceutico.email}
            required
            onChange={(e) => atualizar("email", e.target.value)}
            className="w-full rounded-lg border border-teal-200 bg-white px-4 py-2.5 text-gray-800 focus:border-teal-500 focus:outline-none focus:ring-2 focus:ring-teal-500"
          />
        </div>
        <div className="space-y-2">
          <label className="block text-sm font-medium text-teal-700">
            Senha{farmaceuticoExistente ? " (preencha somente para alterar)" : ":"}
          </label>
          <input
            name="senha"
            type="password"
            value={farmaceutico.senha}
            required={!farmaceuticoExistente}
            onChange={(e) => atualizar("senha", e.target.value)}
            className="w-full rounded-lg border border-teal-200 bg-white px-4 py-2.5 text-gray-800 focus:border-teal-500 focus:outline-none focus:ring-2 focus:ring-teal-500"
          />
        </div>
        <div className="flex items-center justify-between border-t border-teal-50 pt-4">
          <Link
            href="/farmaceuticos"
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
