export class Medicamento {
    constructor(
        public id: number,
        public nome: string,
        public tipo: string,
        public dataValidade: string,
        public dosagem: number,
        public unidadeDosagem: string,
        public quantidade: number,
        public marca: string,
        public precisaReceita: boolean
    ) {

    }
}