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

    public LoginResponse validarUsuarioAutenticadoRetornaToken(LoginRequest request) {

        if (usuarioRepository.existsUsuarioByEmailAndSenha(request.email(), request.senha())) {

            var token = tokenService.gerarToken(request);
            //Gerar o token
            return new LoginResponse(token);
        }
        return null;
    }

    public List<UsuarioResponse> listarTodosUsuariosTable(){

        var usuarioLogado = getUsuarioLogado();

        if (isEmpresaAdmin(usuarioLogado)) {
            return usuarioRepository.getUsuariosByEmpresa_Id(usuarioLogado.empresa_id())
                    .stream()
                    .filter(usuario -> EnumStatusUsuario.EXCLUIDO != usuario.getStatus())
                    .filter(usuario -> usuario.getEmpresa() != null
                            && usuarioLogado.empresa_id().equals(usuario.getEmpresa().getId()))
                    .filter(usuario -> "ROLE_USER".equals(usuario.getRole()))
                    .map(UsuarioResponse::new)
                    .toList();
        }

        if (isAdmin(usuarioLogado) && usuarioLogado.empresa_id() == null) {
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
        usuarioRepository.save(usuarioAdminSalvar);

        return new CriarAdminResponse(usuarioAdminSalvar.getId(),"Usuario Salvo com sucesso!");
    }

    public UsuarioLogado getUsuarioLogado(){
        return (UsuarioLogado) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public UsuarioResponse salvarUsuario(UsuarioRequest usuario, Long id) {

        var usuarioLogado = getUsuarioLogado();

        //Novo Usuario
        if (id == 0L){
            if (usuarioRepository.existsByEmail(usuario.email())) {
                throw new IllegalArgumentException("Já existe um usuário cadastrado com este e-mail.");
            }
            Usuario usuarioBanco;
            if (isEmpresaAdmin(usuarioLogado)) {
                Empresa empresa = empresaRepository.findById(usuarioLogado.empresa_id())
                        .orElseThrow(() -> new AccessDeniedException(
                                "Empresa do usuário autenticado não encontrada."));
                usuarioBanco = new Usuario(usuario, empresa);
            } else if (isAdmin(usuarioLogado) && usuarioLogado.empresa_id() == null) {
                usuarioBanco = new Usuario(usuario);
            } else {
                throw new AccessDeniedException("Usuário sem permissão para cadastrar usuários.");
            }
            usuarioRepository.save(usuarioBanco);

            return new UsuarioResponse(usuarioBanco);
            //Usuario Existente
        }else{
            Usuario usuarioBanco = usuarioRepository.findById(id).orElse(null);
            if (usuarioBanco != null && usuarioDentroDoEscopo(usuarioBanco, usuarioLogado)) {
                if (!usuarioBanco.getEmail().equals(usuario.email())
                        && usuarioRepository.existsByEmailAndIdNot(usuario.email(), id)) {
                    throw new IllegalArgumentException("Já existe um usuário cadastrado com este e-mail.");
                }
                usuarioBanco.setNome(usuario.nome());
                usuarioBanco.setCpf(new CPF(usuario.cpf()));
                usuarioBanco.setEmail(usuario.email());
                if (usuario.senha() != null && !usuario.senha().isBlank()) {
                    usuarioBanco.setSenha(usuario.senha());
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
        Usuario usuarioBanco = usuarioRepository.findById(id).orElse(null);
        if (usuarioBanco != null && usuarioDentroDoEscopo(usuarioBanco, usuarioLogado)) {
            usuarioBanco.setStatus(status);
            usuarioRepository.save(usuarioBanco);
            return true;
        }
        return false;
    }

    private boolean usuarioDentroDoEscopo(Usuario usuario, UsuarioLogado usuarioLogado) {
        if (isAdmin(usuarioLogado) && usuarioLogado.empresa_id() == null) {
            return true;
        }

        return isEmpresaAdmin(usuarioLogado)
                && usuario.getEmpresa() != null
                && usuario.getEmpresa().getId().equals(usuarioLogado.empresa_id())
                && "ROLE_USER".equals(usuario.getRole());
    }

    private boolean isAdmin(UsuarioLogado usuarioLogado) {
        return usuarioLogado.authorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
    }

    private boolean isEmpresaAdmin(UsuarioLogado usuarioLogado) {
        return usuarioLogado.empresa_id() != null
                && usuarioLogado.authorities().stream()
                .anyMatch(authority -> "ROLE_EMPRESA_ADMIN".equals(authority.getAuthority()));
    }

    public UsuarioResponse BuscarUsuarioLogado() {
        try {
            var usuarioLogado = getUsuarioLogado();
            return usuarioRepository.findById(usuarioLogado.id()).
                    stream()
                    .map(UsuarioResponse::new)
                    .findFirst().orElse(null);
        }catch (Exception e){
            throw new RuntimeException("Erro ao buscar usuario logado");
        }
    }
}