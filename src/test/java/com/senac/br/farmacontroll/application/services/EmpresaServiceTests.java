package com.senac.br.farmacontroll.application.services;

import com.senac.br.farmacontroll.application.DTOs.EmpresaRequest;
import com.senac.br.farmacontroll.application.DTOs.UsuarioLogado;
import com.senac.br.farmacontroll.application.DTOs.UsuarioRequest;
import com.senac.br.farmacontroll.domain.entidade.Empresa;
import com.senac.br.farmacontroll.domain.entidade.Usuario;
import com.senac.br.farmacontroll.domain.repository.EmpresaRepository;
import com.senac.br.farmacontroll.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.junit.jupiter.api.AfterEach;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpresaServiceTests {

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmpresaService empresaService;

    @AfterEach
    void limparContexto() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void criaEmpresaPublicamenteComCredenciaisProprias() {
        when(passwordEncoder.encode("senha")).thenReturn("hash-senha");
        when(usuarioRepository.existsByEmail("admin@farma.com")).thenReturn(false);
        when(empresaRepository.save(any(Empresa.class))).thenAnswer(invocation -> {
            Empresa empresa = invocation.getArgument(0);
            empresa.setId(42L);
            return empresa;
        });
        EmpresaRequest request = new EmpresaRequest(
                "Farma Ltda.",
                "Farma",
                "12345678000199",
                "Administradora",
                "admin@farma.com",
                "senha",
                "529.982.247-25"
        );

        var resposta = empresaService.salvarEmpresa(request, 0L);

        ArgumentCaptor<Empresa> empresaCaptor = ArgumentCaptor.forClass(Empresa.class);
        verify(empresaRepository).save(empresaCaptor.capture());
        Empresa empresa = empresaCaptor.getValue();
        assertEquals(42L, resposta.id());
        assertEquals("admin@farma.com", empresa.getEmail());
        assertEquals("hash-senha", empresa.getSenha());
        assertEquals("Administradora", empresa.getNomeResponsavel());
        assertNotNull(empresa.getCpfResponsavel());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void exigeCredenciaisNoCadastroDaEmpresa() {
        EmpresaRequest request = new EmpresaRequest(
                "Farma Ltda.",
                "Farma",
                "12345678000199",
                null,
                null,
                null,
                null
        );

        assertThrows(IllegalArgumentException.class,
                () -> empresaService.salvarEmpresa(request, 0L));
        verify(empresaRepository, never()).save(any(Empresa.class));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void permiteAoAdministradorGlobalCriarUsuarioDaEmpresa() {
        autenticarAdministradorGlobal();
        when(passwordEncoder.encode("senha")).thenReturn("hash-senha");
        Empresa empresa = new Empresa();
        empresa.setId(42L);
        when(empresaRepository.findById(42L)).thenReturn(java.util.Optional.of(empresa));
        when(usuarioRepository.existsByEmail("admin@farma.com")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var resposta = empresaService.criarAdministrador(
                42L,
                new UsuarioRequest("Administradora", "admin@farma.com", "senha", "529.982.247-25")
        );

        assertNotNull(resposta);
        ArgumentCaptor<Usuario> administradorCaptor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(administradorCaptor.capture());
        assertEquals("ROLE_USER", administradorCaptor.getValue().getRole());
        assertEquals("hash-senha", administradorCaptor.getValue().getSenha());
        assertEquals(42L, administradorCaptor.getValue().getEmpresa().getId());
    }

    private void autenticarAdministradorGlobal() {
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        var identidade = new UsuarioLogado(
                1L,
                1L,
                null,
                "Administrador",
                "root@farma.com",
                null,
                "ATIVO",
                authorities
        );
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(identidade, null, authorities)
        );
    }
}
