// Modelo das datas e informações clínicas registradas em uma receita.
export class Receita {

    constructor(
        public id: number | null,
        public dataEmissao: string,
        public dataValidade: string,
        public diagnostico: string,
        public observacoes: string,
        public tipo: string
    ) 
    {
        
    }
    }

// Receita opcional recebida pelo formulário quando o modo é edição.
export interface ReceitaFormProps{
    receitaExistente?:Receita
}