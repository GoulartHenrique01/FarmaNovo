import { Receita } from "../types/receitas";
import api from "./api";

 type ReceitaRequest = Pick<
    Receita,
    "dataEmissao" | "dataValidade" | "diagnostico" | "observacoes" | "pacienteId" | "medicamentos"
>;

// FLUXO: funções responsáveis pela comunicação entre as telas de receitas e a API REST.
export async function buscarListaReceitas(): Promise<Receita[]> {
    const dados = await api.get<Receita[]>("/receitas");
    return dados.data;
}

export async function deletarReceita(id: number): Promise<number> {
    const dados = await api.delete(`/receitas/${id}/excluir`);
    return dados.status;
}

export async function consultaReceitaPorId(id: number): Promise<Receita> {
    const dados = await api.get<Receita>(`/receitas/${id}`);
    return dados.data;
}

export async function atualizarReceita(receita: Receita): Promise<number> {
    const dados = await api.put(`/receitas/${receita.id}`, requestReceita(receita));
    return dados.status;
}

export async function salvarReceita(receita: Receita): Promise<number> {
    const dados = await api.post("/receitas", requestReceita(receita));
    return dados.status;
}

// FLUXO: transforma o estado do formulário no DTO esperado pelo backend, enviando somente os IDs dos cadastros existentes.
function requestReceita(receita: Receita): ReceitaRequest {
    return {
        dataEmissao: receita.dataEmissao,
        dataValidade: receita.dataValidade,
        diagnostico: receita.diagnostico,
        observacoes: receita.observacoes,
        pacienteId: receita.pacienteId,
        medicamentos: receita.medicamentos.map((item) => ({
            medicamentoId: item.medicamentoId,
            dosagemPrescrita: item.dosagemPrescrita,
            posologia: item.posologia,
            quantidadePrescrita: item.quantidadePrescrita,
            duracaoTratamento: item.duracaoTratamento,
        })),
    };
}
