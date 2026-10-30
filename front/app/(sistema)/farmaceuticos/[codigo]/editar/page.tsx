"use client";

import { consultaFarmaceuticoPorId } from "@/app/services/farmaceuticoService";
import { Farmaceutico } from "@/app/types/farmaceuticos";
import Link from "next/link";
import { useParams, useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import FarmaceuticoForm from "../../components/FarmaceuticoForm";

export default function EditarFarmaceutico() {
  const router = useRouter();
  const parametro = useParams();
  const codigo = Number(parametro.codigo);
  const [farmaceutico, setFarmaceutico] = useState<Farmaceutico | null>(null);

  useEffect(() => {
    consultaFarmaceuticoPorId(codigo)
      .then(setFarmaceutico)
      .catch(() => router.push("/farmaceuticos"));
  }, [codigo, router]);

  if (!farmaceutico) return <div className="p-8">Carregando dados...</div>;

  return (
    <div className="p-6">
      <div className="mx-auto max-w-2xl space-y-6">
        <div>
          <Link href="/farmaceuticos" className="text-sm font-medium text-teal-600 transition-colors hover:text-teal-800">
            Voltar para listagem
          </Link>
        </div>
        <div className="space-y-1">
          <h1 className="text-2xl font-bold tracking-tight text-teal-800">Editar Farmacêutico {codigo}</h1>
          <p className="text-sm text-teal-600">Atualize os dados do farmacêutico.</p>
        </div>
        <FarmaceuticoForm farmaceuticoExistente={farmaceutico} />
      </div>
    </div>
  );
}
