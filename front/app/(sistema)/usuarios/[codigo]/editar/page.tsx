'use client'

import Link from "next/link";
import UsuarioForm from "../../components/UsuarioForm";
import { useParams, useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { Usuario } from "@/app/types/usuario";
import axios from "axios";

export default function EditarUsuario() {
    const router = useRouter();

    const parametro = useParams();

    const codigo = Number(parametro.codigo);

    const [usuario, setUsuario] = useState<Usuario | null>(null)

    useEffect(() => {

        buscarDados();

    }, []);

    const buscarDados = async () => {

        const valorUsuarioBack = await axios.get<Usuario>('http://localhost:8080/usuarios/' + codigo);

        if (valorUsuarioBack.status == 200) {
            setUsuario(valorUsuarioBack.data);
        } else {
            router.push("/usuarios")
        }

    }

    if (!usuario) return (<div className="p-8">Carregando Dados...</div>)

    return (
        <div className="p-6">
            <div className="max-w-2xl mx-auto space-y-6">
                <div>
                    <Link
                        href="/usuarios"
                        className="inline-flex items-center text-sm font-medium text-teal-600 hover:text-teal-800 transition-colors"
                    >
                        Voltar para listagem
                    </Link>
                </div>
                <div className="space-y-1">
                    <h1 className="text-2xl font-bold text-teal-800 tracking-tight">
                        Editar Usuário {codigo}
                    </h1>
                    <p className="text-sm text-teal-600">
                        Preencha os dados para editar um usuário
                    </p>
                </div>
                <div>
                    <UsuarioForm usuarioExistente={usuario} />
                </div>
            </div>
        </div>
    );
}