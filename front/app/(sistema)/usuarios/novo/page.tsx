import Link from "next/link";
import UsuarioForm from "../components/UsuarioForm";

// Página de cadastro: apresenta o contexto e reutiliza o formulário de usuário.
export default function CadastroUsuario(){

    return(
        <div className="p-6">
    <div className="max-w-2xl mx-auto space-y-6">
        <div>
            <Link 
                href="/usuarios" 
                className="rounded-lg border border-slate-300 bg-white px-4 py-2.5 text-sm font-semibold text-slate-700 shadow-sm transition"
            >
                Voltar para listagem
            </Link>
        </div>
        <div className="space-y-1">
            <h1 className="font-serif text-2xl font-bold text-teal-800 tracking-tight">
                Novo Usuário
            </h1>
            <p className="font-serif text-sm text-teal-600">
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