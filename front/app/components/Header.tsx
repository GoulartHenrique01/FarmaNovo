export default function Header() {

    return (
        <header className="w-full border-b border-green-100 bg-white shadow-sm">
            <div className="mx-auto flex min-h-16 w-full items-center justify-between px-4 sm:px-6 lg:px-8">
                <div className="flex items-center gap-3">
                    <div className="flex h-10 w-10 items-center justify-center rounded-xl bg-green-600 shadow-sm">
                        <div>
                            <svg className="h-6 w-6 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2" >
                                {/* Cruz de farmácia */}
                                <path strokeLinecap="round" strokeLinejoin="round" d="M9 3v6H3v6h6v6h6v-6h6V9h-6V3H9z" />
                            </svg>
                        </div>
                    </div>
                    <span className="text-sm font-semibold text-gray-700 sm:text-base"> Usuário Henrique Goulart </span>
                </div>
                <button className="rounded-lg border border-red-200 px-4 py-2 text-sm font-semibold text-red-600 transition-all duration-200 hover:border-red-300 hover:bg-red-50 hover:text-red-700 focus:outline-none focus:ring-2 focus:ring-red-200">
                    Sair
                </button>
            </div>
        </header>
    );
}