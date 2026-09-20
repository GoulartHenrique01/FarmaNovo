"use client";

import { useEffect, useState } from "react";

export default function Header() {
    const [darkMode, setDarkMode] = useState(false);

    useEffect(() => {
        const storedTheme = localStorage.getItem("theme");
        const prefersDark = window.matchMedia("(prefers-color-scheme: dark)").matches;
        const isDark = storedTheme ? storedTheme === "dark" : prefersDark;

        setDarkMode(isDark);
        document.documentElement.classList.toggle("dark", isDark);
    }, []);

    useEffect(() => {
        document.documentElement.classList.toggle("dark", darkMode);
        localStorage.setItem("theme", darkMode ? "dark" : "light");
    }, [darkMode]);

    return (
        <header className={`w-full border-b shadow-sm transition-colors duration-200 ${darkMode ? "border-slate-700 bg-slate-900" : "border-green-100 bg-white"}`}>
            <div className="mx-auto flex min-h-16 w-full items-center justify-between px-4 sm:px-6 lg:px-8">
                <div className="flex items-center gap-3">
                    <div className="flex h-10 w-10 items-center justify-center rounded-xl bg-green-600 shadow-sm">
                        <div>
                            <svg className="h-6 w-6 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2" >
                                <path strokeLinecap="round" strokeLinejoin="round" d="M9 3v6H3v6h6v6h6v-6h6V9h-6V3H9z" />
                            </svg>
                        </div>
                    </div>
                    <span className={`text-sm font-semibold sm:text-base ${darkMode ? "text-slate-100" : "text-gray-700"}`}>
                        Usuário Henrique Goulart
                    </span>
                </div>

                <div className="flex items-center gap-3">
                    <button
                        type="button"
                        onClick={() => setDarkMode((prev) => !prev)}
                        className={`rounded-lg border px-3 py-2 text-xs font-semibold transition-all duration-200 focus:outline-none focus:ring-2 ${darkMode ? "border-slate-600 bg-slate-800 text-slate-100 hover:bg-slate-700 focus:ring-slate-400" : "border-amber-200 bg-amber-50 text-amber-700 hover:bg-amber-100 focus:ring-amber-200"}`}
                    >
                        {darkMode ? "Modo claro" : "Modo dark"}
                    </button>

                    <button className={`rounded-lg border px-4 py-2 text-sm font-semibold transition-all duration-200 focus:outline-none focus:ring-2 ${darkMode ? "border-red-500/40 bg-red-500/10 text-red-300 hover:bg-red-500/20 focus:ring-red-400" : "border-red-200 bg-red-50 text-red-600 hover:border-red-300 hover:bg-red-100 hover:text-red-700 focus:ring-red-200"}`}>
                        Sair
                    </button>
                </div>
            </div>
        </header>
    );
}