'use client'

import { Usuario } from "@/app/types/usuario";
import axios from "@/node_modules/axios/index";
import { useEffect, useState } from "react";

export default function Usuarios() {

    const [usuarios, setUsuarios] = useState<Usuario[]>([]);

    useEffect(() => {
        carregarDados();
    }, []);

    const carregarDados = async () => {

        try {
            const dados = await axios.get<Usuario[]>("http://localhost:8080/usuarios");

            setUsuarios(dados.data);

        } catch (error) {
            alert("Erro ao carregar usuarios!")
        }
    };

    return (
        <div className="space-y-6">
            <h1 className="text-2xl font-bold text-teal-800 tracking-tight">
                Usuários
            </h1>
            <table className="w-full border-collapse overflow-hidden rounded-xl shadow-md border border-teal-100">
                <thead>
                    <tr className="bg-teal-50">
                        <th className="px-4 py-3 text-left text-sm font-semibold text-teal-800">
                            Código
                        </th>
                        <th className="px-4 py-3 text-left text-sm font-semibold text-teal-800">
                            Nome
                        </th>
                        <th className="px-4 py-3 text-left text-sm font-semibold text-teal-800">
                            CPF
                        </th>
                        <th className="px-4 py-3 text-left text-sm font-semibold text-teal-800">
                            Email
                        </th>
                        <th className="px-4 py-3 text-left text-sm font-semibold text-teal-800">
                            Status
                        </th>
                    </tr>
                </thead>
                <tbody>

                    {usuarios.map((usuario) => (

                        <tr key={usuario.id} className="border-t border-teal-100 hover:bg-teal-50/50 transition-colors">
                            <td className="px-4 py-3 text-sm text-gray-700">
                                {usuario.id}
                            </td>
                            <td className="px-4 py-3 text-sm text-gray-700">
                                {usuario.nome}
                            </td>
                            <td className="px-4 py-3 text-sm text-gray-700">
                                {usuario.cpf}
                            </td>
                            <td className="px-4 py-3 text-sm text-gray-700">
                                {usuario.email}
                            </td>
                            <td className="px-4 py-3 text-sm text-gray-700">
                                {usuario.status}
                            </td>
                        </tr>
                    ))}

                    {usuarios.length === 0 && (
                        <tr>
                            <td colSpan={5} className="px-6 py-12 text-center text-slate-800 italic">
                                Nenhum usuario encontrado!
                            </td>
                        </tr>
                    )
                    }

                </tbody>
            </table>
        </div>
    );
}