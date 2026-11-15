import api from "./api";

export interface CadastroEmpresaRequest {
    razaoSocial: string;
    nomeFantasia: string;
    cnpj: string;
    administradorNome: string;
    administradorEmail: string;
    administradorSenha: string;
    administradorCpf: string;
}

export async function cadastrarEmpresa(empresa: CadastroEmpresaRequest): Promise<void> {
    await api.post("/empresas", empresa);
}
