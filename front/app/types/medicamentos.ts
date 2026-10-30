// Modelo dos dados de estoque e das exigências de prescrição de um medicamento.
export class Medicamento {
    constructor(
        public id: number | null,
        public nome: string,
        public tipo: string,
        public dataValidade: string,
        public dosagem: number,
        public unidadeDosagem: string,
        public quantidade: number,
        public marca: string,
        public precisaReceita: string,
        public status: string
    ) {}
}

// Registro opcional usado para preencher o formulário durante uma edição.
export interface MedicamentoFormProps {
    medicamentoExistente?: Medicamento;
}