import Link from "next/link";
import UsuarioForm from "../components/UsuarioForm";

export default function CadastroUsuario(){

    return(
        <div className="p-6">
    <div className="max-w-2xl mx-auto space-y-6">
        <div>
            <Link 
                href="/usuarios" 
                className="inline-flex items-center text-sm font-medium text-teal-600 hover:text-teal-800 transition-colors"
            >
                Voltar para listagem
            </Link>
        </div>
        <div className="space-y-1">
            <h1 className="text-2xl font-bold text-teal-800 tracking-tight">
                Novo Usuário
            </h1>
            <p className="text-sm text-teal-600">
                Preencha os dados para registrar um novo usuário
            </p>
        </div>
        <div>
            <UsuarioForm/>
        </div>
    </div>
</div>
    );
}