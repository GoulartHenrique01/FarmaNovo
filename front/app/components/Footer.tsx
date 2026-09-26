export default function Footer() {

    // Mantém o ano exibido no rodapé sempre atualizado.
    const currentYear = new Date().getFullYear();

    return (
        <footer className="w-full border-t border-teal-100 bg-white">
    <div className="max-w-7xl mx-auto px-4 py-4">
        <div className="text-center">
            <p className="text-sm text-teal-600">
                &copy;{currentYear} <span className="font-medium text-teal-800">FarmaControll</span> Todos os direitos reservados.
            </p>
        </div>
    </div>
</footer>
    );
}