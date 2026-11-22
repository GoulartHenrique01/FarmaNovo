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

    @Autowired
    private UsuarioService usuarioService;

    @Transactional(readOnly = true)
    public List<PacienteResponse> listarTodosPacientesTable() {
        UsuarioLogado identidade = usuarioService.getUsuarioLogado();
        List<Paciente> pacientes;

        if (isEmpresa(identidade)) {
            pacientes = pacienteRepository.findByEmpresa_Id(identidade.empresaId());
        } else if (isFuncionario(identidade)) {
            pacientes = pacienteRepository.findByEmpresa_IdAndUsuario_Id(
                    identidade.empresaId(), identidade.usuarioId());
        } else {
            throw new AccessDeniedException("Usuário sem permissão para consultar pacientes.");
        }

        return pacientes.stream()
                .filter(paciente -> paciente.getStatus() != EnumStatusPaciente.EXCLUIDO)
                .map(PacienteResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public PacienteResponse buscarPorId(Long id) {
        UsuarioLogado identidade = usuarioService.getUsuarioLogado();
        Paciente paciente = buscarPacienteNoEscopo(id, identidade);

        if (paciente == null || paciente.getStatus() == EnumStatusPaciente.EXCLUIDO) {
            return null;
        }
        return new PacienteResponse(paciente);
    }

    @Transactional
    public PacienteResponse salvarPaciente(PacienteRequest request, Long id) {
        UsuarioLogado identidade = usuarioService.getUsuarioLogado();
        validarRequest(request);
        validarFuncionario(identidade);

        if (id == null || id == 0L) {
            Usuario usuario = usuarioRepository.findById(identidade.usuarioId()).orElse(null);
            if (usuario == null
                    || usuario.getEmpresa() == null
                    || !usuario.getEmpresa().getId().equals(identidade.empresaId())
                    || !"ROLE_USER".equals(usuario.getRole())
                    || usuario.getStatus() != EnumStatusUsuario.ATIVO) {
                throw new AccessDeniedException("Usuário autenticado inválido.");
            }

            Empresa empresa = empresaRepository.findById(identidade.empresaId()).orElse(null);
            if (empresa == null || empresa.getStatus() != EnumStatusEmpresa.ATIVO) {
                throw new AccessDeniedException("Empresa não encontrada ou inativa.");
            }

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

        UsuarioLogado identidade = usuarioService.getUsuarioLogado();
        validarFuncionario(identidade);

        Paciente paciente = pacienteRepository.findByIdAndEmpresa_IdAndUsuario_Id(
                id, identidade.empresaId(), identidade.usuarioId()).orElse(null);
        if (paciente == null) {
            return false;
        }

        paciente.setStatus(status);
        pacienteRepository.save(paciente);
        return true;
    }

    private Paciente buscarPacienteNoEscopo(Long id, UsuarioLogado identidade) {
        if (isEmpresa(identidade)) {
            return pacienteRepository.findByIdAndEmpresa_Id(id, identidade.empresaId()).orElse(null);
        }
        if (isFuncionario(identidade)) {
            return pacienteRepository.findByIdAndEmpresa_IdAndUsuario_Id(
                    id, identidade.empresaId(), identidade.usuarioId()).orElse(null);
        }
        throw new AccessDeniedException("Usuário sem permissão para consultar pacientes.");
    }

    private void validarFuncionario(UsuarioLogado identidade) {
        if (!isFuncionario(identidade)) {
            throw new AccessDeniedException("Somente funcionários podem cadastrar, editar ou alterar pacientes.");
        }
    }

    private void validarRequest(PacienteRequest request) {
        if (request == null || request.nome() == null || request.nome().isBlank()
                || request.cpf() == null || request.cpf().isBlank()
                || !CPF.isValidCpf(request.cpf())) {
            throw new IllegalArgumentException("Informe um nome e um CPF válido para o paciente.");
        }
    }

    private boolean isEmpresa(UsuarioLogado identidade) {
        return identidade.usuarioId() == null && identidade.empresaId() != null
                && identidade.authorities() != null
                && identidade.authorities().stream()
                .anyMatch(authority -> "ROLE_USER".equals(authority.getAuthority()));
    }

    private boolean isFuncionario(UsuarioLogado identidade) {
        return identidade.usuarioId() != null && identidade.empresaId() != null
                && identidade.authorities() != null
                && identidade.authorities().stream()
                .anyMatch(authority -> "ROLE_USER".equals(authority.getAuthority()));
    }
}
