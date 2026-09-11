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