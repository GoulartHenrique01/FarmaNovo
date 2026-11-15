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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    @Transactional(readOnly = true)
    public List<ReceitaResponse> listarTodosReceitasTable() {
        UsuarioLogado identidade = getUsuarioLogado();
        List<Receita> receitas;

        if (isAdministradorGlobal(identidade)) {
            receitas = receitaRepository.findAll();
        } else if (isEmpresa(identidade)) {
            receitas = receitaRepository.findByEmpresa_Id(identidade.empresaId());
        } else if (identidade.usuarioId() != null
                && identidade.empresaId() != null) {
            receitas = receitaRepository.findByResponsavel_IdAndEmpresa_Id(
                    identidade.usuarioId(), identidade.empresaId());
        } else {
            throw new AccessDeniedException("Usuário sem permissão para consultar receitas.");
        }

        return receitas.stream()
                .filter(receita -> receita.getStatus() != EnumStatusReceita.EXCLUIDO)
                .map(receita -> new ReceitaResponse(receita, identidade.usuarioId() != null
                        && identidade.empresaId() != null
                        && !isAdministradorGlobal(identidade)))
                .toList();
    }

    @Transactional
    public ReceitaResponse salvarReceita(ReceitaRequest receita, Long id) {
        UsuarioLogado identidade = getUsuarioLogado();

        if (id == 0L) {
            if (!isFuncionario(identidade)) {
                throw new AccessDeniedException("Somente o usuário autenticado pode cadastrar receitas.");
            }
            Empresa empresa = empresaRepository.findById(identidade.empresaId())
                    .orElseThrow(() -> new AccessDeniedException("Empresa do usuário autenticado não encontrada."));
            Usuario responsavel = usuarioRepository.findById(identidade.usuarioId())
                    .filter(usuario -> usuario.getEmpresa() != null
                            && usuario.getEmpresa().getId().equals(identidade.empresaId())
                            && "ROLE_USER".equals(usuario.getRole())
                            && usuario.getStatus() == EnumStatusUsuario.ATIVO)
                    .orElseThrow(() -> new AccessDeniedException("Usuário autenticado inválido."));
            Paciente paciente = buscarPacienteDoUsuario(receita.pacienteId(), identidade);

            Receita receitaBanco = new Receita();
            aplicarDados(receitaBanco, receita);
            receitaBanco.setStatus(EnumStatusReceita.ATIVO);
            receitaBanco.setEmpresa(empresa);
            receitaBanco.setResponsavel(responsavel);
            receitaBanco.setPaciente(paciente);
            receitaRepository.save(receitaBanco);
            return new ReceitaResponse(receitaBanco);
        }

        Optional<Receita> receitaAutorizada = buscarNoEscopo(id, identidade);
        if (receitaAutorizada.isEmpty()) {
            return null;
        }
        Receita receitaBanco = receitaAutorizada.get();
        if (receita.pacienteId() != null) {
            if (!isFuncionario(identidade)) {
                throw new AccessDeniedException("Somente o usuário responsável pode alterar o paciente da receita.");
            }
            receitaBanco.setPaciente(buscarPacienteDoUsuario(receita.pacienteId(), identidade));
        }
        aplicarDados(receitaBanco, receita);
        receitaRepository.save(receitaBanco);
        return new ReceitaResponse(receitaBanco);
    }

    @Transactional(readOnly = true)
    public ReceitaResponse buscarPorId(Long id) {
        UsuarioLogado identidade = getUsuarioLogado();
        return buscarNoEscopo(id, identidade)
                .map(receita -> new ReceitaResponse(receita, isFuncionario(identidade)))
                .orElse(null);
    }

    @Transactional
    public boolean atualizarStatus(EnumStatusReceita status, Long id) {
        Optional<Receita> receitaAutorizada = buscarNoEscopo(id, getUsuarioLogado());
        if (receitaAutorizada.isEmpty()) {
            return false;
        }
        Receita receitaBanco = receitaAutorizada.get();
        receitaBanco.setStatus(status);
        receitaRepository.save(receitaBanco);
        return true;
    }

    private void aplicarDados(Receita destino, ReceitaRequest origem) {
        destino.setDataEmissao(origem.dataEmissao());
        destino.setDataValidade(origem.dataValidade());
        destino.setDiagnostico(origem.diagnostico());
        destino.setObservacoes(origem.observacoes());
        destino.setTipo(origem.tipo());
    }

    private Optional<Receita> buscarNoEscopo(Long id, UsuarioLogado identidade) {
        if (isAdministradorGlobal(identidade)) {
            return receitaRepository.findById(id);
        }
        if (isEmpresa(identidade)) {
            return receitaRepository.findByIdAndEmpresa_Id(id, identidade.empresaId());
        }
        if (identidade.usuarioId() != null && identidade.empresaId() != null) {
            return receitaRepository.findByIdAndResponsavel_IdAndEmpresa_Id(
                    id, identidade.usuarioId(), identidade.empresaId());
        }
        return Optional.empty();
    }

    private Paciente buscarPacienteDoUsuario(Long pacienteId, UsuarioLogado identidade) {
        if (pacienteId == null) {
            throw new IllegalArgumentException("Selecione um paciente para a receita.");
        }
        return pacienteRepository.findByIdAndEmpresa_IdAndUsuario_Id(
                        pacienteId, identidade.empresaId(), identidade.usuarioId())
                .filter(paciente -> paciente.getStatus() == EnumStatusPaciente.ATIVO)
                .orElseThrow(() -> new AccessDeniedException(
                        "O paciente não pertence ao usuário autenticado ou está inativo."));
    }

    private UsuarioLogado getUsuarioLogado() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UsuarioLogado usuarioLogado) {
            return usuarioLogado;
        }
        throw new AccessDeniedException("Usuário não autenticado.");
    }

    private boolean isAdministradorGlobal(UsuarioLogado identidade) {
        return identidade.authorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
    }

    private boolean isEmpresa(UsuarioLogado identidade) {
        return identidade.usuarioId() == null && identidade.empresaId() != null;
    }

    private boolean isFuncionario(UsuarioLogado identidade) {
        return identidade.usuarioId() != null
                && identidade.empresaId() != null
                && identidade.authorities().stream()
                .anyMatch(authority -> "ROLE_USER".equals(authority.getAuthority()));
    }
}
