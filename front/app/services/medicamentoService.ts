import { Medicamento } from "../types/medicamentos";
import api from "./api";

export async function buscarListaMedicamentos() : Promise<Medicamento[]> {
    const dados = await api.get<Medicamento[]>(
        "http://localhost:8080/medicamentos"
    );

    if(dados.status == 200){
        return dados.data;
    }
    return [];
}

export async function deletarMedicamento(id:number) : Promise<number>{
    var dados = await api.delete('http://localhost:8080/medicamentos/' + id + '/excluir');

    return dados.status;
}

export async function consultaMedicamentoPorId(id:Number) : Promise<Medicamento>{
    const valorMedicamentoBack = await api.get<Medicamento>('http://localhost:8080/medicamentos/' + id);
    return valorMedicamentoBack.data;
}

export async function atualizarMedicamento(medicamento:Medicamento) : Promise<number>{
    var dadosRetorno = await api.put<number>(
        "http://localhost:8080/medicamentos/" + medicamento.id,
        medicamento);
    return dadosRetorno.data;
}

export async function salvarMedicamento(medicamento:Medicamento) : Promise<number>{
    var dadosRetorno = await api.post<number>(
        "http://localhost:8080/medicamentos",
        medicamento);
    return dadosRetorno.data;
}