import Footer from "../components/Footer";
import Header from "../components/Header";
import Sidebar from "../components/Sidebar";

// Envolve as páginas internas com navegação lateral, cabeçalho e rodapé comuns.
export default function SistemaLayout({ children }) {
    return (
        
        <div className="flex min-h-screen bg-gradient-to-br from-white via-emerald-50/40 to-teal-50">
        <Sidebar />
    
        <div className="flex-1 flex flex-col min-w-0">
            <Header />
    
            <main className="flex-1 p-6 overflow-auto">
                {children}
            </main>
            
            <Footer />
        </div>
    </div>);
}

//CTRL + K + C, comenta a linha