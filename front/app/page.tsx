'use client';

import { useRouter } from 'next/navigation';
import { useEffect, useRef, useState } from 'react';

export default function Page() {
  const [isNavOpen, setIsNavOpen] = useState(false);
  const [isLoginOpen, setIsLoginOpen] = useState(false);
  const [showLoginNote, setShowLoginNote] = useState(false);
  const emailInputRef = useRef<HTMLInputElement>(null);
  const router = useRouter();

  function openLogin(e: React.MouseEvent) {
    e.preventDefault();

    router.push("/login")
  }

  function closeLogin() {
    setIsLoginOpen(false);
    setShowLoginNote(false);
  }

  function handleOverlayClick(e: React.MouseEvent<HTMLDivElement>) {
    if (e.target === e.currentTarget) closeLogin();
  }

  function handleLoginSubmit(e: React.FormEvent) {
    e.preventDefault();
    setShowLoginNote(true);
  }

  useEffect(() => {
    if (isLoginOpen) emailInputRef.current?.focus();
  }, [isLoginOpen]);

  useEffect(() => {
    function handleKeyDown(e: KeyboardEvent) {
      if (e.key === 'Escape') closeLogin();
    }
    document.addEventListener('keydown', handleKeyDown);
    return () => document.removeEventListener('keydown', handleKeyDown);
  }, []);

  return (
    <>
      <style>{`
        :root{
          --bg:#F5FAF7;
          --bg-alt:#EAF3EE;
          --surface:#FFFFFF;
          --ink:#102A24;
          --ink-soft:#4A655F;
          --ink-faint:#7C948E;
          --blue-900:#0E3B52;
          --blue-700:#175E80;
          --blue-500:#2E86AB;
          --blue-100:#DCEEF4;
          --green-900:#123D2C;
          --green-700:#1F7A4D;
          --green-500:#33A868;
          --green-100:#DCF1E3;
          --border:#D7E6DE;
          --radius-s:8px;
          --radius-m:12px;
          --radius-l:20px;
          --shadow-soft:0 20px 40px -24px rgba(14,44,38,0.35);
          --container:1180px;
        }

        *{box-sizing:border-box;}
        html{scroll-behavior:smooth;}
        body{
          margin:0;
          background:var(--bg);
          color:var(--ink);
          font-family:'Public Sans', -apple-system, sans-serif;
          font-size:16px;
          line-height:1.6;
          -webkit-font-smoothing:antialiased;
        }
        h1,h2,h3,h4{
          font-family:'Fraunces', Georgia, serif;
          color:var(--ink);
          line-height:1.15;
          margin:0;
          font-weight:600;
        }
        p{margin:0;}
        a{color:inherit;text-decoration:none;}
        img,svg{display:block;max-width:100%;}
        ul{margin:0;padding:0;list-style:none;}
        button{font-family:inherit;cursor:pointer;}

        .container{
          width:100%;
          max-width:var(--container);
          margin:0 auto;
          padding:0 24px;
        }

        :focus-visible{
          outline:2px solid var(--blue-700);
          outline-offset:3px;
          border-radius:4px;
        }

        .btn{
          display:inline-flex;
          align-items:center;
          justify-content:center;
          gap:8px;
          padding:13px 24px;
          border-radius:var(--radius-s);
          font-weight:600;
          font-size:0.95rem;
          border:1px solid transparent;
          transition:transform .15s ease, box-shadow .15s ease, background .15s ease;
          white-space:nowrap;
        }
        .btn:active{transform:translateY(1px);}
        .btn-primary{
          background:linear-gradient(135deg, var(--blue-700), var(--green-700));
          color:#fff;
          box-shadow:0 12px 24px -12px rgba(23,94,128,0.55);
        }
        .btn-primary:hover{box-shadow:0 16px 28px -12px rgba(23,94,128,0.65);}
        .btn-ghost{
          background:transparent;
          color:var(--blue-900);
          border-color:var(--border);
        }
        .btn-ghost:hover{background:var(--bg-alt);}
        .btn-block{width:100%;}

        header{
          position:sticky;
          top:0;
          z-index:40;
          background:rgba(245,250,247,0.9);
          backdrop-filter:blur(8px);
          border-bottom:1px solid var(--border);
        }
        .nav{
          display:flex;
          align-items:center;
          justify-content:space-between;
          padding:16px 24px;
        }
        .brand{
          display:flex;
          align-items:center;
          gap:10px;
          font-family:'Fraunces', serif;
          font-weight:700;
          font-size:1.15rem;
          color:var(--ink);
        }
        .brand-mark{width:36px;height:36px;flex:none;}
        .nav-links{
          display:flex;
          align-items:center;
          gap:32px;
        }
        .nav-links a{
          font-size:0.95rem;
          font-weight:500;
          color:var(--ink-soft);
          transition:color .15s ease;
        }
        .nav-links a:hover{color:var(--ink);}
        .nav-actions{display:flex;align-items:center;gap:12px;}
        .nav-toggle{
          display:none;
          background:none;
          border:1px solid var(--border);
          border-radius:var(--radius-s);
          width:40px;height:40px;
          align-items:center;
          justify-content:center;
        }
        .nav-toggle span, .nav-toggle span::before, .nav-toggle span::after{
          content:"";
          display:block;
          width:18px;height:2px;
          background:var(--ink);
          position:relative;
          transition:transform .2s ease, opacity .2s ease;
        }
        .nav-toggle span::before{position:absolute;top:-6px;}
        .nav-toggle span::after{position:absolute;top:6px;}

        .hero{
          padding:72px 0 96px;
          overflow:hidden;
        }
        .hero .container{
          display:grid;
          grid-template-columns:1.05fr 0.95fr;
          gap:56px;
          align-items:center;
        }
        .eyebrow-chip{
          display:inline-flex;
          align-items:center;
          gap:8px;
          font-size:0.85rem;
          font-weight:600;
          color:var(--green-700);
          background:var(--green-100);
          border-radius:999px;
          padding:6px 14px 6px 10px;
          margin-bottom:22px;
        }
        .eyebrow-chip .dot{width:7px;height:7px;border-radius:50%;background:var(--green-500);}
        .hero h1{
          font-size:clamp(2.1rem, 3.6vw, 3.2rem);
          letter-spacing:-0.01em;
          max-width:15ch;
        }
        .hero-sub{
          margin-top:22px;
          font-size:1.08rem;
          color:var(--ink-soft);
          max-width:46ch;
        }
        .hero-actions{
          display:flex;
          gap:14px;
          margin-top:32px;
          flex-wrap:wrap;
        }
        .hero-chips{
          display:flex;
          gap:22px;
          margin-top:38px;
          flex-wrap:wrap;
        }
        .hero-chip{
          display:flex;
          align-items:center;
          gap:8px;
          font-size:0.88rem;
          color:var(--ink-soft);
          font-weight:500;
        }
        .hero-chip svg{width:18px;height:18px;color:var(--blue-700);flex:none;}

        .hero-visual{position:relative;}
        .mock-card{
          background:var(--surface);
          border:1px solid var(--border);
          border-radius:var(--radius-l);
          box-shadow:var(--shadow-soft);
          padding:24px;
          animation:rise .7s ease both;
        }
        @keyframes rise{
          from{opacity:0; transform:translateY(18px);}
          to{opacity:1; transform:translateY(0);}
        }
        .mock-head{
          display:flex;
          align-items:center;
          justify-content:space-between;
          padding-bottom:16px;
          border-bottom:1px solid var(--border);
          margin-bottom:18px;
        }
        .mock-title{font-family:'Fraunces',serif;font-weight:600;font-size:1.05rem;}
        .mock-sub{font-size:0.82rem;color:var(--ink-faint);margin-top:2px;}
        .status-pill{
          display:inline-flex;
          align-items:center;
          gap:6px;
          background:var(--green-100);
          color:var(--green-700);
          font-size:0.78rem;
          font-weight:700;
          padding:6px 12px;
          border-radius:999px;
        }
        .status-pill svg{width:13px;height:13px;}
        .mock-field{margin-bottom:14px;}
        .mock-field label{
          display:block;
          font-size:0.75rem;
          color:var(--ink-faint);
          margin-bottom:5px;
          font-weight:600;
        }
        .mock-field .value{
          font-size:0.95rem;
          font-weight:500;
          color:var(--ink);
        }
        .mock-meds{
          margin-top:18px;
          padding-top:16px;
          border-top:1px solid var(--border);
        }
        .med-row{
          display:flex;
          align-items:center;
          justify-content:space-between;
          padding:10px 12px;
          border-radius:var(--radius-s);
          background:var(--bg-alt);
          margin-bottom:8px;
        }
        .med-row:last-child{margin-bottom:0;}
        .med-name{font-size:0.88rem;font-weight:600;}
        .med-tag{font-size:0.72rem;color:var(--ink-faint);}
        .med-check{
          width:22px;height:22px;
          border-radius:50%;
          background:var(--green-500);
          display:flex;align-items:center;justify-content:center;
          flex:none;
          animation:pulse 2.4s ease-in-out infinite;
        }
        .med-check svg{width:12px;height:12px;color:#fff;}
        @keyframes pulse{
          0%,100%{box-shadow:0 0 0 0 rgba(51,168,104,0.35);}
          50%{box-shadow:0 0 0 6px rgba(51,168,104,0);}
        }
        .float-badge{
          position:absolute;
          background:var(--surface);
          border:1px solid var(--border);
          border-radius:var(--radius-m);
          box-shadow:var(--shadow-soft);
          padding:12px 16px;
          display:flex;
          align-items:center;
          gap:10px;
          font-size:0.82rem;
          font-weight:600;
        }
        .float-badge svg{width:20px;height:20px;color:var(--blue-700);}
        .float-badge.top-right{top:-18px;right:-14px;}

        section{padding:88px 0;}
        .section-alt{background:var(--bg-alt);}
        .section-head{
          max-width:56ch;
          margin-bottom:52px;
        }
        .section-head h2{
          font-size:clamp(1.7rem, 2.6vw, 2.3rem);
          max-width:18ch;
        }
        .section-head p{
          margin-top:14px;
          color:var(--ink-soft);
          font-size:1.02rem;
        }

        .compare{
          display:grid;
          grid-template-columns:1fr 1fr;
          gap:28px;
        }
        .compare-col{
          border-radius:var(--radius-l);
          padding:32px;
        }
        .compare-col.is-before{
          background:var(--surface);
          border:1px dashed var(--border);
        }
        .compare-col.is-after{
          background:linear-gradient(160deg, var(--blue-900), var(--green-900));
          color:#fff;
        }
        .compare-col h3{
          font-size:1.2rem;
          margin-bottom:18px;
        }
        .compare-col.is-after h3{color:#fff;}
        .compare-col ul{display:flex;flex-direction:column;gap:14px;}
        .compare-col li{
          display:flex;
          gap:12px;
          align-items:flex-start;
          font-size:0.95rem;
        }
        .compare-col.is-before li{color:var(--ink-soft);}
        .compare-col.is-after li{color:rgba(255,255,255,0.9);}
        .compare-col li svg{width:18px;height:18px;flex:none;margin-top:2px;}
        .compare-col.is-before li svg{color:#C77B4A;}
        .compare-col.is-after li svg{color:var(--green-100);}

        .features-grid{
          display:grid;
          grid-template-columns:repeat(3, 1fr);
          gap:1px;
          background:var(--border);
          border:1px solid var(--border);
          border-radius:var(--radius-l);
          overflow:hidden;
        }
        .feature-card{
          background:var(--surface);
          padding:32px 28px;
        }
        .feature-icon{
          width:44px;height:44px;
          border-radius:10px;
          display:flex;align-items:center;justify-content:center;
          margin-bottom:18px;
        }
        .feature-icon svg{width:22px;height:22px;}
        .feature-card:nth-child(odd) .feature-icon{background:var(--blue-100);color:var(--blue-700);}
        .feature-card:nth-child(even) .feature-icon{background:var(--green-100);color:var(--green-700);}
        .feature-card h3{font-size:1.05rem;margin-bottom:10px;font-weight:600;}
        .feature-card p{color:var(--ink-soft);font-size:0.92rem;}

        .steps{
          display:grid;
          grid-template-columns:repeat(3, 1fr);
          gap:32px;
        }
        .step{position:relative;padding-left:2px;}
        .step-num{
          font-family:'Fraunces',serif;
          font-size:2.2rem;
          font-weight:600;
          background:linear-gradient(135deg, var(--blue-700), var(--green-700));
          -webkit-background-clip:text;
          background-clip:text;
          color:transparent;
          margin-bottom:10px;
          display:block;
        }
        .step h3{font-size:1.1rem;margin-bottom:10px;}
        .step p{color:var(--ink-soft);font-size:0.94rem;}
        .step:not(:last-child)::after{
          content:"";
          position:absolute;
          top:20px;
          right:-16px;
          width:1px;
          height:60px;
          background:var(--border);
        }

        .historia{
          display:grid;
          grid-template-columns:0.85fr 1.15fr;
          gap:56px;
          align-items:start;
        }
        .historia-mark{
          position:sticky;
          top:110px;
        }
        .historia-mark .quote-icon{
          width:52px;height:52px;
          color:var(--blue-100);
          margin-bottom:18px;
        }
        .historia-mark h2{
          font-size:clamp(1.7rem,2.6vw,2.2rem);
          max-width:12ch;
        }
        .historia-body p{
          color:var(--ink-soft);
          font-size:1.02rem;
          margin-bottom:20px;
        }
        .historia-body p:first-child{
          font-family:'Fraunces', serif;
          font-style:italic;
          font-size:1.18rem;
          color:var(--ink);
          font-weight:500;
        }

        .cta-band{
          background:linear-gradient(135deg, var(--blue-900), var(--green-900));
          border-radius:var(--radius-l);
          padding:56px;
          display:flex;
          align-items:center;
          justify-content:space-between;
          gap:32px;
          flex-wrap:wrap;
          color:#fff;
        }
        .cta-band h2{color:#fff;font-size:clamp(1.5rem,2.4vw,2rem);max-width:20ch;}
        .cta-band p{color:rgba(255,255,255,0.78);margin-top:10px;max-width:42ch;}
        .cta-band .btn-primary{
          background:#fff;
          color:var(--blue-900);
          box-shadow:none;
        }

        footer{
          border-top:1px solid var(--border);
          padding:56px 0 32px;
        }
        .footer-top{
          display:grid;
          grid-template-columns:1.4fr 1fr 1fr;
          gap:40px;
          padding-bottom:40px;
        }
        .footer-brand p{
          color:var(--ink-soft);
          margin-top:14px;
          font-size:0.92rem;
          max-width:34ch;
        }
        .footer-col h4{
          font-family:'Public Sans', sans-serif;
          font-size:0.85rem;
          font-weight:700;
          color:var(--ink);
          margin-bottom:14px;
        }
        .footer-col ul{display:flex;flex-direction:column;gap:10px;}
        .footer-col a{font-size:0.9rem;color:var(--ink-soft);}
        .footer-col a:hover{color:var(--ink);}
        .footer-bottom{
          border-top:1px solid var(--border);
          padding-top:24px;
          display:flex;
          justify-content:space-between;
          align-items:center;
          flex-wrap:wrap;
          gap:12px;
          font-size:0.82rem;
          color:var(--ink-faint);
        }

        .modal-overlay{
          position:fixed;
          inset:0;
          background:rgba(16,42,36,0.45);
          backdrop-filter:blur(3px);
          display:none;
          align-items:center;
          justify-content:center;
          padding:20px;
          z-index:100;
        }
        .modal-overlay.is-open{display:flex;}
        .modal{
          background:var(--surface);
          border-radius:var(--radius-l);
          padding:36px;
          width:100%;
          max-width:400px;
          box-shadow:0 30px 60px -20px rgba(14,44,38,0.45);
          animation:pop .18s ease both;
        }
        @keyframes pop{
          from{opacity:0; transform:scale(0.97) translateY(6px);}
          to{opacity:1; transform:scale(1) translateY(0);}
        }
        .modal-head{
          display:flex;
          align-items:flex-start;
          justify-content:space-between;
          margin-bottom:6px;
        }
        .modal-head h3{font-size:1.3rem;}
        .modal-close{
          background:none;border:none;
          width:32px;height:32px;
          display:flex;align-items:center;justify-content:center;
          border-radius:8px;
          color:var(--ink-faint);
        }
        .modal-close:hover{background:var(--bg-alt);color:var(--ink);}
        .modal-sub{color:var(--ink-soft);font-size:0.9rem;margin-bottom:26px;}
        .field{margin-bottom:16px;}
        .field label{
          display:block;
          font-size:0.85rem;
          font-weight:600;
          margin-bottom:7px;
        }
        .field input{
          width:100%;
          padding:11px 13px;
          border-radius:var(--radius-s);
          border:1px solid var(--border);
          font-family:inherit;
          font-size:0.95rem;
          background:var(--bg);
          color:var(--ink);
        }
        .field input:focus{
          outline:2px solid var(--blue-700);
          outline-offset:1px;
          border-color:var(--blue-700);
        }
        .modal-foot{
          display:flex;
          align-items:center;
          justify-content:space-between;
          margin:6px 0 22px;
          font-size:0.85rem;
        }
        .modal-foot a{color:var(--blue-700);font-weight:600;}
        .modal-note{
          margin-top:16px;
          font-size:0.82rem;
          color:var(--ink-faint);
          background:var(--bg-alt);
          border-radius:var(--radius-s);
          padding:10px 12px;
        }

        @media (max-width: 980px){
          .hero .container{grid-template-columns:1fr;}
          .hero-visual{order:-1;max-width:460px;margin:0 auto;}
          .compare{grid-template-columns:1fr;}
          .features-grid{grid-template-columns:1fr 1fr;}
          .steps{grid-template-columns:1fr;gap:36px;}
          .step:not(:last-child)::after{display:none;}
          .historia{grid-template-columns:1fr;}
          .historia-mark{position:static;}
          .footer-top{grid-template-columns:1fr 1fr;}
        }
        @media (max-width: 720px){
          .nav-links{
            position:fixed;
            top:69px; left:0; right:0;
            background:var(--surface);
            border-bottom:1px solid var(--border);
            flex-direction:column;
            align-items:flex-start;
            gap:0;
            padding:8px 24px 18px;
            transform:translateY(-8px);
            opacity:0;
            pointer-events:none;
            transition:opacity .18s ease, transform .18s ease;
          }
          .nav-links.is-open{opacity:1;transform:translateY(0);pointer-events:auto;}
          .nav-links a{padding:12px 0;width:100%;border-bottom:1px solid var(--border);}
          .nav-toggle{display:flex;}
          .nav-actions .btn-ghost{display:none;}
          .features-grid{grid-template-columns:1fr;}
          .footer-top{grid-template-columns:1fr;gap:28px;}
          .cta-band{padding:36px 24px;flex-direction:column;align-items:flex-start;}
          .float-badge.top-right{display:none;}
        }

        @media (prefers-reduced-motion: reduce){
          *{animation-duration:0.01ms !important; animation-iteration-count:1 !important; transition-duration:0.01ms !important;}
          html{scroll-behavior:auto;}
        }
      `}</style>

      <header>
        <nav className="nav container">
          <a href="#home" className="brand" aria-label="Farma+, página inicial">
            <svg className="brand-mark" viewBox="0 0 100 100" role="img" aria-hidden="true">
              <defs>
                <linearGradient id="logoGrad" x1="0" y1="0" x2="1" y2="1">
                  <stop offset="0" stopColor="#175E80" />
                  <stop offset="1" stopColor="#1F7A4D" />
                </linearGradient>
              </defs>
              <rect width="100" height="100" rx="24" fill="url(#logoGrad)" />
              <circle cx="50" cy="24" r="13" fill="#fff" />
              <rect x="42" y="28" width="16" height="57" rx="8" fill="#fff" />
              <rect x="20" y="42" width="60" height="16" rx="8" fill="#fff" />
            </svg>
            Farma+
          </a>

          <button
            className="nav-toggle"
            aria-label="Abrir menu"
            aria-expanded={isNavOpen}
            aria-controls="navLinks"
            onClick={() => setIsNavOpen((v) => !v)}
          >
            <span></span>
          </button>

          <ul className={`nav-links ${isNavOpen ? 'is-open' : ''}`} id="navLinks">
            <li><a href="#recursos" onClick={() => setIsNavOpen(false)}>Recursos</a></li>
            <li><a href="#como-funciona" onClick={() => setIsNavOpen(false)}>Como funciona</a></li>
            <li><a href="#historia" onClick={() => setIsNavOpen(false)}>Nossa história</a></li>
          </ul>

          <div className="nav-actions">
            <button className="btn btn-ghost" onClick={openLogin}>Entrar</button>
            <button className="btn btn-primary" onClick={openLogin}>Começar agora</button>
          </div>
        </nav>
      </header>

      <main id="home">
        {/* HERO */}
        <section className="hero">
          <div className="container">
            <div className="hero-copy">
              <span className="eyebrow-chip"><span className="dot"></span>Gestão de receitas médicas</span>
              <h1>Cada receita, sob controle, do balcão ao histórico</h1>
              <p className="hero-sub">O Farma+ centraliza o cadastro, a consulta e o acompanhamento de receitas médicas da sua farmácia, ajudando a equipe a vender com segurança e dentro das exigências legais — sem depender de papel.</p>
              <div className="hero-actions">
                <button className="btn btn-primary" onClick={openLogin}>Entrar no sistema</button>
                <a href="#recursos" className="btn btn-ghost">Conhecer os recursos</a>
              </div>
              <div className="hero-chips">
                <span className="hero-chip">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M9 12l2 2 4-4" /><circle cx="12" cy="12" r="9" /></svg>
                  Cadastro centralizado
                </span>
                <span className="hero-chip">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><circle cx="11" cy="11" r="7" /><path d="M21 21l-4.3-4.3" /></svg>
                  Consulta em segundos
                </span>
                <span className="hero-chip">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M12 3l7 3v6c0 4.5-3 7.5-7 9-4-1.5-7-4.5-7-9V6l7-3z" /></svg>
                  Conforme a legislação
                </span>
              </div>
            </div>

            <div className="hero-visual">
              <div className="mock-card">
                <div className="mock-head">
                  <div>
                    <div className="mock-title">Receita #4821</div>
                    <div className="mock-sub">Cadastrada há 2 minutos</div>
                  </div>
                  <span className="status-pill">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="3" strokeLinecap="round" strokeLinejoin="round"><path d="M20 6L9 17l-5-5" /></svg>
                    Válida
                  </span>
                </div>

                <div className="mock-field">
                  <label>Paciente</label>
                  <div className="value">Marina Alves de Souza</div>
                </div>
                <div className="mock-field">
                  <label>Médico responsável (CRM)</label>
                  <div className="value">Dr. Ricardo Nunes — CRM 45.812</div>
                </div>

                <div className="mock-meds">
                  <div className="med-row">
                    <div>
                      <div className="med-name">Amoxicilina 500mg</div>
                      <div className="med-tag">Retenção de receita</div>
                    </div>
                    <span className="med-check">
                      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="3" strokeLinecap="round" strokeLinejoin="round"><path d="M20 6L9 17l-5-5" /></svg>
                    </span>
                  </div>
                  <div className="med-row">
                    <div>
                      <div className="med-name">Dipirona 1g</div>
                      <div className="med-tag">Venda livre</div>
                    </div>
                    <span className="med-check">
                      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="3" strokeLinecap="round" strokeLinejoin="round"><path d="M20 6L9 17l-5-5" /></svg>
                    </span>
                  </div>
                </div>
              </div>

              <div className="float-badge top-right">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M12 2a10 10 0 100 20 10 10 0 000-20z" /><path d="M12 6v6l4 2" /></svg>
                Histórico completo
              </div>
            </div>
          </div>
        </section>

        {/* PROBLEMA / SOLUÇÃO */}
        <section className="section-alt" id="problema">
          <div className="container">
            <div className="section-head">
              <h2>Da rotina manual ao controle digital</h2>
              <p>O registro de receitas em papel funciona até parar de funcionar: uma anotação ilegível, um documento perdido, uma consulta que demora mais do que deveria.</p>
            </div>

            <div className="compare">
              <div className="compare-col is-before">
                <h3>Sem o Farma+</h3>
                <ul>
                  <li>
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M12 9v4M12 17h.01M10.3 3.86L1.82 18a2 2 0 001.71 3h16.94a2 2 0 001.71-3L13.7 3.86a2 2 0 00-3.4 0z" /></svg>
                    Anotações manuais sujeitas a erro e perda
                  </li>
                  <li>
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M12 9v4M12 17h.01M10.3 3.86L1.82 18a2 2 0 001.71 3h16.94a2 2 0 001.71-3L13.7 3.86a2 2 0 00-3.4 0z" /></svg>
                    Documentos físicos difíceis de organizar e arquivar
                  </li>
                  <li>
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M12 9v4M12 17h.01M10.3 3.86L1.82 18a2 2 0 001.71 3h16.94a2 2 0 001.71-3L13.7 3.86a2 2 0 00-3.4 0z" /></svg>
                    Consultas demoradas no momento da venda
                  </li>
                </ul>
              </div>
              <div className="compare-col is-after">
                <h3>Com o Farma+</h3>
                <ul>
                  <li>
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M20 6L9 17l-5-5" /></svg>
                    Receitas cadastradas e organizadas em um único lugar
                  </li>
                  <li>
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M20 6L9 17l-5-5" /></svg>
                    Consulta ao histórico em poucos cliques
                  </li>
                  <li>
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M20 6L9 17l-5-5" /></svg>
                    Identificação clara de medicamentos controlados
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </section>

        {/* RECURSOS */}
        <section id="recursos">
          <div className="container">
            <div className="section-head">
              <h2>Recursos para o dia a dia da farmácia</h2>
              <p>Tudo o que a equipe do balcão precisa para cadastrar, consultar e vender com segurança.</p>
            </div>

            <div className="features-grid">
              <div className="feature-card">
                <div className="feature-icon">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M9 12h6M9 16h6M9 8h1" /><path d="M14 3H7a2 2 0 00-2 2v14a2 2 0 002 2h10a2 2 0 002-2V8l-5-5z" /><path d="M14 3v5h5" /></svg>
                </div>
                <h3>Cadastro de receitas</h3>
                <p>Registre receitas médicas em minutos, com paciente, prescritor e medicamentos organizados no sistema.</p>
              </div>

              <div className="feature-card">
                <div className="feature-icon">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><circle cx="11" cy="11" r="7" /><path d="M21 21l-4.3-4.3" /></svg>
                </div>
                <h3>Consulta rápida</h3>
                <p>Encontre qualquer receita pelo nome do paciente, do medicamento ou do médico, sem revirar arquivos.</p>
              </div>

              <div className="feature-card">
                <div className="feature-icon">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M10.5 20.5a4.95 4.95 0 007-7l-7.5-7.5a4.95 4.95 0 00-7 7l7.5 7.5z" /><path d="M8.5 8.5l7 7" /></svg>
                </div>
                <h3>Controle de medicamentos</h3>
                <p>Identifique automaticamente quais medicamentos exigem prescrição antes de concluir a venda.</p>
              </div>

              <div className="feature-card">
                <div className="feature-icon">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M12 3l7 3v6c0 4.5-3 7.5-7 9-4-1.5-7-4.5-7-9V6l7-3z" /><path d="M9.5 12l2 2 3.5-3.5" /></svg>
                </div>
                <h3>Conformidade regulatória</h3>
                <p>Mantenha a farmácia alinhada às exigências legais sobre venda de medicamentos controlados.</p>
              </div>

              <div className="feature-card">
                <div className="feature-icon">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><circle cx="12" cy="12" r="9" /><path d="M12 7v5l3 2" /></svg>
                </div>
                <h3>Histórico completo</h3>
                <p>Acompanhe o caminho de cada receita, do cadastro até a dispensação, sempre que precisar.</p>
              </div>

              <div className="feature-card">
                <div className="feature-icon">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><rect x="4" y="11" width="16" height="9" rx="2" /><path d="M8 11V7a4 4 0 118 0v4" /></svg>
                </div>
                <h3>Acesso por atendente</h3>
                <p>Cada pessoa da equipe entra com sua própria conta, com login individual e seguro.</p>
              </div>
            </div>
          </div>
        </section>

        {/* COMO FUNCIONA */}
        <section className="section-alt" id="como-funciona">
          <div className="container">
            <div className="section-head">
              <h2>Como funciona</h2>
              <p>Três passos entre a chegada da receita e a venda concluída no balcão.</p>
            </div>

            <div className="steps">
              <div className="step">
                <span className="step-num">01</span>
                <h3>Cadastre a receita</h3>
                <p>Insira os dados do paciente, do médico e dos medicamentos prescritos diretamente no sistema.</p>
              </div>
              <div className="step">
                <span className="step-num">02</span>
                <h3>Consulte e acompanhe</h3>
                <p>Veja o status e o histórico completo da receita sempre que precisar, sem depender de papel.</p>
              </div>
              <div className="step">
                <span className="step-num">03</span>
                <h3>Venda com segurança</h3>
                <p>Confirme as exigências de prescrição antes de finalizar a venda, com tudo já verificado.</p>
              </div>
            </div>
          </div>
        </section>

        {/* NOSSA HISTÓRIA */}
        <section id="historia">
          <div className="container historia">
            <div className="historia-mark">
              <svg className="quote-icon" viewBox="0 0 48 48" fill="currentColor"><path d="M14 8C7.4 11.6 4 17.2 4 24.4 4 31 8 36 14.6 36c5 0 8.6-3.8 8.6-8.6 0-4.6-3.2-8-7.4-8-.8 0-1.6.2-2.2.4.6-4.2 4-8 9-10.2L14 8zm22 0c-6.6 3.6-10 9.2-10 16.4 0 6.6 4 11.6 10.6 11.6 5 0 8.6-3.8 8.6-8.6 0-4.6-3.2-8-7.4-8-.8 0-1.6.2-2.2.4.6-4.2 4-8 9-10.2L36 8z" /></svg>
              <h2>Nossa história</h2>
            </div>
            <div className="historia-body">
              <p>O projeto surgiu da necessidade de tornar mais eficiente e seguro o controle de receitas médicas dentro de uma farmácia.</p>
              <p>Em uma rotina tradicional, o registro e a conferência das receitas podem depender de anotações manuais, documentos físicos e consultas demoradas, aumentando a possibilidade de erros e dificultando o acompanhamento das informações.</p>
              <p>Pensando nesse cenário, foi desenvolvido o Farma+, um sistema de gestão de farmácias voltado principalmente para o controle e gerenciamento de receitas médicas. A proposta é centralizar as informações em um único ambiente, permitindo que os responsáveis pela farmácia realizem o cadastro, a consulta e o acompanhamento das receitas de forma organizada.</p>
              <p>O sistema também auxilia no controle dos medicamentos relacionados às receitas, facilitando a identificação daqueles que exigem prescrição e contribuindo para que a venda seja realizada de acordo com as exigências estabelecidas.</p>
              <p>O principal objetivo do Farma+ é oferecer uma ferramenta simples, organizada e segura para a gestão de receitas e medicamentos, reduzindo processos manuais, facilitando o acesso às informações e proporcionando maior controle sobre as operações realizadas no estabelecimento.</p>
            </div>
          </div>
        </section>

        {/* CTA FINAL */}
        <section>
          <div className="container">
            <div className="cta-band">
              <div>
                <h2>Pronto para modernizar o controle de receitas da sua farmácia?</h2>
                <p>Entre no sistema e veja como o cadastro, a consulta e o acompanhamento de receitas podem ficar mais simples.</p>
              </div>
              <button className="btn btn-primary" onClick={openLogin}>Entrar no sistema</button>
            </div>
          </div>
        </section>
      </main>

      <footer>
        <div className="container">
          <div className="footer-top">
            <div className="footer-brand">
              <a href="#home" className="brand" aria-label="Farma+, página inicial">
                <svg className="brand-mark" viewBox="0 0 100 100" role="img" aria-hidden="true">
                  <rect width="100" height="100" rx="24" fill="url(#logoGrad)" />
                  <circle cx="50" cy="24" r="13" fill="#fff" />
                  <rect x="42" y="28" width="16" height="57" rx="8" fill="#fff" />
                  <rect x="20" y="42" width="60" height="16" rx="8" fill="#fff" />
                </svg>
                Farma+
              </a>
              <p>Sistema de gestão de farmácias voltado ao controle e gerenciamento de receitas médicas.</p>
            </div>

            <div className="footer-col">
              <h4>Produto</h4>
              <ul>
                <li><a href="#recursos">Recursos</a></li>
                <li><a href="#como-funciona">Como funciona</a></li>
                <li><a href="#historia">Nossa história</a></li>
              </ul>
            </div>

            <div className="footer-col">
              <h4>Acesso</h4>
              <ul>
                <li><a href="#" onClick={openLogin}>Entrar no sistema</a></li>
                <li><a href="mailto:contato@farmaplus.com.br">contato@farmaplus.com.br</a></li>
              </ul>
            </div>
          </div>

          <div className="footer-bottom">
            <span>© 2026 Farma+. Todos os direitos reservados.</span>
            <span>Feito para farmácias que levam receitas a sério.</span>
          </div>
        </div>
      </footer>

      <div
        className={`modal-overlay ${isLoginOpen ? 'is-open' : ''}`}
        role="dialog"
        aria-modal="true"
        aria-labelledby="loginTitle"
        onClick={handleOverlayClick}
      >
        <div className="modal">
          <div className="modal-head">
            <h3 id="loginTitle">Entrar no Farma+</h3>
            <button className="modal-close" aria-label="Fechar" onClick={closeLogin}>
              <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M18 6L6 18M6 6l12 12" /></svg>
            </button>
          </div>
          <p className="modal-sub">Acesse com a conta da sua farmácia.</p>

          <form onSubmit={handleLoginSubmit}>
            <div className="field">
              <label htmlFor="loginEmail">E-mail</label>
              <input ref={emailInputRef} type="email" id="loginEmail" name="email" placeholder="voce@farmacia.com.br" required />
            </div>
            <div className="field">
              <label htmlFor="loginPass">Senha</label>
              <input type="password" id="loginPass" name="senha" placeholder="••••••••" required />
            </div>
            <div className="modal-foot">
              <label style={{ display: 'flex', alignItems: 'center', gap: '6px', fontWeight: 500, color: 'var(--ink-soft)' }}>
                <input type="checkbox" style={{ width: 'auto' }} />
                Lembrar de mim
              </label>
              <a href="#">Esqueci a senha</a>
            </div>
            <button type="submit" className="btn btn-primary btn-block">Entrar</button>
            {showLoginNote && (
              <p className="modal-note">
                Ambiente de demonstração — conecte este formulário à autenticação real no seu projeto Next.js.
              </p>
            )}
          </form>
        </div>
      </div>
    </>
  );
}