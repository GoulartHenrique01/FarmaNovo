export interface Empresa {
    id: number;
    razaoSocial: string;
    nomeFantasia: string;
    cnpj: string;
    status: "ATIVO" | "BLOQUEADO" | "EXCLUIDO";
    nomeResponsavel: string;
    cpfResponsavel: string | null;
    email: string;
    cep?: string;
    logradouro?: string;
    numero?: string;
    complemento?: string;
    bairro?: string;
    cidade?: string;
    uf?: string;
}

export interface EmpresaRequest {
    razaoSocial: string;
    nomeFantasia: string;
    cnpj: string;
    administradorNome: string;
    administradorEmail: string;
    administradorSenha?: string;
    administradorCpf: string;
    cep?: string;
    logradouro?: string;
    numero?: string;
    complemento?: string;
    bairro?: string;
    cidade?: string;
    uf?: string;
}
