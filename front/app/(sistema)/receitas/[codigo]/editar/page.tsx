'use client'

import Link from "next/link";
import { useParams, useRouter } from "next/navigation";
import ReceitaForm from "../../components/ReceitaForm";
import { useEffect, useState } from "react";
import { Receita } from "@/app/types/receitas";
import { consultaReceitaPorId } from "@/app/services/receitaService";

// Busca a receita indicada na URL e abre o formulário com seus dados.
export default function EditarReceita(){
    
    const router = useRouter();

    // Código identificador recebido pelos parâmetros da rota.
    const parametro = useParams();

    const codigo = Number(parametro.codigo);

    // Mantém a página em carregamento até a receita ser obtida.
    const [receita, setReceita] = useState<Receita | null>(null)

    // Carrega os dados da receita ao abrir a página de edição.
    useEffect(() => {
        consultaReceitaPorId(codigo)
            .then((dados) => setReceita({
                ...dados,
                dataEmissao: dados.dataEmissao?.slice(0, 10) ?? "",
                dataValidade: dados.dataValidade?.slice(0, 10) ?? "",
            }))
            .catch(() => router.push("/receitas"));
    }, [codigo, router]);

    if (!receita) return (<div className="p-8">Carregando Dados...</div>)

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
                Editar Receita {codigo}
            </h1>
            <p className="text-sm text-teal-600">
                Preencha os dados para editar uma receita
            </p>
        </div>
        <div>
            <ReceitaForm receitaExistente={receita}/>
        </div>
    </div>
</div>
    );
}