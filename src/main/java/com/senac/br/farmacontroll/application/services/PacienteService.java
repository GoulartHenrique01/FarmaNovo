package com.senac.br.farmacontroll.application.services;

import com.senac.br.farmacontroll.application.DTOs.*;
import com.senac.br.farmacontroll.domain.entidade.Empresa;
import com.senac.br.farmacontroll.domain.entidade.Paciente;
import com.senac.br.farmacontroll.domain.repository.EmpresaRepository;
import com.senac.br.farmacontroll.domain.repository.PacienteRepository;
import com.senac.br.farmacontroll.domain.valueObject.CPF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// FLUXO: gerencia pacientes vinculados à empresa autenticada, mantendo os dados isolados entre empresas.

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private UsuarioService usuarioService;

    // Fluxo: obtém empresa pelo JWT -> consulta apenas os pacientes dela -> converte para DTO de resposta.
    public List<PacienteResponse> listarTodosPacientesTable() {
        UsuarioLogado identidade = usuarioService.getUsuarioLogado();
        exigirEmpresa(identidade);

        return pacienteRepository.findByEmpresa_Id(identidade.empresaId()).stream()
                .filter(paciente -> paciente.getStatus() != EnumStatusPaciente.EXCLUIDO)
                .map(PacienteResponse::new)
                .toList();
    }

    public PacienteResponse buscarPorId(Long id) {
        UsuarioLogado identidade = usuarioService.getUsuarioLogado();
        exigirEmpresa(identidade);
        Paciente paciente = pacienteRepository.findByIdAndEmpresa_Id(id, identidade.empresaId()).orElse(null);

        if (paciente == null || paciente.getStatus() == EnumStatusPaciente.EXCLUIDO) {
            return null;
        }
        return new PacienteResponse(paciente);
    }


    // Fluxo: valida empresa e CPF; cria ou atualiza o paciente sem permitir trocar seu vínculo de empresa.
    public PacienteResponse salvarPaciente(PacienteRequest request, Long id) {
        UsuarioLogado identidade = usuarioService.getUsuarioLogado();
        exigirEmpresa(identidade);
        validarRequest(request);

        Empresa empresa = empresaRepository.findById(identidade.empresaId()).orElse(null);
        if (empresa == null || empresa.getStatus() != EnumStatusEmpresa.ATIVO) {
            throw new AccessDeniedException("Empresa não encontrada ou inativa.");
        }

        if (id == null || id == 0L) {
            Paciente paciente = new Paciente(request, empresa);
            return new PacienteResponse(pacienteRepository.save(paciente));
        }

        Paciente paciente = pacienteRepository.findByIdAndEmpresa_Id(id, identidade.empresaId()).orElse(null);
        if (paciente == null || paciente.getStatus() == EnumStatusPaciente.EXCLUIDO) {
            return null;
        }

        paciente.setNome(request.nome().trim());
        paciente.setCpf(new CPF(request.cpf()));
        paciente.setTelefone(request.telefone());
        return new PacienteResponse(pacienteRepository.save(paciente));
    }


    public boolean atualizarStatus(EnumStatusPaciente status, Long id) {
        if (status == null) {
            throw new IllegalArgumentException("Informe o status do paciente.");
        }

        UsuarioLogado identidade = usuarioService.getUsuarioLogado();
        exigirEmpresa(identidade);
        Paciente paciente = pacienteRepository.findByIdAndEmpresa_Id(id, identidade.empresaId()).orElse(null);
        if (paciente == null || paciente.getStatus() == EnumStatusPaciente.EXCLUIDO) {
            return false;
        }

        paciente.setStatus(status);
        pacienteRepository.save(paciente);
        return true;
    }

    private void validarRequest(PacienteRequest request) {
        if (request == null || request.nome() == null || request.nome().isBlank()
                || request.cpf() == null || request.cpf().isBlank()
                || !CPF.isValidCpf(request.cpf())) {
            throw new IllegalArgumentException("Informe um nome e um CPF válido para o paciente.");
        }
    }

    private void exigirEmpresa(UsuarioLogado identidade) {
        boolean empresa = identidade.empresaId() != null
                && identidade.authorities() != null
                && identidade.authorities().stream()
                .anyMatch(authority -> "ROLE_USER".equals(authority.getAuthority()));
        if (!empresa) {
            throw new AccessDeniedException("Somente uma empresa autenticada pode gerenciar pacientes.");
        }
    }
}
