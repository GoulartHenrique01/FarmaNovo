import { Medicamento } from "../types/medicamentos";
import api from "./api";

type MedicamentoRequest = Pick<
    Medicamento,
    | "nome"
    | "tipo"
    | "dataValidade"
    | "dosagem"
    | "unidadeDosagem"
    | "quantidade"
    | "marca"
    | "precisaReceita"
>;

export async function buscarListaMedicamentos() : Promise<Medicamento[]> {
    const dados = await api.get<Medicamento[]>("/medicamentos");

    if(dados.status === 200){
        return dados.data;
    }
    return [];
}

export async function deletarMedicamento(id:number) : Promise<number>{
    const dados = await api.delete(`/medicamentos/${id}/excluir`);

    return dados.status;
}

export async function consultaMedicamentoPorId(id:number) : Promise<Medicamento>{
    const valorMedicamentoBack = await api.get<Medicamento>(`/medicamentos/${id}`);
    return valorMedicamentoBack.data;
}

export async function atualizarMedicamento(medicamento:Medicamento) : Promise<number>{
    const dadosRetorno = await api.put<Medicamento>(
        `/medicamentos/${medicamento.id}`,
        requestMedicamento(medicamento));
    return dadosRetorno.status;
}

export async function salvarMedicamento(medicamento:Medicamento) : Promise<number>{
    const dadosRetorno = await api.post<Medicamento>(
        "/medicamentos",
        requestMedicamento(medicamento));
    return dadosRetorno.status;
}

function requestMedicamento(medicamento: Medicamento): MedicamentoRequest {
    return {
        nome: medicamento.nome,
        tipo: medicamento.tipo,
        dataValidade: medicamento.dataValidade,
        dosagem: medicamento.dosagem,
        unidadeDosagem: medicamento.unidadeDosagem,
        quantidade: medicamento.quantidade,
        marca: medicamento.marca,
        precisaReceita: medicamento.precisaReceita,
    };
}