import Link from "next/link";

export default function UsuarioForm() {

    return (
        <form className="max-w-2xl mx-auto">
        <div className="bg-white rounded-2xl shadow-md border border-teal-100 p-6 space-y-5">
            <div className="space-y-2">
                <label className="block text-sm font-medium text-teal-700">
                    Nome Completo:
                </label>
                <input 
                    name="nome" 
                    className="w-full px-4 py-2.5 border border-teal-200 rounded-lg bg-white text-gray-800 focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500 transition"
                />
            </div>
            <div className="space-y-2">
                <label className="block text-sm font-medium text-teal-700">
                    CPF:
                </label>
                <input 
                    name="cpf" 
                    className="w-full px-4 py-2.5 border border-teal-200 rounded-lg bg-white text-gray-800 focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500 transition"
                />
            </div>
            <div className="space-y-2">
                <label className="block text-sm font-medium text-teal-700">
                    E-mail:
                </label>
                <input 
                    name="email" 
                    className="w-full px-4 py-2.5 border border-teal-200 rounded-lg bg-white text-gray-800 focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500 transition"
                />
            </div>
            <div className="space-y-2">
                <label className="block text-sm font-medium text-teal-700">
                    Senha:
                </label>
            </div>
    
            <input 
                name="senha" 
                className="w-full px-4 py-2.5 border border-teal-200 rounded-lg bg-white text-gray-800 focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500 transition"
            />
    
            <div className="flex items-center justify-between pt-4 border-t border-teal-50">
                <Link 
                    href="/usuarios" 
                    className="px-5 py-2.5 text-sm font-medium text-teal-700 bg-teal-50 hover:bg-teal-100 rounded-lg transition-colors"
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