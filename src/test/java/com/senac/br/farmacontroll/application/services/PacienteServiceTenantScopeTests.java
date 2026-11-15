package com.senac.br.farmacontroll.application.services;

import com.senac.br.farmacontroll.application.DTOs.EnumStatusUsuario;
import com.senac.br.farmacontroll.application.DTOs.PacienteRequest;
import com.senac.br.farmacontroll.application.DTOs.UsuarioLogado;
import com.senac.br.farmacontroll.domain.entidade.Empresa;
import com.senac.br.farmacontroll.domain.entidade.Paciente;
import com.senac.br.farmacontroll.domain.entidade.Usuario;
import com.senac.br.farmacontroll.domain.repository.EmpresaRepository;
import com.senac.br.farmacontroll.domain.repository.PacienteRepository;
import com.senac.br.farmacontroll.domain.repository.UsuarioRepository;
import com.senac.br.farmacontroll.domain.valueObject.CPF;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PacienteServiceTenantScopeTests {

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EmpresaRepository empresaRepository;

    @InjectMocks
    private PacienteService pacienteService;

    @AfterEach
    void limparContextoSeguranca() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void listaSomentePacientesDoUsuarioEEmpresaAutenticados() {
        autenticarComoUsuario(25L, 10L);
        when(pacienteRepository.findByEmpresa_IdAndUsuario_Id(10L, 25L))
                .thenReturn(List.of(paciente(50L, empresa(10L), usuario(25L, empresa(10L)))));

        assertEquals(1, pacienteService.listarTodosPacientesTable().size());

        verify(pacienteRepository).findByEmpresa_IdAndUsuario_Id(10L, 25L);
        verify(pacienteRepository, never()).findAll();
    }

    @Test
    void naoBuscaPacienteDeOutroUsuario() {
        autenticarComoUsuario(25L, 10L);
        when(pacienteRepository.findByIdAndEmpresa_IdAndUsuario_Id(50L, 10L, 25L))
                .thenReturn(Optional.empty());

        assertNull(pacienteService.buscarPorId(50L));
        verify(pacienteRepository, never()).findById(50L);
    }

    @Test
    void criaPacienteComVinculosDoTokenEIgnoraIdsDoCliente() {
        autenticarComoUsuario(25L, 10L);
        Empresa empresa = empresa(10L);
        Usuario usuario = usuario(25L, empresa);
        when(usuarioRepository.findById(25L)).thenReturn(Optional.of(usuario));
        when(empresaRepository.findById(10L)).thenReturn(Optional.of(empresa));
        when(pacienteRepository.save(any(Paciente.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        pacienteService.salvarPaciente(
                new PacienteRequest("Paciente", "529.982.247-25", "11999999999"),
                0L
        );

        var pacienteCaptor = org.mockito.ArgumentCaptor.forClass(Paciente.class);
        verify(pacienteRepository).save(pacienteCaptor.capture());
        assertSame(usuario, pacienteCaptor.getValue().getUsuario());
        assertSame(empresa, pacienteCaptor.getValue().getEmpresa());
    }

    @Test
    void usuarioNaoEditaNemExcluiPacienteDeTerceiro() {
        autenticarComoUsuario(25L, 10L);
        when(pacienteRepository.findByIdAndEmpresa_IdAndUsuario_Id(50L, 10L, 25L))
                .thenReturn(Optional.empty());

        assertNull(pacienteService.salvarPaciente(
                new PacienteRequest("Alterado", "529.982.247-25", ""),
                50L
        ));
        assertFalse(pacienteService.atualizarStatus(
                com.senac.br.farmacontroll.application.DTOs.EnumStatusPaciente.EXCLUIDO,
                50L
        ));
        verify(pacienteRepository, never()).save(any(Paciente.class));
    }

    @Test
    void empresaNaoTemAcessoAoCrudDePacientes() {
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        UsuarioLogado identidade = new UsuarioLogado(
                10L, null, 10L, "Empresa", "empresa@farma.com", null, "ATIVO", authorities
        );
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(identidade, null, authorities)
        );

        assertThrows(AccessDeniedException.class, () -> pacienteService.listarTodosPacientesTable());
        verify(pacienteRepository, never()).findAll();
    }

    private void autenticarComoUsuario(Long usuarioId, Long empresaId) {
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        UsuarioLogado identidade = new UsuarioLogado(
                usuarioId,
                usuarioId,
                empresaId,
                "Usuário",
                "usuario@farma.com",
                new CPF("529.982.247-25"),
                "ATIVO",
                authorities
        );
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(identidade, null, authorities)
        );
    }

    private Paciente paciente(Long id, Empresa empresa, Usuario usuario) {
        Paciente paciente = new Paciente();
        paciente.setId(id);
        paciente.setNome("Paciente");
        paciente.setCpf(new CPF("529.982.247-25"));
        paciente.setEmpresa(empresa);
        paciente.setUsuario(usuario);
        return paciente;
    }

    private Empresa empresa(Long id) {
        Empresa empresa = new Empresa();
        empresa.setId(id);
        return empresa;
    }

    private Usuario usuario(Long id, Empresa empresa) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setRole("ROLE_USER");
        usuario.setStatus(EnumStatusUsuario.ATIVO);
        usuario.setEmpresa(empresa);
        return usuario;
    }
}
