
package com.senac.br.farmacontroll.application.services;

import com.senac.br.farmacontroll.application.DTOs.*;
import com.senac.br.farmacontroll.application.DTOs.EnumStatusUsuario;
import com.senac.br.farmacontroll.domain.entidade.Empresa;
import com.senac.br.farmacontroll.domain.entidade.Usuario;
import com.senac.br.farmacontroll.domain.repository.EmpresaRepository;
import com.senac.br.farmacontroll.domain.repository.UsuarioRepository;
import com.senac.br.farmacontroll.domain.valueObject.CPF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Value("${spring.secretkey}")
    private String secret;

    // LOGIN
    public LoginResponse validarUsuarioAutenticadoRetornaToken(LoginRequest request) {

        if (request == null
                || request.email() == null || request.email().isBlank()
                || request.senha() == null || request.senha().isBlank()) {
            return null;
        }

        String email = request.email().trim().toLowerCase();
        String senhaDigitada = request.senha();

        // Primeiro tenta o login como empresa
        Empresa empresa = empresaRepository.findByEmailIgnoreCase(email).orElse(null);

        if (empresa != null
                && empresa.getStatus() == EnumStatusEmpresa.ATIVO
                && senhaDigitada.equals(empresa.getSenha())) {

            return new LoginResponse(tokenService.gerarToken(empresa));
        }

        // Depois tenta o login como usuário
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email).orElse(null);

        if (usuario == null
                || usuario.getStatus() != EnumStatusUsuario.ATIVO
                || !senhaDigitada.equals(usuario.getSenha())) {
            return null;
        }

        Empresa empresaDoUsuario = usuario.getEmpresa();

        // Conta antiga de administrador de empresa
        if ("ROLE_EMPRESA_ADMIN".equals(usuario.getRole())
                && empresaDoUsuario != null
                && empresaDoUsuario.getEmail() == null
                && empresaDoUsuario.getStatus() == EnumStatusEmpresa.ATIVO) {

            return new LoginResponse(tokenService.gerarToken(usuario));
        }

        // Administrador global ou usuário comum
        boolean perfilPermitido =
                "ROLE_ADMIN".equals(usuario.getRole())
                        || "ROLE_USER".equals(usuario.getRole());

        boolean empresaAtiva = empresaDoUsuario == null
                || empresaDoUsuario.getStatus() == EnumStatusEmpresa.ATIVO;

        if (perfilPermitido && empresaAtiva) {
            return new LoginResponse(tokenService.gerarToken(usuario));
        }

        return null;
    }

    // LISTAR USUÁRIOS
    public List<UsuarioResponse> listarTodosUsuariosTable() {

        UsuarioLogado usuarioLogado = getUsuarioLogado();
        List<UsuarioResponse> lista = new ArrayList<>();

        // Administrador global: visualiza todos os usuários
        if (isAdmin(usuarioLogado)) {

            List<Usuario> usuarios = usuarioRepository.findAll();

            for (Usuario usuario : usuarios) {
                if (usuario.getStatus() != EnumStatusUsuario.EXCLUIDO) {
                    lista.add(new UsuarioResponse(usuario));
                }
            }

            return lista;
        }

        // Empresa: visualiza somente seus funcionários
        if (isEmpresa(usuarioLogado)) {

            List<Usuario> usuarios =
                    usuarioRepository.getUsuariosByEmpresa_Id(usuarioLogado.empresaId());

            for (Usuario usuario : usuarios) {
                if (usuario.getStatus() != EnumStatusUsuario.EXCLUIDO
                        && usuario.getEmpresa() != null
                        && usuario.getEmpresa().getId().equals(usuarioLogado.empresaId())
                        && "ROLE_USER".equals(usuario.getRole())) {

                    lista.add(new UsuarioResponse(usuario));
                }
            }

            return lista;
        }

        throw new AccessDeniedException("Usuário sem permissão para visualizar usuários.");
    }

    // CRIAR ADMINISTRADOR
    public CriarAdminResponse criarAdmin(CriarAdminRequest criarAdminRequest) {

        if (criarAdminRequest.secretKey() == null
                || !secret.equals(criarAdminRequest.secretKey())) {

            throw new IllegalArgumentException("Chave de cadastro inválida.");
        }

        Usuario usuarioAdminSalvar = new Usuario(criarAdminRequest);
        usuarioAdminSalvar.setSenha(criarAdminRequest.senha());

        usuarioRepository.save(usuarioAdminSalvar);

        return new CriarAdminResponse(
                usuarioAdminSalvar.getId(),
                "Usuario Salvo com sucesso!"
        );
    }

    // RECUPERAR USUÁRIO LOGADO
    public UsuarioLogado getUsuarioLogado() {

        var authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof UsuarioLogado)) {

            throw new AccessDeniedException("Usuário não autenticado.");
        }

        return (UsuarioLogado) authentication.getPrincipal();
    }

    // CADASTRAR E EDITAR USUÁRIO
    public UsuarioResponse salvarUsuario(UsuarioRequest usuario, Long id) {

        UsuarioLogado usuarioLogado = getUsuarioLogado();

        if (usuario == null
                || usuario.nome() == null || usuario.nome().isBlank()
                || usuario.email() == null || usuario.email().isBlank()
                || usuario.cpf() == null || usuario.cpf().isBlank()
                || (id == 0L && (usuario.senha() == null || usuario.senha().isBlank()))) {

            throw new IllegalArgumentException("Informe os dados obrigatórios do usuário.");
        }

        String email = usuario.email().trim().toLowerCase();

        // NOVO USUÁRIO
        if (id == 0L) {

            if (usuarioRepository.existsByEmail(email)
                    || empresaRepository.existsByEmail(email)) {

                throw new IllegalArgumentException(
                        "Já existe um usuário cadastrado com este e-mail."
                );
            }

            Usuario usuarioBanco;

            // Administrador global: cria usuário sem vínculo com empresa
            if (isAdmin(usuarioLogado)) {

                usuarioBanco = new Usuario(usuario);

                // Empresa: cria funcionário vinculado à própria empresa
            } else if (isEmpresa(usuarioLogado)) {

                Empresa empresa = empresaRepository
                        .findById(usuarioLogado.empresaId())
                        .orElse(null);

                if (empresa == null) {
                    throw new AccessDeniedException("Empresa não encontrada.");
                }

                usuarioBanco = new Usuario(usuario, empresa);

            } else {
                throw new AccessDeniedException(
                        "Usuário sem permissão para cadastrar usuários."
                );
            }

            usuarioBanco.setEmail(email);
            usuarioBanco.setSenha(usuario.senha());

            usuarioRepository.save(usuarioBanco);

            return new UsuarioResponse(usuarioBanco);
        }

        // EDITAR USUÁRIO EXISTENTE
        Usuario usuarioBanco = usuarioRepository.findById(id).orElse(null);

        if (usuarioBanco == null) {
            return null;
        }

        if (!usuarioDentroDoEscopo(usuarioBanco, usuarioLogado)) {
            throw new AccessDeniedException(
                    "Usuário sem permissão para editar este cadastro."
            );
        }

        if (usuarioBanco.getStatus() == EnumStatusUsuario.EXCLUIDO) {
            throw new IllegalArgumentException(
                    "Não é possível editar um usuário excluído."
            );
        }

        if (!email.equalsIgnoreCase(usuarioBanco.getEmail())
                && (usuarioRepository.existsByEmailAndIdNot(email, id)
                || empresaRepository.existsByEmail(email))) {

            throw new IllegalArgumentException(
                    "Já existe um usuário cadastrado com este e-mail."
            );
        }

        usuarioBanco.setNome(usuario.nome());
        usuarioBanco.setCpf(new CPF(usuario.cpf()));
        usuarioBanco.setEmail(email);

        if (usuario.senha() != null && !usuario.senha().isBlank()) {
            usuarioBanco.setSenha(usuario.senha());
        }

        usuarioRepository.save(usuarioBanco);

        return new UsuarioResponse(usuarioBanco);
    }

    // BUSCAR USUÁRIO POR ID
    public UsuarioResponse buscarPorId(Long id) {

        UsuarioLogado usuarioLogado = getUsuarioLogado();

        Usuario usuario = usuarioRepository.findById(id).orElse(null);

        if (usuario == null
                || usuario.getStatus() == EnumStatusUsuario.EXCLUIDO
                || !usuarioDentroDoEscopo(usuario, usuarioLogado)) {

            return null;
        }

        return new UsuarioResponse(usuario);
    }

    // ATUALIZAR STATUS, BLOQUEAR, ATIVAR OU EXCLUIR
    public boolean atualizarStatus(EnumStatusUsuario status, Long id) {

        UsuarioLogado usuarioLogado = getUsuarioLogado();

        if (status == null) {
            throw new IllegalArgumentException("Informe o status do usuário.");
        }

        if (!isAdmin(usuarioLogado) && !isEmpresa(usuarioLogado)) {
            throw new AccessDeniedException(
                    "Usuário sem permissão para alterar status."
            );
        }

        Usuario usuarioBanco = usuarioRepository.findById(id).orElse(null);

        if (usuarioBanco == null
                || usuarioBanco.getStatus() == EnumStatusUsuario.EXCLUIDO
                || !usuarioDentroDoEscopo(usuarioBanco, usuarioLogado)) {

            return false;
        }

        usuarioBanco.setStatus(status);

        usuarioRepository.save(usuarioBanco);

        return true;
    }

    // VERIFICAR SE O USUÁRIO ESTÁ DENTRO DO ESCOPO DE ACESSO
    private boolean usuarioDentroDoEscopo(
            Usuario usuario,
            UsuarioLogado usuarioLogado) {

        // Administrador global: pode acessar qualquer usuário
        if (isAdmin(usuarioLogado)) {
            return true;
        }

        // Empresa: acessa somente seus funcionários
        if (isEmpresa(usuarioLogado)) {

            return usuario.getEmpresa() != null
                    && usuario.getEmpresa().getId().equals(usuarioLogado.empresaId())
                    && "ROLE_USER".equals(usuario.getRole());
        }

        // Funcionário: acessa somente o próprio cadastro
        return usuarioLogado.usuarioId() != null
                && usuarioLogado.usuarioId().equals(usuario.getId());
    }

    // VERIFICAR ADMINISTRADOR GLOBAL
    private boolean isAdmin(UsuarioLogado usuarioLogado) {

        return usuarioLogado.authorities().stream()
                .anyMatch(authority ->
                        "ROLE_ADMIN".equals(authority.getAuthority()));
    }

    // VERIFICAR SE O LOGIN É DE UMA EMPRESA
    private boolean isEmpresa(UsuarioLogado usuarioLogado) {

        return usuarioLogado.usuarioId() == null
                && usuarioLogado.empresaId() != null;
    }

    // BUSCAR USUÁRIO LOGADO
    public UsuarioResponse BuscarUsuarioLogado() {

        UsuarioLogado usuarioLogado = getUsuarioLogado();

        if (usuarioLogado.usuarioId() == null) {
            return null;
        }

        Usuario usuarioBanco = usuarioRepository
                .findById(usuarioLogado.usuarioId())
                .orElse(null);

        if (usuarioBanco == null) {
            return null;
        }

        return new UsuarioResponse(usuarioBanco);
    }

    // BUSCAR IDENTIDADE AUTENTICADA
    public IdentidadeResponse buscarIdentidadeAutenticada() {

        return new IdentidadeResponse(getUsuarioLogado());
    }
}
