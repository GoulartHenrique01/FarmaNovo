export default function Pacientes() {
    return (
      <div className="space-y-6">
    <h1 className="text-2xl font-bold text-teal-800 tracking-tight">
        Pacientes
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
                    CPF
                </th>
                <th className="px-4 py-3 text-left text-sm font-semibold text-teal-800">
                    Data de Nascimento
                </th>
                <th className="px-4 py-3 text-left text-sm font-semibold text-teal-800">
                    Sexo
                </th>
                <th className="px-4 py-3 text-left text-sm font-semibold text-teal-800">
                    Telefone
                </th>
                <th className="px-4 py-3 text-left text-sm font-semibold text-teal-800">
                    Email
                </th>
                <th className="px-4 py-3 text-left text-sm font-semibold text-teal-800">
                    Observações
                </th>
                <th className="px-4 py-3 text-left text-sm font-semibold text-teal-800">
                    Status
                </th>
            </tr>
        </thead>
        <tbody>
            <tr className="border-t border-teal-100 hover:bg-teal-50/50 transition-colors">
                <td className="px-4 py-3 text-sm text-gray-700">
                    1
                </td>
                <td className="px-4 py-3 text-sm text-gray-700">
                    João da Silva
                </td>
                <td className="px-4 py-3 text-sm text-gray-700">
                    123.456.789-00
                </td>
                <td className="px-4 py-3 text-sm text-gray-700">
                    1990-05-15
                </td>
                <td className="px-4 py-3 text-sm text-gray-700">
                    Masculino
                </td>
                <td className="px-4 py-3 text-sm text-gray-700">
                    (11) 99999-9999
                </td>
                <td className="px-4 py-3 text-sm text-gray-700">
                    joao@email.com
                </td>
                <td className="px-4 py-3 text-sm text-gray-700">
                    Sem observações
                </td>
                <td className="px-4 py-3 text-sm text-gray-700">
                    Ativo
                </td>
            </tr>
        </tbody>
    </table>
</div>
    );
}