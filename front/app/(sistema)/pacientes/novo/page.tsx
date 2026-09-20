import Link from "next/link";
import PacienteForm from "../components/PacienteForm";

export default function CadastroPaciente() {
  return (
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
            Novo Paciente
          </h1>
          <p className="text-sm text-teal-600">
            Preencha os dados para registrar um novo paciente
          </p>
        </div>

        <div>
          <PacienteForm />
        </div>
      </div>
    </div>
  );
}
