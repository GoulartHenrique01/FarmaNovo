package com.senac.br.farmacontroll.application.services;

import com.senac.br.farmacontroll.application.DTOs.EnumStatusUsuario;
import com.senac.br.farmacontroll.application.DTOs.LoginRequest;
import com.senac.br.farmacontroll.application.DTOs.UsuarioLogado;
import com.senac.br.farmacontroll.application.DTOs.UsuarioRequest;
import com.senac.br.farmacontroll.domain.entidade.Empresa;
import com.senac.br.farmacontroll.domain.entidade.Usuario;
import com.senac.br.farmacontroll.domain.repository.EmpresaRepository;
import com.senac.br.farmacontroll.domain.repository.UsuarioRepository;
import com.senac.br.farmacontroll.domain.valueObject.CPF;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTenantScopeTests {

    @Mock
    private TokenService tokenService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @AfterEach
    void limparContextoSeguranca() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void criaUsuarioVinculadoAEmpresaDoAdministradorAutenticado() {
        Empresa empresa = empresa(10L);
        autenticarComoAdministradorDaEmpresa(empresa.getId());
        when(passwordEncoder.encode("senha")).thenReturn("hash-senha");
        when(empresaRepository.findById(empresa.getId())).thenReturn(Optional.of(empresa));
        when(usuarioRepository.existsByEmail("funcionario@farma.com")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        usuarioService.salvarUsuario(
                new UsuarioRequest("Funcionário", "funcionario@farma.com", "senha", "529.982.247-25"),
                0L
        );

        org.mockito.ArgumentCaptor<Usuario> usuarioCaptor =
                org.mockito.ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(usuarioCaptor.capture());
        Usuario salvo = usuarioCaptor.getValue();
        assertEquals(empresa.getId(), salvo.getEmpresa().getId());
        assertEquals("ROLE_USER", salvo.getRole());
        assertEquals("hash-senha", salvo.getSenha());
    }

    @Test
    void autenticaEmpresaComCredenciaisProprias() {
        Empresa empresa = empresa(10L);
        empresa.setEmail("empresa@farma.com");
        String senhaHash = new BCryptPasswordEncoder().encode("senha");
        empresa.setSenha(senhaHash);
        when(empresaRepository.findByEmailIgnoreCase("empresa@farma.com")).thenReturn(Optional.of(empresa));
        when(passwordEncoder.matches("senha", senhaHash)).thenReturn(true);
        when(tokenService.gerarToken(empresa)).thenReturn("token-empresa");

        var resposta = usuarioService.validarUsuarioAutenticadoRetornaToken(
                new LoginRequest("empresa@farma.com", "senha"));

        assertEquals("token-empresa", resposta.token());
        verify(usuarioRepository, never()).findByEmailIgnoreCase("empresa@farma.com");
    }

    @Test
    void autenticaUsuarioRoleUserComSenhaHash() {
        Usuario usuario = usuario("usuario@farma.com", empresa(10L), "ROLE_USER");
        String senhaHash = new BCryptPasswordEncoder().encode("senha");
        usuario.setSenha(senhaHash);
        when(empresaRepository.findByEmailIgnoreCase("usuario@farma.com")).thenReturn(Optional.empty());
        when(usuarioRepository.findByEmailIgnoreCase("usuario@farma.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senha", senhaHash)).thenReturn(true);
        when(tokenService.gerarToken(usuario)).thenReturn("token-usuario");

        var resposta = usuarioService.validarUsuarioAutenticadoRetornaToken(
                new LoginRequest("usuario@farma.com", "senha"));

        assertEquals("token-usuario", resposta.token());
    }

    @Test
    void aceitaSenhaLegadaEmTextoClaroEConverteParaHash() {
        Usuario usuario = usuario("usuario@farma.com", empresa(10L), "ROLE_USER");
        usuario.setSenha("senha-antiga");
        when(empresaRepository.findByEmailIgnoreCase("usuario@farma.com")).thenReturn(Optional.empty());
        when(usuarioRepository.findByEmailIgnoreCase("usuario@farma.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode("senha-antiga")).thenReturn("hash-novo");
        when(tokenService.gerarToken(usuario)).thenReturn("token-usuario");

        var resposta = usuarioService.validarUsuarioAutenticadoRetornaToken(
                new LoginRequest("USUARIO@FARMA.COM", "senha-antiga"));

        assertEquals("token-usuario", resposta.token());
        assertEquals("hash-novo", usuario.getSenha());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void usuarioDeEmpresaExcluidaNaoAutentica() {
        Empresa empresa = empresa(10L);
        empresa.setStatus(com.senac.br.farmacontroll.application.DTOs.EnumStatusEmpresa.EXCLUIDO);
        Usuario usuario = usuario("usuario@farma.com", empresa, "ROLE_USER");
        usuario.setSenha("senha-antiga");
        when(empresaRepository.findByEmailIgnoreCase("usuario@farma.com")).thenReturn(Optional.empty());
        when(usuarioRepository.findByEmailIgnoreCase("usuario@farma.com")).thenReturn(Optional.of(usuario));

        var resposta = usuarioService.validarUsuarioAutenticadoRetornaToken(
                new LoginRequest("usuario@farma.com", "senha-antiga"));

        assertNull(resposta);
        verify(tokenService, never()).gerarToken(usuario);
    }

    @Test
    void recuperaLoginLegadoDaEmpresaSemReutilizarContaBloqueada() {
        Empresa empresa = empresa(10L);
        Usuario contaLegada = usuario("empresa@farma.com", empresa, "ROLE_EMPRESA_ADMIN");
        contaLegada.setStatus(EnumStatusUsuario.EXCLUIDO);
        contaLegada.setSenha("senha-antiga");
        when(empresaRepository.findByEmailIgnoreCase("empresa@farma.com")).thenReturn(Optional.empty());
        when(usuarioRepository.findByEmailIgnoreCase("empresa@farma.com")).thenReturn(Optional.of(contaLegada));

        var resposta = usuarioService.validarUsuarioAutenticadoRetornaToken(
                new LoginRequest("empresa@farma.com", "senha-antiga"));

        assertNull(resposta);
        verify(tokenService, never()).gerarToken(contaLegada);
        verify(empresaRepository, never()).save(empresa);
    }

    @Test
    void listaSomenteUsuariosAtivosDaPropriaEmpresa() {
        Empresa propriaEmpresa = empresa(10L);
        Empresa outraEmpresa = empresa(20L);
        autenticarComoAdministradorDaEmpresa(propriaEmpresa.getId());
        Usuario usuarioDaEmpresa = usuario("funcionario@farma.com", propriaEmpresa, "ROLE_USER");
        Usuario usuarioDeOutraEmpresa = usuario("outra@farma.com", outraEmpresa, "ROLE_USER");
        Usuario administradorDaEmpresa = usuario("admin@farma.com", propriaEmpresa, "ROLE_EMPRESA_ADMIN");
        Usuario excluido = usuario("excluido@farma.com", propriaEmpresa, "ROLE_USER");
        excluido.setStatus(EnumStatusUsuario.EXCLUIDO);
        when(usuarioRepository.getUsuariosByEmpresa_Id(propriaEmpresa.getId()))
                .thenReturn(List.of(usuarioDaEmpresa, usuarioDeOutraEmpresa, administradorDaEmpresa, excluido));

        var resultado = usuarioService.listarTodosUsuariosTable();

        assertEquals(1, resultado.size());
        assertEquals("funcionario@farma.com", resultado.get(0).email());
    }

    @Test
    void impedeConsultaPorIdDeUsuarioDeOutraEmpresa() {
        autenticarComoAdministradorDaEmpresa(10L);
        when(usuarioRepository.findById(5L))
                .thenReturn(Optional.of(usuario("outra@farma.com", empresa(20L), "ROLE_USER")));

        assertNull(usuarioService.buscarPorId(5L));
    }

    @Test
    void impedeAtualizacaoDeUsuarioDeOutraEmpresa() {
        autenticarComoAdministradorDaEmpresa(10L);
        when(usuarioRepository.findById(5L))
                .thenReturn(Optional.of(usuario("outra@farma.com", empresa(20L), "ROLE_USER")));

        assertNull(usuarioService.salvarUsuario(
                new UsuarioRequest("Alterado", "outra@farma.com", "", "529.982.247-25"),
                5L
        ));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void impedeExclusaoDeUsuarioDeOutraEmpresa() {
        autenticarComoAdministradorDaEmpresa(10L);
        when(usuarioRepository.findById(5L))
                .thenReturn(Optional.of(usuario("outra@farma.com", empresa(20L), "ROLE_USER")));

        assertFalse(usuarioService.atualizarStatus(EnumStatusUsuario.EXCLUIDO, 5L));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    private void autenticarComoAdministradorDaEmpresa(Long empresaId) {
        var authority = new SimpleGrantedAuthority("ROLE_EMPRESA_ADMIN");
        var usuarioLogado = new UsuarioLogado(
                empresaId,
                null,
                empresaId,
                "Empresa",
                "admin@farma.com",
                new CPF("529.982.247-25"),
                "ATIVO",
                List.of(authority)
        );
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(usuarioLogado, null, usuarioLogado.authorities())
        );
    }

    private Empresa empresa(Long id) {
        Empresa empresa = new Empresa();
        empresa.setId(id);
        return empresa;
    }

    private Usuario usuario(String email, Empresa empresa, String role) {
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setEmpresa(empresa);
        usuario.setRole(role);
        usuario.setCpf(new CPF("529.982.247-25"));
        usuario.setNome("Usuário");
        return usuario;
    }
}
