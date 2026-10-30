import { Farmaceutico } from "../types/farmaceuticos";
import api from "./api";

type FarmaceuticoRequest = Pick<Farmaceutico, "nome" | "email" | "senha" | "cpf" | "telefone">;

export async function buscarListaFarmaceuticos(): Promise<Farmaceutico[]> {
    const dados = await api.get<Farmaceutico[]>("/farmaceuticos");
    return dados.data;
}

export async function deletarFarmaceutico(id: number): Promise<number> {
    const dados = await api.delete(`/farmaceuticos/${id}/excluir`);
    return dados.status;
}

export async function atualizarStatusFarmaceutico(farmaceutico: Farmaceutico): Promise<number> {
    const status = farmaceutico.status === "ATIVO" ? "BLOQUEADO" : "ATIVO";
    const dados = await api.patch(`/farmaceuticos/${farmaceutico.id}/status`, { status });
    return dados.status;
}

export async function consultaFarmaceuticoPorId(id: number): Promise<Farmaceutico> {
    const dados = await api.get<Farmaceutico>(`/farmaceuticos/${id}`);
    return dados.data;
}

export async function atualizarFarmaceutico(farmaceutico: Farmaceutico): Promise<number> {
    const request: FarmaceuticoRequest = {
        nome: farmaceutico.nome,
        email: farmaceutico.email,
        senha: farmaceutico.senha,
        cpf: farmaceutico.cpf,
        telefone: farmaceutico.telefone,
    };
    const dados = await api.put(`/farmaceuticos/${farmaceutico.id}`, request);
    return dados.status;
}

export async function salvarFarmaceutico(farmaceutico: Farmaceutico): Promise<number> {
    const request: FarmaceuticoRequest = {
        nome: farmaceutico.nome,
        email: farmaceutico.email,
        senha: farmaceutico.senha,
        cpf: farmaceutico.cpf,
        telefone: farmaceutico.telefone,
    };
    const dados = await api.post("/farmaceuticos", request);
    return dados.status;
}
