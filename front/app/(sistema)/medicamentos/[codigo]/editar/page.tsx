'use client'

import axios from "axios";
import Link from "next/link";
import { useParams, useRouter } from "next/navigation";
import MedicamentoForm from "../../components/MedicamentoForm";
import { useEffect, useState } from "react";
import { Medicamento } from "@/app/types/medicamentos";

export default function EditarMedicamentos() {

    const router = useRouter();
    const parametro = useParams();

    const codigo = Number(parametro.codigo);

    const [medicamento, setMedicamento] = useState<Medicamento | null>(null);

    useEffect(() => {
        axios.get<Medicamento>(`http://localhost:8080/medicamentos/${codigo}`)
            .then((resposta) => setMedicamento(resposta.data))
            .catch(() => router.push("/medicamentos"));
    }, [codigo, router]);

    if (!medicamento) return <div className="p-8">Carregando dados...</div>;

    return (
        <div className="p-6">
            <div className="max-w-2xl mx-auto space-y-6">
                <div>
                    <Link
                        href="/medicamentos"
                        className="inline-flex items-center text-sm font-medium text-teal-600 hover:text-teal-800 transition-colors"
                    >
                        Voltar para listagem
                    </Link>
                </div>
                <div className="space-y-1">
                    <h1 className="text-2xl font-bold text-teal-800 tracking-tight">
                        Editar Medicamento {codigo}
                    </h1>
                    <p className="text-sm text-teal-600">
                        Preencha os dados para editar um medicamento
                    </p>
                </div>
                <div>
                    <MedicamentoForm medicamentoExistente={medicamento} />
                </div>
            </div>
        </div>
    );
}