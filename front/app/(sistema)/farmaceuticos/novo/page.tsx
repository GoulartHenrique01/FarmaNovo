import Link from "next/link";
import FarmaceuticoForm from "../components/FarmaceuticoForm";

export default function CadastroFarmaceutico() {
  return (
    <div className="p-6">
      <div className="mx-auto max-w-2xl space-y-6">
        <div>
          <Link href="/farmaceuticos" className="text-sm font-medium text-teal-600 transition-colors hover:text-teal-800">
            Voltar para listagem
          </Link>
        </div>
        <div className="space-y-1">
          <h1 className="text-2xl font-bold tracking-tight text-teal-800">Novo Farmacêutico</h1>
          <p className="text-sm text-teal-600">Preencha os dados para cadastrar um farmacêutico.</p>
        </div>
        <FarmaceuticoForm />
      </div>
    </div>
  );
}
