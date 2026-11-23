package com.senac.br.farmacontroll.application.services;

import com.senac.br.farmacontroll.application.DTOs.*;
import com.senac.br.farmacontroll.domain.entidade.Empresa;
import com.senac.br.farmacontroll.domain.entidade.Usuario;
import com.senac.br.farmacontroll.domain.repository.EmpresaRepository;
import com.senac.br.farmacontroll.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

// FLUXO: valida as credenciais, gera o token e identifica a empresa ou o administrador para aplicar o escopo correto.

@Service
public class UsuarioService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Value("${spring.admin-registration-key:}")
    private String secret;

    public LoginResponse validarUsuarioAutenticadoRetornaToken(LoginRequest request) {
        if (request == null || request.email() == null || request.email().isBlank()
                || request.senha() == null || request.senha().isBlank()) {
            return null;
        }

        String email = request.email().trim().toLowerCase();
        String senha = request.senha();

        // A empresa entra com a conta única cadastrada na própria entidade Empresa.
        Empresa empresa = empresaRepository.findByEmailIgnoreCase(email).orElse(null);
        if (empresa != null && empresa.getStatus() == EnumStatusEmpresa.ATIVO
                && senha.equals(empresa.getSenha())) {
            return new LoginResponse(tokenService.gerarToken(empresa));
        }

        // A entidade Usuario é reservada ao administrador global.
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email).orElse(null);
        if (usuario == null || usuario.getStatus() != EnumStatusUsuario.ATIVO
                || !"ROLE_ADMIN".equals(usuario.getRole()) || usuario.getEmpresa() != null
                || !senha.equals(usuario.getSenha())) {
            return null;
        }

        return new LoginResponse(tokenService.gerarToken(usuario));
    }

    // Cadastro inicial do único administrador global, protegido pela chave do backend.
    public CriarAdminResponse criarAdmin(CriarAdminRequest request) {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("Configure ADMIN_REGISTRATION_KEY no ambiente antes de cadastrar o administrador.");
        }
        if (request == null || request.secretKey() == null || !secret.equals(request.secretKey())) {
            throw new IllegalArgumentException("Chave de cadastro inválida.");
        }
        if (request.nome() == null || request.nome().isBlank()
                || request.email() == null || request.email().isBlank()
                || request.senha() == null || request.senha().isBlank()
                || request.cpf() == null || request.cpf().isBlank()) {
            throw new IllegalArgumentException("Informe nome, e-mail, senha e CPF do administrador.");
        }
        if (usuarioRepository.existsByRole("ROLE_ADMIN")) {
            throw new AccessDeniedException("O administrador inicial já foi cadastrado.");
        }

        String email = request.email().trim().toLowerCase();
        if (usuarioRepository.existsByEmailIgnoreCase(email) || empresaRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("Já existe uma conta cadastrada com este e-mail.");
        }

        Usuario admin = new Usuario(request);
        admin.setEmail(email);
        usuarioRepository.save(admin);
        return new CriarAdminResponse(admin.getId(), "Administrador cadastrado com sucesso!");
    }

    public UsuarioLogado getUsuarioLogado() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof UsuarioLogado)) {
            throw new AccessDeniedException("Usuário não autenticado.");
        }
        return (UsuarioLogado) authentication.getPrincipal();
    }

    public IdentidadeResponse buscarIdentidadeAutenticada() {
        return new IdentidadeResponse(getUsuarioLogado());
    }
}
