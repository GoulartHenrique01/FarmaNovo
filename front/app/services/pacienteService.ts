import { Paciente } from "../types/pacientes";
import api from "./api";

// Camada de comunicação: envia somente nome, CPF e telefone; a empresa vem do JWT no backend.
type PacienteRequest = Pick<Paciente, "nome" | "cpf" | "telefone">;

export async function buscarListaPacientes(): Promise<Paciente[]> {
  const dados = await api.get<Paciente[]>("/pacientes");
  return dados.data;
}

export async function consultaPacientePorId(id: number): Promise<Paciente> {
  const dados = await api.get<Paciente>(`/pacientes/${id}`);
  return dados.data;
}

export async function salvarPaciente(paciente: Paciente): Promise<number> {
  const dados = await api.post("/pacientes", requestPaciente(paciente));
  return dados.status;
}

export async function atualizarPaciente(paciente: Paciente): Promise<number> {
  const dados = await api.put(`/pacientes/${paciente.id}`, requestPaciente(paciente));
  return dados.status;
}

export async function deletarPaciente(id: number): Promise<number> {
  const dados = await api.delete(`/pacientes/${id}/excluir`);
  return dados.status;
}

function requestPaciente(paciente: Paciente): PacienteRequest {
  return {
    nome: paciente.nome,
    cpf: paciente.cpf,
    telefone: paciente.telefone,
  };
}
