// Modelo dos dados de usuário usados nas telas e nas respostas da API.
export class Usuario{
    
    constructor(
        public id:number | null,
        public nome:string,
        public email:string,
        public cpf:string,
        public senha:string,
        public status:string
    )
    {
    }
}

// Dados opcionais recebidos pelo formulário; sua presença indica modo de edição.
export interface UsuarioFormProps{
    usuarioExistente?:Usuario
}