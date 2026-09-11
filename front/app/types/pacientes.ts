export class Paciente {
    constructor(
        public id: number,
        public nome: string,
        public cpf: string,
        public dataNascimento: string,
        public sexo: string,
        public telefone: string,
        public email: string,
        public observacoes: string,
        public status: string
    )
    {

    }
  }