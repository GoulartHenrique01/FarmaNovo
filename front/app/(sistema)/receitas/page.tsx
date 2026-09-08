export default function Receitas() {
    return (
    <div className="space-y-6">
    <h1 className="text-2xl font-bold text-teal-800 tracking-tight">
        Receitas
    </h1>
    <table className="w-full border-collapse overflow-hidden rounded-xl shadow-md border border-teal-100">
        <thead>
            <tr className="bg-teal-50">
                <th className="px-4 py-3 text-left text-sm font-semibold text-teal-800">
                    ID
                </th>
                <th className="px-4 py-3 text-left text-sm font-semibold text-teal-800">
                    Data de Emissão
                </th>
                <th className="px-4 py-3 text-left text-sm font-semibold text-teal-800">
                    Data de Validade
                </th>
                <th className="px-4 py-3 text-left text-sm font-semibold text-teal-800">
                    Diagnóstico
                </th>
                <th className="px-4 py-3 text-left text-sm font-semibold text-teal-800">
                    Observações
                </th>
                <th className="px-4 py-3 text-left text-sm font-semibold text-teal-800">
                    Tipo
                </th>
            </tr>
        </thead>
        <tbody>
            <tr className="border-t border-teal-100 hover:bg-teal-50/50 transition-colors">
                <td className="px-4 py-3 text-sm text-gray-700">
                    1
                </td>
                <td className="px-4 py-3 text-sm text-gray-700">
                    2026-09-01
                </td>
                <td className="px-4 py-3 text-sm text-gray-700">
                    2026-09-30
                </td>
                <td className="px-4 py-3 text-sm text-gray-700">
                    Gripe
                </td>
                <td className="px-4 py-3 text-sm text-gray-700">
                    Seguir orientação médica
                </td>
                <td className="px-4 py-3 text-sm text-gray-700">
                    Controle Especial
                </td>
            </tr>
        </tbody>
    </table>
</div>
    );
}