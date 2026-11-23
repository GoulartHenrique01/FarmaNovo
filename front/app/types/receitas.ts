export interface ReceitaMedicamento {
    id?: number | null;
    medicamentoId: number | null;
    medicamentoNome?: string | null;
    dosagemPrescrita: string;
    posologia: string;
    quantidadePrescrita: number | null;
    duracaoTratamento: string;
}

export interface Receita {
    id: number | null;
    dataEmissao: string;
    dataValidade: string;
    diagnostico: string;
    observacoes: string;
    status: string;
    pacienteId: number | null;
    pacienteNome?: string | null;
    medicamentos: ReceitaMedicamento[];
}

export interface ReceitaFormProps {
    receitaExistente?: Receita;
}
