import Link from "next/link";
import PacienteForm from "../components/PacienteForm";

export default function CadastroPaciente() {
  return (
    <div className="p-6">
      <div className="mx-auto max-w-2xl space-y-6">
        <div>
          <Link href="/pacientes" className="text-sm font-medium text-teal-600 transition-colors hover:text-teal-800">
            Voltar para listagem
          </Link>
        </div>
        <div className="space-y-1">
          <h1 className="text-2xl font-bold tracking-tight text-teal-800">Novo Paciente</h1>
          <p className="text-sm text-teal-600">Preencha os dados para cadastrar um paciente.</p>
        </div>
        <PacienteForm />
      </div>
    </div>
  );
}
