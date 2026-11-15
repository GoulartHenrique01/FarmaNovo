package com.senac.br.farmacontroll.application.services;

import com.senac.br.farmacontroll.domain.entidade.Empresa;
import com.senac.br.farmacontroll.domain.entidade.Medicamento;
import com.senac.br.farmacontroll.domain.repository.EmpresaRepository;
import com.senac.br.farmacontroll.domain.repository.MedicamentoRepository;
import com.senac.br.farmacontroll.domain.valueObject.CPF;
import com.senac.br.farmacontroll.application.DTOs.MedicamentoRequest;
import com.senac.br.farmacontroll.application.DTOs.UsuarioLogado;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicamentoServiceTenantScopeTests {

    @Mock
    private MedicamentoRepository medicamentoRepository;

    @Mock
    private EmpresaRepository empresaRepository;

    @InjectMocks
    private MedicamentoService medicamentoService;

    @AfterEach
    void limparContextoSeguranca() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void listaMedicamentosSomenteDaEmpresaAutenticada() {
        autenticar(10L, "ROLE_EMPRESA_ADMIN");
        when(medicamentoRepository.findByEmpresa_Id(10L)).thenReturn(List.of(new Medicamento()));

        assertEquals(1, medicamentoService.listarTodosMedicamentosTable().size());

        verify(medicamentoRepository).findByEmpresa_Id(10L);
        verify(medicamentoRepository, never()).findAll();
    }

    @Test
    void naoBuscaMedicamentoDeOutraEmpresa() {
        autenticar(10L, "ROLE_EMPRESA_ADMIN");

        assertNull(medicamentoService.buscarPorId(9L));

        verify(medicamentoRepository).findByIdAndEmpresa_Id(9L, 10L);
        verify(medicamentoRepository, never()).findById(9L);
    }

    @Test
    void associaNovoMedicamentoAEmpresaAutenticada() {
        autenticar(10L, "ROLE_EMPRESA_ADMIN");
        Empresa empresa = new Empresa();
        empresa.setId(10L);
        when(empresaRepository.findById(10L)).thenReturn(Optional.of(empresa));
        when(medicamentoRepository.save(any(Medicamento.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        medicamentoService.salvarMedicamento(
                new MedicamentoRequest("Medicamento", "Comum", LocalDate.now(), 10, "mg", 1, "Marca", "Não"),
                0L
        );

        var medicamentoCaptor = org.mockito.ArgumentCaptor.forClass(Medicamento.class);
        verify(medicamentoRepository).save(medicamentoCaptor.capture());
        assertEquals(10L, medicamentoCaptor.getValue().getEmpresa().getId());
    }

    private void autenticar(Long empresaId, String role) {
        var authorities = List.of(new SimpleGrantedAuthority(role));
        var usuarioLogado = new UsuarioLogado(
                empresaId,
                null,
                empresaId,
                "Empresa",
                "admin@farma.com",
                new CPF("529.982.247-25"),
                "ATIVO",
                authorities
        );
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(usuarioLogado, null, authorities)
        );
    }
}
