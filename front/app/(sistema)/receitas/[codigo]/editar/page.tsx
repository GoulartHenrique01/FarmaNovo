'use client'

import Link from "next/link";
import { useParams, useRouter } from "next/navigation";
import ReceitaForm from "../../components/ReceitaForm";
import { useEffect, useState } from "react";
import { Receita } from "@/app/types/receitas";
import axios from "axios";

export default function EditarReceita(){
    
    const router = useRouter();

    const parametro = useParams();

    const codigo = Number(parametro.codigo);

    const [receita, setReceita] = useState<Receita | null>(null)

    useEffect(() => {

        buscarDados();

    }, []);

    const buscarDados = async () => {

        const valorUsuarioBack = await axios.get<Receita>('http://localhost:8080/receitas/' + codigo);

        if (valorUsuarioBack.status == 200) {
            setReceita(valorUsuarioBack.data);
        } else {
            router.push("/receitas")
        }

    }

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