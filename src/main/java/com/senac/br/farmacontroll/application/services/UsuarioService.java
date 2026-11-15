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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${spring.secretkey}")
    private String secret;

    public LoginResponse validarUsuarioAutenticadoRetornaToken(LoginRequest request) {
        if (request == null || request.email() == null || request.email().isBlank()
                || request.senha() == null || request.senha().isBlank()) {
            return null;
        }
        String email = request.email().trim().toLowerCase();
        var empresa = empresaRepository.findByEmailIgnoreCase(email)
                .filter(cadastro -> cadastro.getStatus() == EnumStatusEmpresa.ATIVO)
                .filter(cadastro -> senhaCorreta(request.senha(), cadastro.getSenha()));
        if (empresa.isPresent()) {
            return new LoginResponse(tokenService.gerarToken(empresa.get()));
        }

        var contaLegadaDaEmpresa = usuarioRepository.findByEmailIgnoreCase(email)
                .filter(cadastro -> "ROLE_EMPRESA_ADMIN".equals(cadastro.getRole()))
                .filter(cadastro -> cadastro.getStatus() == EnumStatusUsuario.ATIVO)
                .filter(cadastro -> cadastro.getEmpresa() != null
                        && cadastro.getEmpresa().getEmail() == null
                        && cadastro.getEmpresa().getStatus() == EnumStatusEmpresa.ATIVO)
                .filter(cadastro -> senhaCorreta(request.senha(), cadastro.getSenha()));
        if (contaLegadaDaEmpresa.isPresent()) {
            return new LoginResponse(tokenService.gerarToken(contaLegadaDaEmpresa.get()));
        }

        return usuarioRepository.findByEmailIgnoreCase(email)
                .filter(cadastro -> cadastro.getStatus() == EnumStatusUsuario.ATIVO)
                .filter(cadastro -> "ROLE_ADMIN".equals(cadastro.getRole())
                        || "ROLE_USER".equals(cadastro.getRole()))
                .filter(cadastro -> cadastro.getEmpresa() == null
                        || cadastro.getEmpresa().getStatus() == EnumStatusEmpresa.ATIVO)
                .filter(cadastro -> senhaCorreta(request.senha(), cadastro.getSenha()))
                .map(cadastro -> {
                    converterSenhaLegadaParaHash(cadastro, request.senha());
                    return new LoginResponse(tokenService.gerarToken(cadastro));
                })
                .orElse(null);
    }

    private boolean senhaCorreta(String senhaInformada, String senhaArmazenada) {
        if (senhaArmazenada == null) {
            return false;
        }
        if (ehHashBcrypt(senhaArmazenada)) {
            return passwordEncoder.matches(senhaInformada, senhaArmazenada);
        }
        return senhaInformada.equals(senhaArmazenada);
    }

    // Senha antiga em texto puro é convertida para BCrypt no primeiro login bem-sucedido.
    private void converterSenhaLegadaParaHash(Usuario usuario, String senhaInformada) {
        if (!ehHashBcrypt(usuario.getSenha())) {
            usuario.setSenha(passwordEncoder.encode(senhaInformada));
            usuarioRepository.save(usuario);
        }
    }

    private boolean ehHashBcrypt(String senha) {
        return senha != null && senha.matches("^\\$2[aby]\\$\\d{2}\\$[./A-Za-z0-9]{53}$");
    }

    public List<UsuarioResponse> listarTodosUsuariosTable(){

        var usuarioLogado = getUsuarioLogado();

        if (isEmpresa(usuarioLogado)) {
            return usuarioRepository.getUsuariosByEmpresa_Id(usuarioLogado.empresaId())
                    .stream()
                    .filter(usuario -> EnumStatusUsuario.EXCLUIDO != usuario.getStatus())
                    .filter(usuario -> usuario.getEmpresa() != null
                            && usuarioLogado.empresaId().equals(usuario.getEmpresa().getId()))
                    .filter(usuario -> "ROLE_USER".equals(usuario.getRole()))
                    .map(UsuarioResponse::new)
                    .toList();
        }

        if (!isEmpresa(usuarioLogado) && !isAdmin(usuarioLogado)
                && usuarioLogado.usuarioId() != null) {
            return usuarioRepository.findById(usuarioLogado.usuarioId())
                    .filter(usuario -> usuario.getStatus() != EnumStatusUsuario.EXCLUIDO)
                    .map(usuario -> List.of(new UsuarioResponse(usuario)))
                    .orElseGet(List::of);
        }

        if (isAdmin(usuarioLogado) && usuarioLogado.empresaId() == null) {
            return (usuarioRepository.findAll()
                    .stream()
                    .filter(usuario -> usuario.getStatus() != EnumStatusUsuario.EXCLUIDO)
                    .map(UsuarioResponse::new)
                    .toList());
        }
        throw new AccessDeniedException("Usuário sem permissão para gerenciar usuários.");
    }

    public CriarAdminResponse criarAdmin(CriarAdminRequest criarAdminRequest) {

        if (criarAdminRequest.secretKey() == null
                || !secret.equals(criarAdminRequest.secretKey())) {
            throw new IllegalArgumentException("Chave de cadastro inválida.");
        }

        Usuario usuarioAdminSalvar = new Usuario(criarAdminRequest);
        usuarioAdminSalvar.setSenha(passwordEncoder.encode(criarAdminRequest.senha()));
        usuarioRepository.save(usuarioAdminSalvar);

        return new CriarAdminResponse(usuarioAdminSalvar.getId(),"Usuario Salvo com sucesso!");
    }

    public UsuarioLogado getUsuarioLogado(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof UsuarioLogado usuarioLogado)) {
            throw new AccessDeniedException("Usuário não autenticado.");
        }
        return usuarioLogado;
    }

    public UsuarioResponse salvarUsuario(UsuarioRequest usuario, Long id) {

        var usuarioLogado = getUsuarioLogado();
        if (usuario == null || usuario.nome() == null || usuario.nome().isBlank()
                || usuario.email() == null || usuario.email().isBlank()
                || usuario.cpf() == null || usuario.cpf().isBlank()
                || (id == 0L && (usuario.senha() == null || usuario.senha().isBlank()))) {
            throw new IllegalArgumentException("Informe os dados obrigatórios do usuário.");
        }

        //Novo Usuario
        if (id == 0L){
            String email = usuario.email().trim().toLowerCase();
            if (usuarioRepository.existsByEmail(email)
                    || empresaRepository.existsByEmail(email)) {
                throw new IllegalArgumentException("Já existe um usuário cadastrado com este e-mail.");
            }
            Usuario usuarioBanco;
            if (isEmpresa(usuarioLogado)) {
                Empresa empresa = empresaRepository.findById(usuarioLogado.empresaId())
                        .orElseThrow(() -> new AccessDeniedException(
                                "Empresa do usuário autenticado não encontrada."));
                usuarioBanco = new Usuario(usuario, empresa);
            } else if (isAdmin(usuarioLogado) && usuarioLogado.empresaId() == null) {
                usuarioBanco = new Usuario(usuario);
            } else {
                throw new AccessDeniedException("Usuário sem permissão para cadastrar usuários.");
            }
            usuarioBanco.setEmail(email);
            usuarioBanco.setSenha(passwordEncoder.encode(usuario.senha()));
            usuarioRepository.save(usuarioBanco);

            return new UsuarioResponse(usuarioBanco);
            //Usuario Existente
        }else{
            Usuario usuarioBanco = usuarioRepository.findById(id).orElse(null);
            if (usuarioBanco != null && usuarioDentroDoEscopo(usuarioBanco, usuarioLogado)) {
                usuarioBanco.setNome(usuario.nome());
                usuarioBanco.setCpf(new CPF(usuario.cpf()));
                String email = usuario.email().trim().toLowerCase();
                if (!usuarioBanco.getEmail().equalsIgnoreCase(email)
                        && (usuarioRepository.existsByEmailAndIdNot(email, id)
                        || empresaRepository.existsByEmail(email))) {
                    throw new IllegalArgumentException("Já existe um usuário cadastrado com este e-mail.");
                }
                usuarioBanco.setEmail(email);
                if (usuario.senha() != null && !usuario.senha().isBlank()) {
                    usuarioBanco.setSenha(passwordEncoder.encode(usuario.senha()));
                }
                usuarioRepository.save(usuarioBanco);
                return  new UsuarioResponse(usuarioBanco);
            }
        }
        return null;
    }

    public UsuarioResponse buscarPorId(Long id){

        var usuarioLogado = getUsuarioLogado();
        var usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null || !usuarioDentroDoEscopo(usuario, usuarioLogado)) {
            return null;
        }
        return new UsuarioResponse(usuario);
    }

    public boolean atualizarStatus(EnumStatusUsuario status, Long id) {
        var usuarioLogado = getUsuarioLogado();
        if (!isEmpresa(usuarioLogado) && !isAdmin(usuarioLogado)) {
            throw new AccessDeniedException("Usuários não podem alterar status de contas.");
        }
        Usuario usuarioBanco = usuarioRepository.findById(id).orElse(null);
        if (usuarioBanco != null && usuarioDentroDoEscopo(usuarioBanco, usuarioLogado)) {
            usuarioBanco.setStatus(status);
            usuarioRepository.save(usuarioBanco);
            return true;
        }
        return false;
    }

    private boolean usuarioDentroDoEscopo(Usuario usuario, UsuarioLogado usuarioLogado) {
        if (isAdmin(usuarioLogado) && usuarioLogado.empresaId() == null) {
            return true;
        }

        if (!isEmpresa(usuarioLogado) && !isAdmin(usuarioLogado)
                && usuarioLogado.usuarioId() != null
                && usuarioLogado.usuarioId().equals(usuario.getId())) {
            return true;
        }

        return isEmpresa(usuarioLogado)
                && usuario.getEmpresa() != null
                && usuario.getEmpresa().getId().equals(usuarioLogado.empresaId())
                && "ROLE_USER".equals(usuario.getRole());
    }

    private boolean isAdmin(UsuarioLogado usuarioLogado) {
        return usuarioLogado.authorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
    }

    private boolean isEmpresa(UsuarioLogado usuarioLogado) {
        return usuarioLogado.usuarioId() == null && usuarioLogado.empresaId() != null;
    }

    public UsuarioResponse BuscarUsuarioLogado() {
        var usuarioLogado = getUsuarioLogado();
        if (usuarioLogado.usuarioId() == null) {
            return null;
        }
        return usuarioRepository.findById(usuarioLogado.usuarioId())
                .map(UsuarioResponse::new)
                .orElse(null);
    }

    public IdentidadeResponse buscarIdentidadeAutenticada() {
        return new IdentidadeResponse(getUsuarioLogado());
    }
}