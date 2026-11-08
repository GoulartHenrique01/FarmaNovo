package com.senac.br.farmacontroll.application.services;

import com.senac.br.farmacontroll.application.DTOs.EmpresaRequest;
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
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpresaServiceTests {

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private EmpresaService empresaService;

    @Test
    void criaEmpresaEAdministradorVinculadoNaMesmaOperacao() {
        ReflectionTestUtils.setField(empresaService, "secret", "chave-teste");
        when(usuarioRepository.existsByEmail("admin@farma.com")).thenReturn(false);
        when(empresaRepository.save(any(Empresa.class))).thenAnswer(invocation -> {
            Empresa empresa = invocation.getArgument(0);
            empresa.setId(42L);
            return empresa;
        });
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));
        EmpresaRequest request = new EmpresaRequest(
                "Farma Ltda.",
                "Farma",
                "12345678000199",
                "chave-teste",
                "Administradora",
                "admin@farma.com",
                "senha",
                "529.982.247-25"
        );

        var resposta = empresaService.salvarEmpresa(request, 0L);

        ArgumentCaptor<Usuario> administradorCaptor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(administradorCaptor.capture());
        Usuario administrador = administradorCaptor.getValue();
        assertEquals(42L, resposta.id());
        assertEquals("ROLE_EMPRESA_ADMIN", administrador.getRole());
        assertEquals(42L, administrador.getEmpresa().getId());
        assertNotNull(administrador.getCpf());
    }

    @Test
    void preservaCadastroLegadoDeEmpresaSemDadosDeAdministrador() {
        ReflectionTestUtils.setField(empresaService, "secret", "chave-teste");
        when(empresaRepository.save(any(Empresa.class))).thenAnswer(invocation -> {
            Empresa empresa = invocation.getArgument(0);
            empresa.setId(42L);
            return empresa;
        });
        EmpresaRequest request = new EmpresaRequest(
                "Farma Ltda.",
                "Farma",
                "12345678000199",
                "chave-teste",
                null,
                null,
                null,
                null
        );

        var resposta = empresaService.salvarEmpresa(request, 0L);

        assertEquals(42L, resposta.id());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void permiteAoAdministradorGlobalVincularContaAEmpresaExistente() {
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
        assertEquals("ROLE_EMPRESA_ADMIN", administradorCaptor.getValue().getRole());
        assertEquals(42L, administradorCaptor.getValue().getEmpresa().getId());
    }
}
