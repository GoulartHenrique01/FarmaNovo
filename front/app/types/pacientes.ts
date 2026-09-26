export class Paciente {
    constructor(
        public id: number | null,
        public nome: string,
        public cpf: string,
        public dataNascimento: string,
        public sexo: string,
        public telefone: string,
        public email: string,
        public alergias: string,
        public observacoes: string,
        public status: string
    )
    {

    }
  }

export interface PacienteFormProps {
        pacienteExistente?: Paciente;
}