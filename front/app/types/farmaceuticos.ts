// Modelo que reúne os dados pessoais e o status de um Farmaceutico.
export class Farmaceutico {
    constructor(
        public id: number | null,
        public nome: string,
        public cpf: string,
        public telefone: string,
        public email: string,
        public senha: string,
        public status: string
    )
    {

    }
  }

// Propriedade opcional que permite reutilizar o formulário para edição.
export interface FarmaceuticoFormProps {
  farmaceuticoExistente?: Farmaceutico;
}