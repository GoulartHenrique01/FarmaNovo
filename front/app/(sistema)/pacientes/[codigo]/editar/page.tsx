'use client'

import axios from "axios";
import Link from "next/link";
import { useParams, useRouter } from "next/navigation";
import PacienteForm from "../../components/PacienteForm";
import { useEffect, useState } from "react";
import { Paciente } from "@/app/types/pacientes";

export default function EditarPaciente(){
    
    const router = useRouter();
    const parametro = useParams();

    const codigo = Number(parametro.codigo);

    const [paciente, setPaciente] = useState<Paciente | null>(null);

    useEffect(() => {
        axios.get<Paciente>(`http://localhost:8080/pacientes/${codigo}`)
            .then((resposta) => setPaciente(resposta.data))
            .catch(() => router.push("/pacientes"));
    }, [codigo, router]);

    if (!paciente) return <div className="p-8">Carregando dados...</div>;

    return(
        <div className="p-6">
    <div className="max-w-2xl mx-auto space-y-6">
        <div>
            <Link 
                href="/pacientes" 
                className="inline-flex items-center text-sm font-medium text-teal-600 hover:text-teal-800 transition-colors"
            >
                Voltar para listagem
            </Link>
        </div>
        <div className="space-y-1">
            <h1 className="text-2xl font-bold text-teal-800 tracking-tight">
                Editar Paciente {codigo}
            </h1>
            <p className="text-sm text-teal-600">
                Preencha os dados para editar um paciente
            </p>
        </div>
        <div>
            <PacienteForm pacienteExistente={paciente}/>
        </div>
    </div>
</div>
    );
}