package com.senac.br.farmacontroll.application.services;

import com.senac.br.farmacontroll.application.DTOs.*;
import com.senac.br.farmacontroll.domain.entidade.Empresa;
import com.senac.br.farmacontroll.domain.entidade.Paciente;
import com.senac.br.farmacontroll.domain.entidade.Usuario;
import com.senac.br.farmacontroll.domain.repository.EmpresaRepository;
import com.senac.br.farmacontroll.domain.repository.PacienteRepository;
import com.senac.br.farmacontroll.domain.repository.UsuarioRepository;
import com.senac.br.farmacontroll.domain.valueObject.CPF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Transactional(readOnly = true)
    public List<PacienteResponse> listarTodosPacientesTable() {
        UsuarioLogado identidade = getUsuarioLogado();
        return pacienteRepository.findByEmpresa_IdAndUsuario_Id(
                        identidade.empresaId(), identidade.usuarioId())
                .stream()
                .filter(paciente -> paciente.getStatus() != EnumStatusPaciente.EXCLUIDO)
                .map(PacienteResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public PacienteResponse buscarPorId(Long id) {
        UsuarioLogado identidade = getUsuarioLogado();
        return pacienteRepository.findByIdAndEmpresa_IdAndUsuario_Id(
                        id, identidade.empresaId(), identidade.usuarioId())
                .filter(paciente -> paciente.getStatus() != EnumStatusPaciente.EXCLUIDO)
                .map(PacienteResponse::new)
                .orElse(null);
    }

    @Transactional
    public PacienteResponse salvarPaciente(PacienteRequest request, Long id) {
        UsuarioLogado identidade = getUsuarioLogado();
        validarRequest(request);

        if (id == 0L) {
            Usuario usuario = usuarioRepository.findById(identidade.usuarioId())
                    .filter(cadastro -> cadastro.getEmpresa() != null
                            && cadastro.getEmpresa().getId().equals(identidade.empresaId())
                            && "ROLE_USER".equals(cadastro.getRole())
                            && cadastro.getStatus() == EnumStatusUsuario.ATIVO)
                    .orElseThrow(() -> new AccessDeniedException("Usuário autenticado inválido."));
            Empresa empresa = empresaRepository.findById(identidade.empresaId())
                    .orElseThrow(() -> new AccessDeniedException("Empresa do usuário autenticado não encontrada."));

            Paciente paciente = new Paciente(request, usuario, empresa);
            return new PacienteResponse(pacienteRepository.save(paciente));
        }

        Paciente paciente = pacienteRepository.findByIdAndEmpresa_IdAndUsuario_Id(
                id, identidade.empresaId(), identidade.usuarioId()).orElse(null);
        if (paciente == null || paciente.getStatus() == EnumStatusPaciente.EXCLUIDO) {
            return null;
        }

        paciente.setNome(request.nome().trim());
        paciente.setCpf(new CPF(request.cpf()));
        paciente.setTelefone(request.telefone());
        return new PacienteResponse(pacienteRepository.save(paciente));
    }

    @Transactional
    public boolean atualizarStatus(EnumStatusPaciente status, Long id) {
        if (status == null) {
            throw new IllegalArgumentException("Informe o status do paciente.");
        }
        UsuarioLogado identidade = getUsuarioLogado();
        Paciente paciente = pacienteRepository.findByIdAndEmpresa_IdAndUsuario_Id(
                id, identidade.empresaId(), identidade.usuarioId()).orElse(null);
        if (paciente == null) {
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

    private UsuarioLogado getUsuarioLogado() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof UsuarioLogado identidade)
                || identidade.usuarioId() == null || identidade.empresaId() == null
                || identidade.authorities().stream()
                .noneMatch(authority -> "ROLE_USER".equals(authority.getAuthority()))) {
            throw new AccessDeniedException("Somente usuários vinculados a uma empresa podem acessar pacientes.");
        }
        return identidade;
    }
}
