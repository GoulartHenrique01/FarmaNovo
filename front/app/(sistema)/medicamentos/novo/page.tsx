import Link from "next/link";
import MedicamentoForm from "../components/MedicamentoForm";

// Página de cadastro que reutiliza o formulário compartilhado de medicamentos.
export default function CadastroMedicamento() {
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
            Novo Medicamento
          </h1>
          <p className="text-sm text-teal-600">
            Preencha os dados para registrar um novo medicamento
          </p>
        </div>

        <div>
          <MedicamentoForm />
        </div>
      </div>
    </div>
  );
}
