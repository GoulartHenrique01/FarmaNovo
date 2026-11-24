"use client";

import { Receita, ReceitaFormProps, ReceitaMedicamento } from "@/app/types/receitas";
import { atualizarReceita, salvarReceita } from "@/app/services/receitaService";
import { buscarListaPacientes } from "@/app/services/pacienteService";
import { buscarListaMedicamentos } from "@/app/services/medicamentoService";
import { Paciente } from "@/app/types/pacientes";
import { Medicamento } from "@/app/types/medicamentos";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { RootState } from "@/app/redux/store";

const receitaVazia: Receita = {
  id: null,
  dataEmissao: "",
  dataValidade: "",
  diagnostico: "",
  observacoes: "",
  status: "ATIVO",
  pacienteId: null,
  medicamentos: [],
};

// FLUXO DO FORMULÁRIO: carrega pacientes e medicamentos existentes; a seleção é enviada como IDs no ReceitaRequest.
// O backend valida os vínculos e grava Receita + ReceitaMedicamento em uma transação.
export default function ReceitaForm({ receitaExistente }: ReceitaFormProps) {
  const router = useRouter();
  const identidade = useSelector((state: RootState) => state.auth.usuario);
  const podeGerenciar = identidade !== null && identidade.role === "ROLE_USER" && identidade.empresaId !== null;

  const [receita, setReceita] = useState<Receita>(receitaExistente || receitaVazia);
  const [pacientes, setPacientes] = useState<Paciente[]>([]);
  const [medicamentos, setMedicamentos] = useState<Medicamento[]>([]);
  const [erroListas, setErroListas] = useState("");

  // FLUXO: ao abrir o formulário, busca na API os pacientes e medicamentos ativos da empresa autenticada.
  useEffect(() => {
    if (!podeGerenciar) return;
    let ativo = true;
    Promise.all([buscarListaPacientes(), buscarListaMedicamentos()])
      .then(([listaPacientes, listaMedicamentos]) => {
        if (!ativo) return;
        setPacientes(listaPacientes.filter((item) => item.status === "ATIVO"));
        setMedicamentos(listaMedicamentos.filter((item) => item.status === "ATIVO"));
      })
      .catch(() => {
        if (ativo) setErroListas("Não foi possível carregar os pacientes e medicamentos da empresa.");
      });
    return () => { ativo = false; };
  }, [podeGerenciar]);

  useEffect(() => {
    if (receitaExistente) setReceita(receitaExistente);
  }, [receitaExistente]);

  const handlerCampo = (campo: "dataEmissao" | "dataValidade" | "diagnostico" | "observacoes", valor: string) => {
    setReceita((anterior) => ({ ...anterior, [campo]: valor }));
  };

  // Marca ou desmarca um medicamento que já existe no cadastro da empresa.
  // FLUXO: cada checkbox adiciona ou remove da receita um medicamento já cadastrado; nenhum medicamento é criado aqui.
  const selecionarMedicamento = (medicamento: Medicamento, selecionado: boolean) => {
    setReceita((anterior) => {
      if (selecionado) {
        if (anterior.medicamentos.some((item) => item.medicamentoId === medicamento.id)) return anterior;
        const novoItem: ReceitaMedicamento = {
          medicamentoId: medicamento.id,
          medicamentoNome: medicamento.nome,
          dosagemPrescrita: `${medicamento.dosagem} ${medicamento.unidadeDosagem}`.trim(),
          posologia: "",
          quantidadePrescrita: null,
          duracaoTratamento: "",
        };
        return { ...anterior, medicamentos: [...anterior.medicamentos, novoItem] };
      }
      return {
        ...anterior,
        medicamentos: anterior.medicamentos.filter((item) => item.medicamentoId !== medicamento.id),
      };
    });
  };

  const atualizarItem = (medicamentoId: number | null, campo: keyof ReceitaMedicamento, valor: string) => {
    setReceita((anterior) => ({
      ...anterior,
      medicamentos: anterior.medicamentos.map((item) => {
        if (item.medicamentoId !== medicamentoId) return item;
        if (campo === "quantidadePrescrita") {
          return { ...item, quantidadePrescrita: valor ? Number(valor) : null };
        }
        return { ...item, [campo]: valor };
      }),
    }));
  };

  // FLUXO: envia os dados da receita e os IDs dos medicamentos selecionados; após sucesso, retorna à listagem.
  // Decide entre POST e PUT; receitaService converte os itens selecionados em IDs para o backend validar e persistir.
  const handlerSalvar = async () => {
    try {
      const status = receitaExistente
        ? await atualizarReceita(receita)
        : await salvarReceita(receita);
      if (status !== 200 && status !== 201) {
        alert("Erro ao salvar receita!");
        return;
      }
      alert("Receita salva com sucesso!");
      router.push("/receitas");
    } catch (erro) {
      const mensagem = (erro as { response?: { data?: string } })?.response?.data;
      alert(typeof mensagem === "string" ? mensagem : "Erro ao salvar receita. Verifique os dados informados.");
    }
  };

  return (
    <form action={handlerSalvar} className="mx-auto max-w-3xl">
      <div className="space-y-5 rounded-2xl border border-teal-100 bg-white p-6 shadow-md">
        <div className="space-y-2">
          <label className="block text-sm font-medium text-teal-700" htmlFor="dataEmissao">Data de Emissão</label>
          <input id="dataEmissao" value={receita.dataEmissao} required type="date" onChange={(e) => handlerCampo("dataEmissao", e.target.value)} className="w-full rounded-lg border border-teal-200 bg-white px-4 py-2.5 text-gray-800" />
        </div>

        <div className="space-y-2">
          <label htmlFor="pacienteId" className="block text-sm font-medium text-teal-700">Paciente</label>
          <select id="pacienteId" value={receita.pacienteId ?? ""} required onChange={(e) => setReceita((a) => ({ ...a, pacienteId: e.target.value ? Number(e.target.value) : null }))} className="w-full rounded-lg border border-teal-200 bg-white px-4 py-2.5 text-gray-800">
            <option value="">Selecione um paciente</option>
            {pacientes.map((paciente) => <option key={paciente.id} value={paciente.id ?? ""}>{paciente.nome}</option>)}
          </select>
        </div>

        <div className="space-y-3 border-t border-teal-100 pt-4">
          <div>
            <h3 className="font-semibold text-teal-800">Selecione os medicamentos</h3>
            <p className="mt-1 text-sm text-slate-500">Marque um ou mais medicamentos já cadastrados na tabela de medicamentos da sua empresa.</p>
          </div>

          <div className="divide-y divide-teal-100 rounded-xl border border-teal-100">
            {medicamentos.map((medicamento) => {
              const selecionado = receita.medicamentos.some((item) => item.medicamentoId === medicamento.id);
              return (
                <label key={medicamento.id} className={`flex cursor-pointer items-center gap-3 p-4 transition ${selecionado ? "bg-teal-50" : "bg-white hover:bg-slate-50"}`}>
                  <input
                    type="checkbox"
                    checked={selecionado}
                    onChange={(e) => selecionarMedicamento(medicamento, e.target.checked)}
                    className="h-4 w-4 rounded border-teal-300 text-teal-700 focus:ring-teal-600"
                  />
                  <span className="min-w-0 flex-1">
                    <span className="block font-semibold text-slate-800">{medicamento.nome}</span>
                    <span className="block text-sm text-slate-500">{medicamento.dosagem} {medicamento.unidadeDosagem} · {medicamento.marca || "Sem marca informada"}</span>
                  </span>
                  <span className="text-xs text-slate-500">Estoque: {medicamento.quantidade}</span>
                </label>
              );
            })}
            {medicamentos.length === 0 && <p className="p-4 text-sm text-amber-700">Nenhum medicamento ativo cadastrado. Cadastre medicamentos antes de emitir uma receita.</p>}
          </div>

          {receita.medicamentos.length > 0 && (
            <div className="space-y-3 pt-2">
              <h4 className="font-semibold text-teal-800">Prescrição dos medicamentos selecionados</h4>
              {receita.medicamentos.map((item, indice) => (
                <div key={item.medicamentoId ?? indice} className="space-y-3 rounded-xl border border-teal-100 bg-teal-50/40 p-4">
                  <p className="font-semibold text-slate-800">{indice + 1}. {item.medicamentoNome || medicamentos.find((m) => m.id === item.medicamentoId)?.nome || "Medicamento selecionado"}</p>
                  <div className="grid gap-3 sm:grid-cols-2">
                    <div className="space-y-1">
                      <label className="text-sm font-medium text-teal-700">Dosagem prescrita</label>
                      <input required value={item.dosagemPrescrita} onChange={(e) => atualizarItem(item.medicamentoId, "dosagemPrescrita", e.target.value)} placeholder="Ex.: 500 mg" className="w-full rounded-lg border border-teal-200 bg-white px-3 py-2.5 text-gray-800" />
                    </div>
                    <div className="space-y-1">
                      <label className="text-sm font-medium text-teal-700">Quantidade prescrita</label>
                      <input required min="1" type="number" value={item.quantidadePrescrita ?? ""} onChange={(e) => atualizarItem(item.medicamentoId, "quantidadePrescrita", e.target.value)} placeholder="Ex.: 20" className="w-full rounded-lg border border-teal-200 bg-white px-3 py-2.5 text-gray-800" />
                    </div>
                    <div className="space-y-1">
                      <label className="text-sm font-medium text-teal-700">Posologia</label>
                      <input required value={item.posologia} onChange={(e) => atualizarItem(item.medicamentoId, "posologia", e.target.value)} placeholder="Ex.: 1 comprimido a cada 8 horas" className="w-full rounded-lg border border-teal-200 bg-white px-3 py-2.5 text-gray-800" />
                    </div>
                    <div className="space-y-1">
                      <label className="text-sm font-medium text-teal-700">Duração do tratamento</label>
                      <input required value={item.duracaoTratamento} onChange={(e) => atualizarItem(item.medicamentoId, "duracaoTratamento", e.target.value)} placeholder="Ex.: 7 dias" className="w-full rounded-lg border border-teal-200 bg-white px-3 py-2.5 text-gray-800" />
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
          {receita.medicamentos.length === 0 && <p className="rounded-lg bg-amber-50 p-3 text-sm text-amber-800">Marque pelo menos um medicamento para incluir na receita.</p>}
        </div>

        {erroListas && <p className="text-sm text-red-600">{erroListas}</p>}
        {!erroListas && pacientes.length === 0 && <p className="text-sm text-amber-700">Cadastre um paciente ativo antes de emitir uma receita.</p>}

        <div className="space-y-2"><label className="block text-sm font-medium text-teal-700" htmlFor="dataValidade">Data de Validade</label><input id="dataValidade" value={receita.dataValidade} required type="date" onChange={(e) => handlerCampo("dataValidade", e.target.value)} className="w-full rounded-lg border border-teal-200 bg-white px-4 py-2.5 text-gray-800" /></div>
        <div className="space-y-2"><label className="block text-sm font-medium text-teal-700" htmlFor="diagnostico">Diagnóstico</label><input id="diagnostico" value={receita.diagnostico} required onChange={(e) => handlerCampo("diagnostico", e.target.value)} className="w-full rounded-lg border border-teal-200 bg-white px-4 py-2.5 text-gray-800" /></div>
        <div className="space-y-2"><label className="block text-sm font-medium text-teal-700" htmlFor="observacoes">Observações</label><input id="observacoes" value={receita.observacoes} onChange={(e) => handlerCampo("observacoes", e.target.value)} className="w-full rounded-lg border border-teal-200 bg-white px-4 py-2.5 text-gray-800" /></div>

        <div className="flex items-center justify-between border-t border-teal-50 pt-4">
          <Link href="/receitas" className="rounded-lg border border-red-500/40 bg-red-500/10 px-4 py-2 text-sm font-semibold text-red-600 transition hover:bg-red-500/20">Cancelar</Link>
          <button type="submit" disabled={!podeGerenciar || pacientes.length === 0 || medicamentos.length === 0 || receita.medicamentos.length === 0} className="rounded-lg bg-gradient-to-r from-teal-700 to-emerald-600 px-5 py-2.5 text-sm font-medium text-white shadow-sm transition hover:from-teal-800 hover:to-emerald-700 disabled:cursor-not-allowed disabled:opacity-50">Salvar</button>
        </div>
      </div>
    </form>
  );
}
