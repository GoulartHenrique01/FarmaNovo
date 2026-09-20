import Link from "next/link";

import ReceitaForm from "../components/ReceitaForm";

export default function CadastroReceita(){

    return(
        <div className="p-6">
    <div className="max-w-2xl mx-auto space-y-6">
        <div>
            <Link 
                href="/receitas" 
                className="inline-flex items-center text-sm font-medium text-teal-600 hover:text-teal-800 transition-colors"
            >
                Voltar para listagem
            </Link>
        </div>
        <div className="space-y-1">
            <h1 className="text-2xl font-bold text-teal-800 tracking-tight">
                Nova Receita
            </h1>
            <p className="text-sm text-teal-600">
                Preencha os dados para registrar uma nova receita
            </p>
        </div>
        <div>
            <ReceitaForm/>
        </div>
    </div>
</div>
    );
}