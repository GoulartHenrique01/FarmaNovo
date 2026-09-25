'use client'

import { Usuario, UsuarioFormProps } from "@/app/types/usuario";
import axios from "axios";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";

export default function UsuarioForm({usuarioExistente}:UsuarioFormProps) {
    const router = useRouter();

    //Constante com array de informações
    const [usuario, setUsuario] = useState<Usuario>(
        usuarioExistente || 
        new Usuario(null, "", "", "", "", "ATIVO")
    );

    const handlerChange = (
        campo: 'nome' | 'cpf' | 'email' | 'senha',
        valor: string
    ) => {
        setUsuario(valorAnterior =>
            new Usuario(
                valorAnterior.id,
                campo === 'nome' ? valor : valorAnterior.nome,
                campo === 'email' ? valor : valorAnterior.email,
                campo === 'cpf' ? valor : valorAnterior.cpf,
                campo === 'senha' ? valor : valorAnterior.senha,
                valorAnterior.status
            )
        );
    }

    //formData - nome / FormData - Tipo
    const hanlderSalvar = async (formData: FormData) => {

        if(usuarioExistente){
            var dadosRetorno = await axios.put<number>('http://localhost:8080/usuarios'+usuario.id, usuario)

        if (dadosRetorno.status == 200) {
            alert("Usuário salvo com sucesso")
        } else {
            alert(dadosRetorno.data);
            return;
        }
            
        }else{

        var dadosRetorno = await axios.post<number>('http://localhost:8080/usuarios', usuario)

        if (dadosRetorno.status == 200) {
            alert("Usuário salvo com sucesso")
        } else {
            alert(dadosRetorno.data);
            return;
        }
        router.push("/usuarios");

    }
}

    return (
        <form action={hanlderSalvar} className="max-w-2xl mx-auto">
            <div className="bg-white rounded-2xl shadow-md border border-teal-100 p-6 space-y-5">
                <div className="space-y-2">
                    <label className="block text-sm font-medium text-teal-700">
                        Nome Completo:
                    </label>
                    <input
                        name="nome"
                        value={usuario.nome}
                        required
                        onChange={(e) => handlerChange('nome', e.target.value)}
                        placeholder="Henrique Goulart"
                        className="w-full px-4 py-2.5 border border-teal-200 rounded-lg bg-white text-gray-800 focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500 transition"
                    />
                </div>
                <div className="space-y-2">
                    <label className="block text-sm font-medium text-teal-700">
                        CPF:
                    </label>
                    <input
                        name="cpf"
                        value={usuario.cpf}
                        required
                        onChange={(e) => handlerChange('cpf', e.target.value)}
                        placeholder="000.000.000-00"
                        className="w-full px-4 py-2.5 border border-teal-200 rounded-lg bg-white text-gray-800 focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500 transition"
                    />
                </div>
                <div className="space-y-2">
                    <label className="block text-sm font-medium text-teal-700">
                        E-mail:
                    </label>
                    <input
                        name="email"
                        value={usuario.email}
                        required
                        onChange={(e) => handlerChange('email', e.target.value)}
                        placeholder="henrique@email.com.br"
                        className="w-full px-4 py-2.5 border border-teal-200 rounded-lg bg-white text-gray-800 focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500 transition"
                    />
                </div>
                <div className="space-y-2">
                    <label className="block text-sm font-medium text-teal-700">
                        Senha:
                    </label>

                    <input
                        name="senha"
                        type="password"
                        value={usuario.senha}
                        required
                        onChange={(e) => handlerChange('senha', e.target.value)}
                        className="w-full px-4 py-2.5 border border-teal-200 rounded-lg bg-white text-gray-800 focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500 transition"
                    />
                </div>
                <div className="flex items-center justify-between pt-4 border-t border-teal-50">
                    <Link
                        href="/usuarios"
                        className="rounded-lg border px-4 py-2 text-sm font-semibold transition-all duration-200 focus:outline-none focus:ring-2 border-red-500/40 bg-red-500/10 text-red-300 hover:bg-red-500/20 focus:ring-red-400"
                    >
                        Cancelar
                    </Link>
                    <button
                        type="submit"
                        className="px-5 py-2.5 text-sm font-medium text-white bg-gradient-to-r from-teal-700 to-emerald-600 hover:from-teal-800 hover:to-emerald-700 rounded-lg shadow-sm transition-all duration-200"
                    >
                        Salvar
                    </button>
                </div>
            </div>
        </form>
    );
}