"use client";

import { consultaPacientePorId } from "@/app/services/pacienteService";
import { Paciente } from "@/app/types/pacientes";
import Link from "next/link";
import { useParams, useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import PacienteForm from "../../components/PacienteForm";

export default function EditarPaciente() {
  const router = useRouter();
  const parametro = useParams();
  const codigo = Number(parametro.codigo);
  const [paciente, setPaciente] = useState<Paciente | null>(null);

  useEffect(() => {
    consultaPacientePorId(codigo)
      .then(setPaciente)
      .catch(() => router.push("/pacientes"));
  }, [codigo, router]);

  if (!paciente) return <div className="p-8">Carregando dados...</div>;

  return (
    <div className="p-6">
      <div className="mx-auto max-w-2xl space-y-6">
        <div>
          <Link href="/pacientes" className="text-sm font-medium text-teal-600 transition-colors hover:text-teal-800">
            Voltar para listagem
          </Link>
        </div>
        <div className="space-y-1">
          <h1 className="text-2xl font-bold tracking-tight text-teal-800">Editar Paciente {codigo}</h1>
          <p className="text-sm text-teal-600">Atualize os dados do paciente.</p>
        </div>
        <PacienteForm pacienteExistente={paciente} />
      </div>
    </div>
  );
}
