package com.senac.br.farmacontroll.application.services;

import com.senac.br.farmacontroll.application.DTOs.ReceitaRequest;
import com.senac.br.farmacontroll.application.DTOs.UsuarioLogado;
import com.senac.br.farmacontroll.domain.entidade.Empresa;
import com.senac.br.farmacontroll.domain.entidade.Paciente;
import com.senac.br.farmacontroll.domain.entidade.Receita;
import com.senac.br.farmacontroll.domain.entidade.Usuario;
import com.senac.br.farmacontroll.domain.repository.EmpresaRepository;
import com.senac.br.farmacontroll.domain.repository.PacienteRepository;
import com.senac.br.farmacontroll.domain.repository.ReceitaRepository;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReceitaServiceTenantScopeTests {

    @Mock
    private ReceitaRepository receitaRepository;

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private ReceitaService receitaService;

    @AfterEach
    void limparContextoSeguranca() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void listaSomenteReceitasDoUsuarioResponsavel() {
        autenticarComoUsuario(5L, 10L);
        when(receitaRepository.findByResponsavel_IdAndEmpresa_Id(5L, 10L))
                .thenReturn(List.of(new Receita()));

        assertEquals(1, receitaService.listarTodosReceitasTable().size());

        verify(receitaRepository).findByResponsavel_IdAndEmpresa_Id(5L, 10L);
        verify(receitaRepository, never()).findAll();
    }

    @Test
    void naoBuscaReceitaDeOutroUsuarioDaMesmaEmpresa() {
        autenticarComoUsuario(5L, 10L);
        when(receitaRepository.findByIdAndResponsavel_IdAndEmpresa_Id(9L, 5L, 10L))
                .thenReturn(Optional.empty());

        assertNull(receitaService.buscarPorId(9L));

        verify(receitaRepository, never()).findById(9L);
    }

    @Test
    void associaNovaReceitaAoUsuarioEEmpresaAutenticados() {
        autenticarComoUsuario(5L, 10L);
        Empresa empresa = empresa(10L);
        Usuario responsavel = usuario(5L, empresa);
        Paciente paciente = paciente(8L, empresa, responsavel);
        when(usuarioRepository.findById(5L)).thenReturn(Optional.of(responsavel));
        when(empresaRepository.findById(10L)).thenReturn(Optional.of(empresa));
        when(pacienteRepository.findByIdAndEmpresa_IdAndUsuario_Id(8L, 10L, 5L))
                .thenReturn(Optional.of(paciente));
        when(receitaRepository.save(any(Receita.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        receitaService.salvarReceita(
                new ReceitaRequest(
                        LocalDate.now(), LocalDate.now().plusDays(30), "Diagnóstico", "", "Simples", 8L),
                0L
        );

        var receitaCaptor = org.mockito.ArgumentCaptor.forClass(Receita.class);
        verify(receitaRepository).save(receitaCaptor.capture());
        assertEquals(5L, receitaCaptor.getValue().getResponsavel().getId());
        assertEquals(10L, receitaCaptor.getValue().getEmpresa().getId());
        assertEquals(8L, receitaCaptor.getValue().getPaciente().getId());
    }

    @Test
    void empresaConsultaReceitasDaSuaEmpresaSemExporPaciente() {
        autenticarComoEmpresa(10L);
        Receita receita = new Receita();
        Paciente paciente = paciente(8L, empresa(10L), usuario(5L, empresa(10L)));
        receita.setPaciente(paciente);

        when(receitaRepository.findByEmpresa_Id(10L)).thenReturn(List.of(receita));

        assertNull(receitaService.listarTodosReceitasTable().get(0).pacienteId());
        verify(receitaRepository).findByEmpresa_Id(10L);
    }

    @Test
    void empresaNaoPodeCadastrarReceitaEmNomeDeUsuario() {
        autenticarComoEmpresa(10L);

        assertThrows(org.springframework.security.access.AccessDeniedException.class,
                () -> receitaService.salvarReceita(
                        new ReceitaRequest(
                                LocalDate.now(), LocalDate.now().plusDays(30), "Diagnóstico", "", "Simples", 8L),
                        0L
                ));
        verify(receitaRepository, never()).save(any(Receita.class));
    }

    @Test
    void empresaNaoPodeAtribuirReceitaAUsuarioDeOutraEmpresa() {
        autenticarComoEmpresa(10L);

        assertThrows(org.springframework.security.access.AccessDeniedException.class,
                () -> receitaService.salvarReceita(
                        new ReceitaRequest(
                                LocalDate.now(), LocalDate.now().plusDays(30), "Diagnóstico", "", "Simples", 5L),
                        0L
                ));

        verify(receitaRepository, never()).save(any(Receita.class));
    }

    @Test
    void naoAssociaPacienteDeOutroUsuarioAReceita() {
        autenticarComoUsuario(5L, 10L);
        Empresa empresa = empresa(10L);
        Usuario responsavel = usuario(5L, empresa);
        when(usuarioRepository.findById(5L)).thenReturn(Optional.of(responsavel));
        when(empresaRepository.findById(10L)).thenReturn(Optional.of(empresa));
        when(pacienteRepository.findByIdAndEmpresa_IdAndUsuario_Id(8L, 10L, 5L))
                .thenReturn(Optional.empty());

        assertThrows(org.springframework.security.access.AccessDeniedException.class,
                () -> receitaService.salvarReceita(
                        new ReceitaRequest(
                                LocalDate.now(), LocalDate.now().plusDays(30), "Diagnóstico", "", "Simples", 8L),
                        0L
                ));
        verify(receitaRepository, never()).save(any(Receita.class));
    }

    @Test
    void empresaNaoAlteraReceitaDeOutraEmpresa() {
        autenticarComoEmpresa(10L);
        when(receitaRepository.findByIdAndEmpresa_Id(9L, 10L)).thenReturn(Optional.empty());

        assertNull(receitaService.salvarReceita(
                new ReceitaRequest(LocalDate.now(), LocalDate.now().plusDays(30), "Alteração", "", "Simples"),
                9L
        ));

        verify(receitaRepository, never()).save(any(Receita.class));
    }

    private void autenticarComoUsuario(Long usuarioId, Long empresaId) {
        autenticar(new UsuarioLogado(
                usuarioId,
                usuarioId,
                empresaId,
                "Usuário",
                "usuario@farma.com",
                new CPF("529.982.247-25"),
                "ATIVO",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        ));
    }

    private void autenticarComoEmpresa(Long empresaId) {
        autenticar(new UsuarioLogado(
                empresaId,
                null,
                empresaId,
                "Empresa",
                "empresa@farma.com",
                new CPF("529.982.247-25"),
                "ATIVO",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        ));
    }

    private void autenticar(UsuarioLogado usuarioLogado) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        usuarioLogado, null, usuarioLogado.authorities())
        );
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
        usuario.setEmpresa(empresa);
        return usuario;
    }

    private Paciente paciente(Long id, Empresa empresa, Usuario usuario) {
        Paciente paciente = new Paciente();
        paciente.setId(id);
        paciente.setEmpresa(empresa);
        paciente.setUsuario(usuario);
        paciente.setStatus(com.senac.br.farmacontroll.application.DTOs.EnumStatusPaciente.ATIVO);
        return paciente;
    }
}
