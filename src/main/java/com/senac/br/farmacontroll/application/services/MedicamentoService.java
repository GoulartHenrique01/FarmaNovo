package com.senac.br.farmacontroll.application.services;

import com.senac.br.farmacontroll.application.DTOs.*;
import com.senac.br.farmacontroll.domain.entidade.Empresa;
import com.senac.br.farmacontroll.domain.entidade.Medicamento;
import com.senac.br.farmacontroll.domain.repository.EmpresaRepository;
import com.senac.br.farmacontroll.domain.repository.MedicamentoRepository;
import com.senac.br.farmacontroll.domain.valueObject.DataValidade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// FLUXO: gerencia o catálogo de medicamentos da empresa autenticada e valida o escopo antes de gravar ou consultar registros.

@Service
public class MedicamentoService {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Transactional(readOnly = true)
    public List<MedicamentoResponse> listarTodosMedicamentosTable() {
        UsuarioLogado identidade = usuarioService.getUsuarioLogado();
        exigirEmpresa(identidade);
        return medicamentoRepository.findByEmpresa_Id(identidade.empresaId()).stream()
                .filter(medicamento -> medicamento.getStatus() != EnumStatusMedicamento.EXCLUIDO)
                .map(MedicamentoResponse::new)
                .toList();
    }

    @Transactional
    public CriarMedicamentoResponse criarTipo(CriarMedicamentoRequest request) {
        UsuarioLogado identidade = usuarioService.getUsuarioLogado();
        Empresa empresa = buscarEmpresaAtiva(identidade);
        Medicamento medicamento = new Medicamento(request);
        medicamento.setEmpresa(empresa);
        medicamentoRepository.save(medicamento);
        return new CriarMedicamentoResponse(medicamento.getId(), medicamento.getTipo());
    }

    @Transactional
    public MedicamentoResponse salvarMedicamento(MedicamentoRequest request, Long id) {
        UsuarioLogado identidade = usuarioService.getUsuarioLogado();
        Empresa empresa = buscarEmpresaAtiva(identidade);
        validarMedicamento(request);

        Medicamento medicamento;
        if (id == null || id == 0L) {
            medicamento = new Medicamento();
            medicamento.setEmpresa(empresa);
            medicamento.setStatus(EnumStatusMedicamento.ATIVO);
        } else {
            medicamento = medicamentoRepository.findByIdAndEmpresa_Id(id, empresa.getId()).orElse(null);
            if (medicamento == null || medicamento.getStatus() == EnumStatusMedicamento.EXCLUIDO) {
                return null;
            }
        }

        medicamento.setNome(request.nome().trim());
        medicamento.setTipo(request.tipo().trim());
        medicamento.setDataValidade(new DataValidade(request.dataValidade()));
        medicamento.setDosagem(request.dosagem());
        medicamento.setUnidadeDosagem(request.unidadeDosagem());
        medicamento.setQuantidade(request.quantidade());
        medicamento.setMarca(request.marca());
        medicamento.setPrecisaReceita(request.precisaReceita());
        return new MedicamentoResponse(medicamentoRepository.save(medicamento));
    }

    @Transactional(readOnly = true)
    public MedicamentoResponse buscarPorId(Long id) {
        UsuarioLogado identidade = usuarioService.getUsuarioLogado();
        exigirEmpresa(identidade);
        Medicamento medicamento = medicamentoRepository
                .findByIdAndEmpresa_Id(id, identidade.empresaId()).orElse(null);
        if (medicamento == null || medicamento.getStatus() == EnumStatusMedicamento.EXCLUIDO) {
            return null;
        }
        return new MedicamentoResponse(medicamento);
    }

    @Transactional
    public boolean atualizarStatus(EnumStatusMedicamento status, Long id) {
        if (status == null) {
            throw new IllegalArgumentException("Informe o status do medicamento.");
        }
        UsuarioLogado identidade = usuarioService.getUsuarioLogado();
        exigirEmpresa(identidade);
        Medicamento medicamento = medicamentoRepository
                .findByIdAndEmpresa_Id(id, identidade.empresaId()).orElse(null);
        if (medicamento == null || medicamento.getStatus() == EnumStatusMedicamento.EXCLUIDO) {
            return false;
        }
        medicamento.setStatus(status);
        medicamentoRepository.save(medicamento);
        return true;
    }

    public boolean excluir(Long id) {
        return atualizarStatus(EnumStatusMedicamento.EXCLUIDO, id);
    }

    private Empresa buscarEmpresaAtiva(UsuarioLogado identidade) {
        exigirEmpresa(identidade);
        Empresa empresa = empresaRepository.findById(identidade.empresaId()).orElse(null);
        if (empresa == null || empresa.getStatus() != EnumStatusEmpresa.ATIVO) {
            throw new AccessDeniedException("Empresa não encontrada ou inativa.");
        }
        return empresa;
    }

    private void exigirEmpresa(UsuarioLogado identidade) {
        boolean permitido = identidade.empresaId() != null
                && identidade.authorities() != null
                && identidade.authorities().stream()
                .anyMatch(authority -> "ROLE_USER".equals(authority.getAuthority()));
        if (!permitido) {
            throw new AccessDeniedException("Somente uma empresa autenticada pode gerenciar medicamentos.");
        }
    }

    private void validarMedicamento(MedicamentoRequest request) {
        if (request == null || request.nome() == null || request.nome().isBlank()
                || request.tipo() == null || request.tipo().isBlank()
                || request.dataValidade() == null
                || request.unidadeDosagem() == null || request.unidadeDosagem().isBlank()
                || request.quantidade() < 0
                || request.precisaReceita() == null || request.precisaReceita().isBlank()) {
            throw new IllegalArgumentException("Informe os dados obrigatórios do medicamento.");
        }
    }
}
