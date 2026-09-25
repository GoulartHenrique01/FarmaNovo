"use client";

import { Usuario } from "@/app/types/usuario";
import axios from "axios";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

export default function Usuarios() {
  const router = useRouter();

  const [usuarios, setUsuarios] = useState<Usuario[]>([]);

  useEffect(() => {
    carregarDados();
  }, []);

  const carregarDados = async () => {
    try {
      const dados = await axios.get<Usuario[]>(
        "http://localhost:8080/usuarios"
      );

      setUsuarios(dados.data);
    } catch (error) {
      alert("Erro ao carregar usuarios!");
    }
  };

  const handlerDeletarUsuario = async (usuario: Usuario) => {
    var dadosRetorno = await axios.delete(
      "http://localhost:8080/usuarios/" + usuario.id + "/excluir"
    );

    if (dadosRetorno.status == 200) {
      alert("Usuário excluido com sucesso");
    } else {
      alert(dadosRetorno.data);
      return;
    }
    carregarDados();
  };

  // Troca o status entre ATIVO e BLOQUEADO
  const handleAlterarStatusUsuario = async (usuario: Usuario) => {
    var novoStatus = {};

    if (usuario.status === "ATIVO") {
      novoStatus = { status: "BLOQUEADO" };
    } else {
      novoStatus = { status: "ATIVO" };
    }

    var dadosRetorno = await axios.patch(
      "http://localhost:8080/usuarios/" + usuario.id + "/status",
      novoStatus
    );

    if (dadosRetorno.status == 200) {
      alert("Status atualizado com sucesso!");
    } else {
      alert(dadosRetorno.data);
      return;
    }

    // Atualiza a lista com o novo status
    carregarDados();
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
              Usuários
            </h1>

            <p className="mt-1 text-sm text-slate-500">
              Acompanhe os usuários cadastrados no sistema.
            </p>
          </div>

          <button
            type="button"
            onClick={() => router.push("/usuarios/novo")}
            className="rounded-lg bg-emerald-600 px-4 py-2.5 text-sm font-semibold text-white shadow-sm transition-all duration-200 hover:bg-emerald-700 hover:shadow-md focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:ring-offset-2"
          >
            + Novo Usuário
          </button>
        </div>

        <div className="overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-[0_12px_35px_rgba(15,118,110,0.08)]">
          <div className="overflow-x-auto">
            <table className="w-full min-w-[760px] border-collapse">
              <thead>
                <tr className="border-b border-slate-200 bg-slate-50">
                  <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-slate-500">
                    Código
                  </th>

                  <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-slate-500">
                    Nome
                  </th>

                  <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-slate-500">
                    CPF
                  </th>

                  <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-slate-500">
                    Email
                  </th>

                  <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-slate-500">
                    Status
                  </th>

                  <th className="px-5 py-4 text-left text-xs font-bold uppercase tracking-wide text-slate-500">
                    Ações
                  </th>
                </tr>
              </thead>

              <tbody className="divide-y divide-slate-100">
                {usuarios.map((usuario) => (
                  <tr
                    key={usuario.id}
                    className="transition-colors duration-150 hover:bg-emerald-50/40"
                  >
                    <td className="px-5 py-4 text-sm font-semibold text-slate-700">
                      {usuario.id}
                    </td>

                    <td className="px-5 py-4 text-sm font-medium text-slate-800">
                      {usuario.nome}
                    </td>

                    <td className="px-5 py-4 text-sm text-slate-600">
                      {usuario.cpf}
                    </td>

                    <td className="px-5 py-4 text-sm text-slate-600">
                      {usuario.email}
                    </td>

                    <td className="px-5 py-4 text-sm">
                      <span
                        className={
                          usuario.status === "BLOQUEADO"
                            ? "inline-flex rounded-full bg-orange-50 px-2.5 py-1 text-xs font-semibold text-orange-700 ring-1 ring-orange-200"
                            : "inline-flex rounded-full bg-emerald-50 px-2.5 py-1 text-xs font-semibold text-emerald-700 ring-1 ring-emerald-200"
                        }
                      >
                        {usuario.status}
                      </span>
                    </td>

                    <td className="px-5 py-4">
                      <div className="flex flex-wrap items-center gap-2">
                        <Link
                          href={`/usuarios/${usuario.id}/editar`}
                          className="rounded-md border border-emerald-200 bg-emerald-50 px-3 py-1.5 text-xs font-semibold text-emerald-700 transition-all duration-200 hover:border-emerald-300 hover:bg-emerald-100 hover:text-emerald-800"
                        >
                          Editar
                        </Link>

                        <button
                          onClick={() => handlerDeletarUsuario(usuario)}
                          className="rounded-md border border-orange-200 bg-orange-50 px-3 py-1.5 text-xs font-semibold text-orange-700 transition-all duration-200 hover:border-orange-300 hover:bg-orange-100 hover:text-orange-800 focus:outline-none focus:ring-2 focus:ring-orange-400 focus:ring-offset-1"
                        >
                          Deletar
                        </button>

                        <button
                          onClick={() => handleAlterarStatusUsuario(usuario)}
                          className={
                            usuario.status === "BLOQUEADO"
                              ? "rounded-md border border-orange-200 bg-orange-50 px-3 py-1.5 text-xs font-semibold text-orange-700 transition-all duration-200 hover:border-orange-300 hover:bg-orange-100 hover:text-orange-800 focus:outline-none focus:ring-2 focus:ring-orange-400 focus:ring-offset-1"
                              : "rounded-md border border-emerald-200 bg-emerald-50 px-3 py-1.5 text-xs font-semibold text-emerald-700 transition-all duration-200 hover:border-emerald-300 hover:bg-emerald-100 hover:text-emerald-800 focus:outline-none focus:ring-2 focus:ring-emerald-400 focus:ring-offset-1"
                          }
                        >
                          {usuario.status === "BLOQUEADO"
                            ? "Desbloquear"
                            : "Bloquear"}
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}

                {usuarios.length === 0 && (
                  <tr>
                    <td
                      colSpan={6}
                      className="px-6 py-14 text-center text-sm text-slate-500"
                    >
                      Nenhum usuario encontrado!
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
          className="rounded-lg border border-slate-300 bg-white px-4 py-2.5 text-sm font-semibold text-slate-700 transition-all duration-200 hover:border-emerald-300 hover:bg-emerald-50 hover:text-emerald-700 focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:ring-offset-2"
        >
          ← Voltar para início
        </button>
      </div>
    </main>
  );
}
