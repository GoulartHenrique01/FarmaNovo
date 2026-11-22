package com.senac.br.farmacontroll.application.services;

import com.senac.br.farmacontroll.application.DTOs.*;
import com.senac.br.farmacontroll.domain.entidade.Empresa;
import com.senac.br.farmacontroll.domain.entidade.Paciente;
import com.senac.br.farmacontroll.domain.entidade.Receita;
import com.senac.br.farmacontroll.domain.entidade.Usuario;
import com.senac.br.farmacontroll.domain.repository.EmpresaRepository;
import com.senac.br.farmacontroll.domain.repository.PacienteRepository;
import com.senac.br.farmacontroll.domain.repository.ReceitaRepository;
import com.senac.br.farmacontroll.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReceitaService {

    @Autowired
    private ReceitaRepository receitaRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Transactional(readOnly = true)
    public List<ReceitaResponse> listarTodosReceitasTable() {
        UsuarioLogado identidade = usuarioService.getUsuarioLogado();
        List<Receita> receitas;

        if (isAdministradorGlobal(identidade)) {
            receitas = receitaRepository.findAll();
        } else if (isEmpresa(identidade)) {
            receitas = receitaRepository.findByEmpresa_Id(identidade.empresaId());
        } else if (isFuncionario(identidade)) {
            receitas = receitaRepository.findByResponsavel_IdAndEmpresa_Id(
                    identidade.usuarioId(), identidade.empresaId());
        } else {
            throw new AccessDeniedException("Usuário sem permissão para consultar receitas.");
        }

        return receitas.stream()
                .filter(receita -> receita.getStatus() != EnumStatusReceita.EXCLUIDO)
                .map(ReceitaResponse::new)
                .toList();
    }

    @Transactional
    public ReceitaResponse salvarReceita(ReceitaRequest request, Long id) {
        UsuarioLogado identidade = usuarioService.getUsuarioLogado();
        validarReceita(request);

        if (id == null || id == 0L) {
            return criarReceita(request, identidade);
        }

        Receita receitaBanco = buscarNoEscopo(id, identidade);
        if (receitaBanco == null || receitaBanco.getStatus() == EnumStatusReceita.EXCLUIDO) {
            return null;
        }

        if (request.pacienteId() != null) {
            receitaBanco.setPaciente(buscarPacienteDaEmpresa(
                    request.pacienteId(), receitaBanco.getEmpresa().getId(), identidade));
        }

        aplicarDados(receitaBanco, request);
        receitaRepository.save(receitaBanco);
        return new ReceitaResponse(receitaBanco);
    }

    private ReceitaResponse criarReceita(ReceitaRequest request, UsuarioLogado identidade) {
        if (isAdministradorGlobal(identidade)) {
            throw new AccessDeniedException("O administrador global não pode emitir receitas.");
        }

        if (!isEmpresa(identidade) && !isFuncionario(identidade)) {
            throw new AccessDeniedException("Usuário sem permissão para cadastrar receitas.");
        }

        Empresa empresa = empresaRepository.findById(identidade.empresaId()).orElse(null);
        if (empresa == null || empresa.getStatus() != EnumStatusEmpresa.ATIVO) {
            throw new AccessDeniedException("Empresa não encontrada ou inativa.");
        }

        Usuario responsavel = null;
        if (isFuncionario(identidade)) {
            responsavel = usuarioRepository.findById(identidade.usuarioId()).orElse(null);
            if (responsavel == null
                    || responsavel.getEmpresa() == null
                    || !responsavel.getEmpresa().getId().equals(identidade.empresaId())
                    || !"ROLE_USER".equals(responsavel.getRole())
                    || responsavel.getStatus() != EnumStatusUsuario.ATIVO) {
                throw new AccessDeniedException("Usuário autenticado inválido.");
            }
        }

        Paciente paciente = buscarPacienteDaEmpresa(
                request.pacienteId(), identidade.empresaId(), identidade);

        Receita receitaBanco = new Receita();
        aplicarDados(receitaBanco, request);
        receitaBanco.setStatus(EnumStatusReceita.ATIVO);
        receitaBanco.setEmpresa(empresa);
        receitaBanco.setResponsavel(responsavel);
        receitaBanco.setPaciente(paciente);

        receitaRepository.save(receitaBanco);
        return new ReceitaResponse(receitaBanco);
    }

    @Transactional(readOnly = true)
    public ReceitaResponse buscarPorId(Long id) {
        Receita receitaBanco = buscarNoEscopo(id, usuarioService.getUsuarioLogado());
        if (receitaBanco == null || receitaBanco.getStatus() == EnumStatusReceita.EXCLUIDO) {
            return null;
        }
        return new ReceitaResponse(receitaBanco);
    }

    @Transactional
    public boolean atualizarStatus(EnumStatusReceita status, Long id) {
        if (status == null) {
            throw new IllegalArgumentException("Informe o status da receita.");
        }

        Receita receitaBanco = buscarNoEscopo(id, usuarioService.getUsuarioLogado());
        if (receitaBanco == null || receitaBanco.getStatus() == EnumStatusReceita.EXCLUIDO) {
            return false;
        }

        receitaBanco.setStatus(status);
        receitaRepository.save(receitaBanco);
        return true;
    }

    private void validarReceita(ReceitaRequest request) {
        if (request == null
                || request.dataEmissao() == null
                || request.dataValidade() == null
                || request.diagnostico() == null || request.diagnostico().isBlank()
                || request.tipo() == null || request.tipo().isBlank()) {
            throw new IllegalArgumentException("Informe as datas, o diagnóstico e o tipo da receita.");
        }
        if (request.dataValidade().isBefore(request.dataEmissao())) {
            throw new IllegalArgumentException("A validade não pode ser anterior à data de emissão.");
        }
        if (request.pacienteId() == null) {
            throw new IllegalArgumentException("Selecione um paciente para a receita.");
        }
    }

    private void aplicarDados(Receita destino, ReceitaRequest origem) {
        destino.setDataEmissao(origem.dataEmissao());
        destino.setDataValidade(origem.dataValidade());
        destino.setDiagnostico(origem.diagnostico().trim());
        destino.setObservacoes(origem.observacoes());
        destino.setTipo(origem.tipo().trim());
    }

    private Paciente buscarPacienteDaEmpresa(Long pacienteId, Long empresaId, UsuarioLogado identidade) {
        Paciente paciente;

        if (isFuncionario(identidade)) {
            paciente = pacienteRepository.findByIdAndEmpresa_IdAndUsuario_Id(
                    pacienteId, empresaId, identidade.usuarioId()).orElse(null);
        } else {
            paciente = pacienteRepository.findByIdAndEmpresa_Id(pacienteId, empresaId).orElse(null);
        }

        if (paciente == null || paciente.getStatus() != EnumStatusPaciente.ATIVO) {
            throw new AccessDeniedException("O paciente não pertence à empresa ou está inativo.");
        }
        return paciente;
    }

    private Receita buscarNoEscopo(Long id, UsuarioLogado identidade) {
        if (isAdministradorGlobal(identidade)) {
            return receitaRepository.findById(id).orElse(null);
        }
        if (isEmpresa(identidade)) {
            return receitaRepository.findByIdAndEmpresa_Id(id, identidade.empresaId()).orElse(null);
        }
        if (isFuncionario(identidade)) {
            return receitaRepository.findByIdAndResponsavel_IdAndEmpresa_Id(
                    id, identidade.usuarioId(), identidade.empresaId()).orElse(null);
        }
        return null;
    }

    private boolean isAdministradorGlobal(UsuarioLogado identidade) {
        return identidade.authorities() != null && identidade.authorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
    }

    private boolean isEmpresa(UsuarioLogado identidade) {
        return identidade.usuarioId() == null
                && identidade.empresaId() != null
                && identidade.authorities() != null
                && identidade.authorities().stream()
                .anyMatch(authority -> "ROLE_USER".equals(authority.getAuthority()));
    }

    private boolean isFuncionario(UsuarioLogado identidade) {
        return identidade.usuarioId() != null
                && identidade.empresaId() != null
                && identidade.authorities() != null
                && identidade.authorities().stream()
                .anyMatch(authority -> "ROLE_USER".equals(authority.getAuthority()));
    }
}
