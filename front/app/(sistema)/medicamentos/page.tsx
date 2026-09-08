export default function Medicamentos() {
    return (
      <div className="space-y-6">
    <h1 className="text-2xl font-bold text-teal-800 tracking-tight">
        Medicamentos
    </h1>
    <table className="w-full border-collapse overflow-hidden rounded-xl shadow-md border border-teal-100">
        <thead>
            <tr className="bg-teal-50">
                <th className="px-4 py-3 text-left text-sm font-semibold text-teal-800">
                    ID
                </th>
                <th className="px-4 py-3 text-left text-sm font-semibold text-teal-800">
                    Nome
                </th>
                <th className="px-4 py-3 text-left text-sm font-semibold text-teal-800">
                    Tipo
                </th>
                <th className="px-4 py-3 text-left text-sm font-semibold text-teal-800">
                    Data de Validade
                </th>
                <th className="px-4 py-3 text-left text-sm font-semibold text-teal-800">
                    Dosagem
                </th>
                <th className="px-4 py-3 text-left text-sm font-semibold text-teal-800">
                    Unidade de Dosagem
                </th>
                <th className="px-4 py-3 text-left text-sm font-semibold text-teal-800">
                    Quantidade
                </th>
                <th className="px-4 py-3 text-left text-sm font-semibold text-teal-800">
                    Marca
                </th>
                <th className="px-4 py-3 text-left text-sm font-semibold text-teal-800">
                    Precisa de Receita
                </th>
            </tr>
        </thead>
        <tbody>
            <tr className="border-t border-teal-100 hover:bg-teal-50/50 transition-colors">
                <td className="px-4 py-3 text-sm text-gray-700">
                    1
                </td>
                <td className="px-4 py-3 text-sm text-gray-700">
                    Paracetamol
                </td>
                <td className="px-4 py-3 text-sm text-gray-700">
                    Analgésico
                </td>
                <td className="px-4 py-3 text-sm text-gray-700">
                    2026-12-31
                </td>
                <td className="px-4 py-3 text-sm text-gray-700">
                    500
                </td>
                <td className="px-4 py-3 text-sm text-gray-700">
                    mg
                </td>
                <td className="px-4 py-3 text-sm text-gray-700">
                    30
                </td>
                <td className="px-4 py-3 text-sm text-gray-700">
                    Medley
                </td>
                <td className="px-4 py-3 text-sm text-gray-700">
                    Não
                </td>
            </tr>
        </tbody>
    </table>
</div>
    );
}