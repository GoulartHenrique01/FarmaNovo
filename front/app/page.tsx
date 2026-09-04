"use client";

export default function Home() {
  return (
    <>
      <style>{`
        * {
          margin: 0;
          padding: 0;
          box-sizing: border-box;
        }

        html {
          scroll-behavior: smooth;
        }

        body {
          font-family:
            Inter,
            -apple-system,
            BlinkMacSystemFont,
            "Segoe UI",
            sans-serif;

          background: #f7fbfa;
          color: #16333a;
          line-height: 1.6;
        }

        a {
          text-decoration: none;
          color: inherit;
        }

        :root {
          --blue: #1769aa;
          --blue-dark: #0e4f82;
          --blue-light: #eaf5ff;

          --green: #16a085;
          --green-dark: #08745f;
          --green-light: #e9faf5;

          --dark: #12343b;
          --text: #4d6268;

          --background: #f7fbfa;

          --border: #dcebea;

          --shadow:
            0 20px 50px rgba(21, 74, 83, 0.08);

          --radius: 18px;
        }

        /* =========================
           HEADER
        ========================= */

        .header {
          position: fixed;
          top: 0;
          left: 0;

          width: 100%;
          height: 76px;

          z-index: 1000;

          background: rgba(255, 255, 255, 0.9);

          backdrop-filter: blur(16px);

          border-bottom:
            1px solid rgba(220, 235, 234, 0.8);
        }

        .navbar {
          width: min(1180px, 90%);
          height: 100%;

          margin: auto;

          display: flex;
          align-items: center;
          justify-content: space-between;
        }

        /* =========================
           LOGO
        ========================= */

        .logo {
          display: flex;
          align-items: center;

          gap: 10px;

          font-size: 1.45rem;
          font-weight: 800;

          color: var(--dark);
        }

        .logo-icon {
          width: 42px;
          height: 42px;

          display: flex;
          align-items: center;
          justify-content: center;

          border-radius: 12px;

          background:
            linear-gradient(
              135deg,
              var(--blue),
              var(--green)
            );

          color: white;

          box-shadow:
            0 8px 20px rgba(23, 105, 170, 0.2);
        }

        .logo-icon svg {
          width: 27px;
          height: 27px;
        }

        .logo span {
          color: var(--blue);
        }

        /* =========================
           MENU
        ========================= */

        .nav-links {
          display: flex;
          align-items: center;

          gap: 34px;
        }

        .nav-links a {
          font-size: 0.94rem;
          font-weight: 500;

          color: #52676d;

          transition: 0.25s ease;
        }

        .nav-links a:hover {
          color: var(--blue);
        }

        .login-button {
          padding: 11px 22px;

          border-radius: 10px;

          background: var(--blue);

          color: white !important;

          font-weight: 600 !important;

          transition: 0.25s ease;

          box-shadow:
            0 8px 20px rgba(23, 105, 170, 0.18);
        }

        .login-button:hover {
          background: var(--blue-dark);

          transform: translateY(-2px);
        }

        /* =========================
           HERO
        ========================= */

        .hero {
          min-height: 720px;

          padding-top: 76px;

          display: flex;
          align-items: center;

          position: relative;

          overflow: hidden;

          background:
            radial-gradient(
              circle at 80% 30%,
              rgba(22, 160, 133, 0.1),
              transparent 30%
            ),
            radial-gradient(
              circle at 20% 70%,
              rgba(23, 105, 170, 0.08),
              transparent 30%
            ),
            #f7fbfa;
        }

        .hero-container {
          width: min(1180px, 90%);

          margin: auto;

          display: grid;

          grid-template-columns:
            1.05fr 0.95fr;

          align-items: center;

          gap: 80px;
        }

        .hero-content {
          max-width: 600px;
        }

        .hero-tag {
          display: inline-flex;
          align-items: center;

          gap: 8px;

          padding: 7px 13px;

          margin-bottom: 22px;

          border-radius: 50px;

          background: var(--green-light);

          color: var(--green-dark);

          font-size: 0.82rem;

          font-weight: 700;
        }

        .hero-tag::before {
          content: "";

          width: 7px;
          height: 7px;

          border-radius: 50%;

          background: var(--green);
        }

        .hero h1 {
          font-size: clamp(2.7rem, 5vw, 4.4rem);

          line-height: 1.08;

          letter-spacing: -2px;

          color: var(--dark);

          margin-bottom: 24px;
        }

        .hero h1 span {
          color: var(--blue);
        }

        .hero-description {
          max-width: 540px;

          color: var(--text);

          font-size: 1.08rem;

          margin-bottom: 34px;
        }

        .hero-buttons {
          display: flex;
          align-items: center;

          gap: 14px;
        }

        /* =========================
           BOTÕES
        ========================= */

        .primary-button {
          display: inline-block;

          padding: 14px 25px;

          border-radius: 11px;

          background:
            linear-gradient(
              135deg,
              var(--blue),
              var(--green)
            );

          color: white;

          font-weight: 700;

          transition: 0.25s ease;

          box-shadow:
            0 12px 25px rgba(23, 105, 170, 0.18);
        }

        .primary-button:hover {
          transform: translateY(-3px);

          box-shadow:
            0 16px 30px rgba(23, 105, 170, 0.25);
        }

        .secondary-button {
          display: inline-block;

          padding: 13px 24px;

          border-radius: 11px;

          border: 1px solid var(--border);

          background: white;

          color: var(--dark);

          font-weight: 600;

          transition: 0.25s ease;
        }

        .secondary-button:hover {
          border-color: var(--blue);

          color: var(--blue);
        }

        /* =========================
           DASHBOARD
        ========================= */

        .hero-visual {
          position: relative;

          display: flex;
          justify-content: center;
        }

        .dashboard {
          width: 100%;
          max-width: 470px;

          padding: 24px;

          border-radius: 25px;

          background: rgba(255, 255, 255, 0.94);

          border: 1px solid rgba(220, 235, 234, 0.8);

          box-shadow:
            0 30px 70px rgba(21, 74, 83, 0.12);

          transform: rotate(1deg);
        }

        .dashboard-header {
          display: flex;
          align-items: center;
          justify-content: space-between;

          gap: 15px;

          margin-bottom: 24px;
        }

        .dashboard-title {
          font-weight: 700;

          color: var(--dark);
        }

        .status {
          padding: 5px 10px;

          border-radius: 20px;

          background: var(--green-light);

          color: var(--green-dark);

          font-size: 0.72rem;

          font-weight: 700;

          white-space: nowrap;
        }

        .dashboard-card {
          padding: 17px;

          margin-bottom: 12px;

          border-radius: 14px;

          background: #f8fbfc;

          border: 1px solid #edf3f3;

          transition: 0.25s ease;
        }

        .dashboard-card:last-child {
          margin-bottom: 0;
        }

        .dashboard-card:hover {
          transform: translateX(4px);

          border-color: #cfe4e2;
        }

        .dashboard-card-top {
          display: flex;

          justify-content: space-between;

          gap: 15px;

          margin-bottom: 10px;
        }

        .medicine-name {
          font-weight: 700;

          color: var(--dark);
        }

        .prescription {
          color: var(--blue);

          font-size: 0.7rem;

          font-weight: 800;

          white-space: nowrap;
        }

        .medicine-info {
          display: flex;

          justify-content: space-between;

          gap: 10px;

          color: #789096;

          font-size: 0.78rem;
        }

        .stock {
          color: var(--green-dark);

          font-weight: 700;
        }

        /* =========================
           SEÇÕES
        ========================= */

        section {
          padding: 100px 0;
        }

        .section-container {
          width: min(1100px, 90%);

          margin: auto;
        }

        .section-heading {
          max-width: 680px;

          margin: 0 auto 60px;

          text-align: center;
        }

        .section-label {
          display: inline-block;

          margin-bottom: 10px;

          color: var(--green);

          font-size: 0.82rem;

          font-weight: 800;

          text-transform: uppercase;

          letter-spacing: 1.4px;
        }

        .section-heading h2 {
          margin-bottom: 15px;

          font-size: clamp(2rem, 4vw, 3rem);

          line-height: 1.15;

          color: var(--dark);

          letter-spacing: -1px;
        }

        .section-heading p {
          color: var(--text);
        }

        /* =========================
           HISTÓRIA
        ========================= */

        .history {
          background: white;
        }

        .history-grid {
          display: grid;

          grid-template-columns:
            0.8fr 1.2fr;

          gap: 80px;

          align-items: center;
        }

        .history-card {
          padding: 40px;

          border-radius: 24px;

          background:
            linear-gradient(
              145deg,
              #edf8ff,
              #effbf7
            );

          border: 1px solid var(--border);
        }

        .history-icon {
          width: 60px;
          height: 60px;

          display: flex;
          align-items: center;
          justify-content: center;

          margin-bottom: 25px;

          border-radius: 16px;

          background: white;

          color: var(--blue);

          box-shadow:
            0 10px 30px rgba(21, 74, 83, 0.08);
        }

        .history-icon svg {
          width: 30px;
          height: 30px;
        }

        .history-card h3 {
          margin-bottom: 12px;

          font-size: 1.5rem;

          color: var(--dark);
        }

        .history-card p {
          color: var(--text);
        }

        .history-text p {
          margin-bottom: 18px;

          color: var(--text);
        }

        .history-text p:last-child {
          margin-bottom: 0;
        }

        .history-text strong {
          color: var(--dark);
        }

        /* =========================
           FUNCIONALIDADES
        ========================= */

        .features {
          background: var(--background);
        }

        .features-grid {
          display: grid;

          grid-template-columns:
            repeat(3, 1fr);

          gap: 20px;
        }

        .feature {
          padding: 30px;

          border-radius: var(--radius);

          background: white;

          border: 1px solid var(--border);

          transition: 0.3s ease;
        }

        .feature:hover {
          transform: translateY(-6px);

          box-shadow: var(--shadow);
        }

        .feature-icon {
          width: 52px;
          height: 52px;

          display: flex;
          align-items: center;
          justify-content: center;

          margin-bottom: 22px;

          border-radius: 14px;

          background: var(--blue-light);

          color: var(--blue);

          font-size: 1.35rem;

          font-weight: 800;
        }

        .feature:nth-child(2) .feature-icon,
        .feature:nth-child(5) .feature-icon {
          background: var(--green-light);

          color: var(--green-dark);
        }

        .feature h3 {
          margin-bottom: 9px;

          color: var(--dark);

          font-size: 1.1rem;
        }

        .feature p {
          color: var(--text);

          font-size: 0.91rem;
        }

        /* =========================
           COMO FUNCIONA
        ========================= */

        .steps {
          background: white;
        }

        .steps-grid {
          display: grid;

          grid-template-columns:
            repeat(3, 1fr);

          gap: 30px;
        }

        .step {
          padding: 35px 30px;

          text-align: center;
        }

        .step-number {
          width: 52px;
          height: 52px;

          display: flex;
          align-items: center;
          justify-content: center;

          margin: 0 auto 20px;

          border-radius: 50%;

          background:
            linear-gradient(
              135deg,
              var(--blue),
              var(--green)
            );

          color: white;

          font-weight: 800;
        }

        .step h3 {
          margin-bottom: 10px;

          color: var(--dark);
        }

        .step p {
          color: var(--text);

          font-size: 0.92rem;
        }

        /* =========================
           CTA
        ========================= */

        .cta {
          padding: 90px 0;
        }

        .cta-container {
          width: min(1100px, 90%);

          margin: auto;

          padding: 65px 70px;

          border-radius: 28px;

          position: relative;

          overflow: hidden;

          background:
            linear-gradient(
              120deg,
              var(--blue-dark),
              var(--green-dark)
            );

          color: white;
        }

        .cta-container::after {
          content: "";

          position: absolute;

          width: 300px;
          height: 300px;

          right: -100px;
          top: -150px;

          border-radius: 50%;

          background: rgba(255, 255, 255, 0.08);
        }

        .cta-content {
          max-width: 650px;

          position: relative;

          z-index: 2;
        }

        .cta h2 {
          margin-bottom: 14px;

          font-size: clamp(2rem, 4vw, 3rem);

          line-height: 1.15;
        }

        .cta p {
          margin-bottom: 28px;

          color: rgba(255, 255, 255, 0.82);
        }

        .cta .primary-button {
          background: white;

          color: var(--blue-dark);
        }

        .cta .primary-button:hover {
          background: #f2f8f8;
        }

        /* =========================
           FOOTER
        ========================= */

        .footer {
          padding: 35px 0;

          background: #102f35;

          color: white;
        }

        .footer-container {
          width: min(1100px, 90%);

          margin: auto;

          display: flex;

          justify-content: space-between;

          align-items: center;

          gap: 20px;
        }

        .footer-logo {
          font-weight: 800;

          font-size: 1.2rem;
        }

        .footer-logo span {
          color: #51d1b2;
        }

        .footer-text {
          color: rgba(255, 255, 255, 0.6);

          font-size: 0.82rem;
        }

        /* =========================
           RESPONSIVIDADE
        ========================= */

        @media (max-width: 900px) {

          .nav-links {
            gap: 15px;
          }

          .nav-links a:not(.login-button) {
            display: none;
          }

          .hero {
            min-height: auto;

            padding-top: 130px;
            padding-bottom: 80px;
          }

          .hero-container {
            grid-template-columns: 1fr;

            gap: 55px;
          }

          .hero-content {
            text-align: center;

            margin: auto;
          }

          .hero-description {
            margin-left: auto;
            margin-right: auto;
          }

          .hero-buttons {
            justify-content: center;
          }

          .history-grid {
            grid-template-columns: 1fr;

            gap: 40px;
          }

          .features-grid {
            grid-template-columns: 1fr 1fr;
          }

          .steps-grid {
            grid-template-columns: 1fr;
          }
        }

        @media (max-width: 600px) {

          .navbar {
            width: 92%;
          }

          .hero h1 {
            letter-spacing: -1px;
          }

          .hero-buttons {
            flex-direction: column;

            width: 100%;
          }

          .primary-button,
          .secondary-button {
            width: 100%;

            text-align: center;
          }

          .dashboard {
            padding: 17px;

            transform: none;
          }

          .dashboard-card-top {
            flex-direction: column;

            gap: 3px;
          }

          .medicine-info {
            flex-direction: column;

            gap: 3px;
          }

          section {
            padding: 75px 0;
          }

          .features-grid {
            grid-template-columns: 1fr;
          }

          .history-card {
            padding: 28px;
          }

          .cta-container {
            padding: 45px 28px;
          }

          .footer-container {
            flex-direction: column;

            text-align: center;
          }
        }
      `}</style>

      {/* =====================================================
          HEADER
      ===================================================== */}

      <header className="header">
        <nav className="navbar">
          <a href="#" className="logo">
            <div className="logo-icon">
              <svg
                viewBox="0 0 24 24"
                fill="none"
                xmlns="http://www.w3.org/2000/svg"
              >
                <circle
                  cx="12"
                  cy="7"
                  r="3"
                  stroke="currentColor"
                  strokeWidth="1.8"
                />

                <path
                  d="M6.5 20C6.9 15.8 8.8 13.5 12 13.5C15.2 13.5 17.1 15.8 17.5 20"
                  stroke="currentColor"
                  strokeWidth="1.8"
                  strokeLinecap="round"
                />

                <path
                  d="M12 14V18"
                  stroke="currentColor"
                  strokeWidth="1.8"
                  strokeLinecap="round"
                />

                <path
                  d="M10 16H14"
                  stroke="currentColor"
                  strokeWidth="1.8"
                  strokeLinecap="round"
                />
              </svg>
            </div>
            Farma<span>+</span>
          </a>

          <div className="nav-links">
            <a href="#historia">Nossa história</a>

            <a href="#funcionalidades">Funcionalidades</a>

            <a href="#como-funciona">Como funciona</a>

            <a href="/login" className="login-button">
              Entrar
            </a>
          </div>
        </nav>
      </header>

      <section className="hero">
        <div className="hero-container">
          <div className="hero-content">
            <div className="hero-tag">Gestão inteligente para farmácias</div>

            <h1>
              Mais controle.
              <br />
              Mais segurança.
              <br />
              <span>Mais Farma+.</span>
            </h1>

            <p className="hero-description">
              Uma solução desenvolvida para tornar o controle de receitas e
              medicamentos mais simples, organizado e seguro.
            </p>

            <div className="hero-buttons">
              <a href="#funcionalidades" className="primary-button">
                Conheça o sistema
              </a>

              <a href="#historia" className="secondary-button">
                Nossa história
              </a>
            </div>
          </div>

          <div className="hero-visual">
            <div className="dashboard">
              <div className="dashboard-header">
                <div className="dashboard-title">Controle de medicamentos</div>

                <div className="status">Sistema ativo</div>
              </div>

              <div className="dashboard-card">
                <div className="dashboard-card-top">
                  <span className="medicine-name">Amoxicilina 500mg</span>

                  <span className="prescription">RECEITA</span>
                </div>

                <div className="medicine-info">
                  <span>Estoque disponível</span>

                  <span className="stock">32 unidades</span>
                </div>
              </div>

              <div className="dashboard-card">
                <div className="dashboard-card-top">
                  <span className="medicine-name">Clonazepam 2mg</span>

                  <span className="prescription">TARJA PRETA</span>
                </div>

                <div className="medicine-info">
                  <span>Controle obrigatório</span>

                  <span className="stock">8 unidades</span>
                </div>
              </div>

              <div className="dashboard-card">
                <div className="dashboard-card-top">
                  <span className="medicine-name">Dipirona 500mg</span>

                  <span className="prescription">DISPONÍVEL</span>
                </div>

                <div className="medicine-info">
                  <span>Estoque disponível</span>

                  <span className="stock">57 unidades</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      <section className="history" id="historia">
        <div className="section-container">
          <div className="section-heading">
            <span className="section-label">Nossa história</span>

            <h2>Tecnologia para uma gestão mais segura</h2>

            <p>
              O Farma+ nasceu de uma necessidade real: tornar o controle de
              receitas médicas mais eficiente dentro das farmácias.
            </p>
          </div>

          <div className="history-grid">
            <div className="history-card">
              <div className="history-icon">
                <svg
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  strokeWidth="1.8"
                >
                  <path d="M4 19V5" strokeLinecap="round" />

                  <path
                    d="M4 5C7 3 9 7 12 5C15 3 17 7 20 5V19C17 21 15 17 12 19C9 21 7 17 4 19"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                  />
                </svg>
              </div>

              <h3>Um problema simples de explicar.</h3>

              <p>
                Processos manuais podem tornar o acompanhamento de receitas e
                medicamentos mais lento e sujeito a erros.
              </p>
            </div>

            <div className="history-text">
              <p>
                O projeto surgiu a partir da necessidade de tornar mais
                eficiente e seguro o controle de receitas médicas dentro de uma
                farmácia.
              </p>

              <p>
                Em uma rotina tradicional, o registro e a conferência das
                receitas podem depender de anotações manuais, documentos físicos
                e consultas demoradas, aumentando a possibilidade de erros e
                dificultando o acompanhamento das informações.
              </p>

              <p>
                Pensando nesse cenário, foi desenvolvido o{" "}
                <strong>Farma+</strong>, um sistema de gestão de farmácias
                voltado principalmente para o controle e gerenciamento de
                receitas médicas.
              </p>

              <p>
                A proposta é centralizar as informações em um único ambiente,
                permitindo que os responsáveis pela farmácia realizem o
                cadastro, consulta e acompanhamento das receitas de forma
                organizada.
              </p>

              <p>
                O sistema também busca auxiliar no controle dos medicamentos
                relacionados às receitas, facilitando a identificação de
                medicamentos que exigem prescrição e contribuindo para que a
                venda seja realizada de acordo com as exigências estabelecidas.
              </p>

              <p>
                Dessa forma, o principal objetivo é oferecer uma ferramenta
                simples, organizada e segura para auxiliar na gestão de receitas
                e medicamentos de uma farmácia.
              </p>
            </div>
          </div>
        </div>
      </section>

      <section className="features" id="funcionalidades">
        <div className="section-container">
          <div className="section-heading">
            <span className="section-label">Funcionalidades</span>

            <h2>Tudo em um único ambiente</h2>

            <p>
              O Farma+ foi pensado para reduzir processos manuais e facilitar o
              acompanhamento das operações da farmácia.
            </p>
          </div>

          <div className="features-grid">
            <article className="feature">
              <div className="feature-icon">✓</div>

              <h3>Controle de receitas</h3>

              <p>
                Cadastre, consulte e acompanhe receitas médicas de maneira
                organizada.
              </p>
            </article>

            <article className="feature">
              <div className="feature-icon">+</div>

              <h3>Gestão de medicamentos</h3>

              <p>
                Mantenha as informações dos medicamentos centralizadas e
                acessíveis.
              </p>
            </article>

            <article className="feature">
              <div className="feature-icon">!</div>

              <h3>Controle de prescrição</h3>

              <p>Identifique medicamentos que exigem receita antes da venda.</p>
            </article>

            <article className="feature">
              <div className="feature-icon">#</div>

              <h3>Organização</h3>

              <p>
                Reduza a dependência de registros físicos e informações
                espalhadas.
              </p>
            </article>

            <article className="feature">
              <div className="feature-icon">↓</div>

              <h3>Controle de estoque</h3>

              <p>
                Facilite o acompanhamento da quantidade disponível de
                medicamentos.
              </p>
            </article>

            <article className="feature">
              <div className="feature-icon">✓</div>

              <h3>Mais segurança</h3>

              <p>
                Tenha maior controle sobre as operações realizadas no
                estabelecimento.
              </p>
            </article>
          </div>
        </div>
      </section>

      {/* =====================================================
          COMO FUNCIONA
      ===================================================== */}

      <section className="steps" id="como-funciona">
        <div className="section-container">
          <div className="section-heading">
            <span className="section-label">Como funciona</span>

            <h2>Simples para quem utiliza</h2>

            <p>
              O sistema centraliza as principais informações em poucos passos.
            </p>
          </div>

          <div className="steps-grid">
            <div className="step">
              <div className="step-number">01</div>

              <h3>Cadastre</h3>

              <p>
                Registre medicamentos e informações relacionadas às receitas.
              </p>
            </div>

            <div className="step">
              <div className="step-number">02</div>

              <h3>Consulte</h3>

              <p>
                Encontre rapidamente as informações necessárias para realizar a
                conferência.
              </p>
            </div>

            <div className="step">
              <div className="step-number">03</div>

              <h3>Gerencie</h3>

              <p>
                Tenha maior controle sobre receitas, medicamentos e estoque.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* =====================================================
          CTA
      ===================================================== */}

      <section className="cta" id="login">
        <div className="cta-container">
          <div className="cta-content">
            <h2>Uma nova forma de cuidar da gestão da sua farmácia.</h2>

            <p>
              Centralize informações, reduza processos manuais e tenha mais
              controle sobre suas receitas e medicamentos.
            </p>

            <a href="/login" className="primary-button">
              Acessar o sistema
            </a>
          </div>
        </div>
      </section>

      {/* =====================================================
          FOOTER
      ===================================================== */}

      <footer className="footer">
        <div className="footer-container">
          <div className="footer-logo">
            Farma<span>+</span>
          </div>

          <div className="footer-text">Gestão inteligente para farmácias.</div>

          <div className="footer-text">
            © 2026 Farma+. Todos os direitos reservados.
          </div>
        </div>
      </footer>
    </>
  );
}
