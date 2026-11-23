package com.senac.br.farmacontroll.application.services;

import com.senac.br.farmacontroll.application.DTOs.*;
import com.senac.br.farmacontroll.domain.entidade.Empresa;
import com.senac.br.farmacontroll.domain.entidade.Medicamento;
import com.senac.br.farmacontroll.domain.entidade.Paciente;
import com.senac.br.farmacontroll.domain.entidade.Receita;
import com.senac.br.farmacontroll.domain.entidade.ReceitaMedicamento;
import com.senac.br.farmacontroll.domain.repository.EmpresaRepository;
import com.senac.br.farmacontroll.domain.repository.MedicamentoRepository;
import com.senac.br.farmacontroll.domain.repository.PacienteRepository;
import com.senac.br.farmacontroll.domain.repository.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ReceitaService {

    @Autowired
    private ReceitaRepository receitaRepository;
    @Autowired
    private EmpresaRepository empresaRepository;
    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private MedicamentoRepository medicamentoRepository;
    @Autowired
    private UsuarioService usuarioService;

    // FLUXO: a empresa autenticada consulta somente as receitas vinculadas ao seu próprio cadastro.
    @Transactional(readOnly = true)
    public List<ReceitaResponse> listarTodosReceitasTable() {
        UsuarioLogado identidade = usuarioService.getUsuarioLogado();
        exigirEmpresa(identidade);
        return receitaRepository.findByEmpresa_Id(identidade.empresaId()).stream()
                .filter(receita -> receita.getStatus() != EnumStatusReceita.EXCLUIDO)
                .map(ReceitaResponse::new)
                .toList();
    }

    // FLUXO: valida a empresa, o paciente e cada medicamento; cria ou atualiza a receita e seus itens em uma única transação.
    @Transactional
    public ReceitaResponse salvarReceita(ReceitaRequest request, Long id) {
        UsuarioLogado identidade = usuarioService.getUsuarioLogado();
        Empresa empresa = buscarEmpresaAtiva(identidade);
        validarReceita(request);

        Paciente paciente = buscarPaciente(request.pacienteId(), empresa.getId());
        Receita receita;

        if (id == null || id == 0L) {
            receita = new Receita();
            receita.setEmpresa(empresa);
            receita.setStatus(EnumStatusReceita.ATIVO);
        } else {
            receita = receitaRepository.findByIdAndEmpresa_Id(id, empresa.getId()).orElse(null);
            if (receita == null || receita.getStatus() == EnumStatusReceita.EXCLUIDO) {
                return null;
            }
        }

        receita.setDataEmissao(request.dataEmissao());
        receita.setDataValidade(request.dataValidade());
        receita.setDiagnostico(request.diagnostico().trim());
        receita.setObservacoes(request.observacoes());
        receita.setPaciente(paciente);
        receita.setUsuario(null);
        receita.getMedicamentos().clear();

        Set<Long> medicamentosSelecionados = new HashSet<>();
        for (ReceitaMedicamentoRequest itemRequest : request.medicamentos()) {
            Medicamento medicamento = buscarMedicamento(itemRequest.medicamentoId(), empresa.getId());
            if (!medicamentosSelecionados.add(medicamento.getId())) {
                throw new IllegalArgumentException("Não selecione o mesmo medicamento mais de uma vez.");
            }

            ReceitaMedicamento item = new ReceitaMedicamento();
            item.setReceita(receita);
            item.setMedicamento(medicamento);
            item.setDosagemPrescrita(itemRequest.dosagemPrescrita().trim());
            item.setPosologia(itemRequest.posologia().trim());
            item.setQuantidadePrescrita(itemRequest.quantidadePrescrita());
            item.setDuracaoTratamento(itemRequest.duracaoTratamento().trim());
            receita.getMedicamentos().add(item);
        }

        return new ReceitaResponse(receitaRepository.save(receita));
    }

    @Transactional(readOnly = true)
    public ReceitaResponse buscarPorId(Long id) {
        UsuarioLogado identidade = usuarioService.getUsuarioLogado();
        exigirEmpresa(identidade);
        Receita receita = receitaRepository.findByIdAndEmpresa_Id(id, identidade.empresaId()).orElse(null);
        if (receita == null || receita.getStatus() == EnumStatusReceita.EXCLUIDO) {
            return null;
        }
        return new ReceitaResponse(receita);
    }

    @Transactional
    public boolean atualizarStatus(EnumStatusReceita status, Long id) {
        if (status == null) {
            throw new IllegalArgumentException("Informe o status da receita.");
        }
        UsuarioLogado identidade = usuarioService.getUsuarioLogado();
        exigirEmpresa(identidade);
        Receita receita = receitaRepository.findByIdAndEmpresa_Id(id, identidade.empresaId()).orElse(null);
        if (receita == null || receita.getStatus() == EnumStatusReceita.EXCLUIDO) {
            return false;
        }
        receita.setStatus(status);
        receitaRepository.save(receita);
        return true;
    }

    private Empresa buscarEmpresaAtiva(UsuarioLogado identidade) {
        exigirEmpresa(identidade);
        Empresa empresa = empresaRepository.findById(identidade.empresaId()).orElse(null);
        if (empresa == null || empresa.getStatus() != EnumStatusEmpresa.ATIVO) {
            throw new AccessDeniedException("Empresa não encontrada ou inativa.");
        }
        return empresa;
    }

    // Impede associar à receita pacientes que pertencem a outra empresa ou estejam inativos.
    private Paciente buscarPaciente(Long pacienteId, Long empresaId) {
        Paciente paciente = pacienteRepository.findByIdAndEmpresa_Id(pacienteId, empresaId).orElse(null);
        if (paciente == null || paciente.getStatus() != EnumStatusPaciente.ATIVO) {
            throw new IllegalArgumentException("Selecione um paciente ativo cadastrado nesta empresa.");
        }
        return paciente;
    }

    // Reutiliza o medicamento do cadastro da empresa; não cria um novo medicamento durante a receita.
    private Medicamento buscarMedicamento(Long medicamentoId, Long empresaId) {
        Medicamento medicamento = medicamentoRepository.findByIdAndEmpresa_Id(medicamentoId, empresaId).orElse(null);
        if (medicamento == null || medicamento.getStatus() != EnumStatusMedicamento.ATIVO) {
            throw new IllegalArgumentException("Selecione um medicamento ativo cadastrado nesta empresa.");
        }
        return medicamento;
    }

    private void validarReceita(ReceitaRequest request) {
        if (request == null || request.dataEmissao() == null || request.dataValidade() == null
                || request.diagnostico() == null || request.diagnostico().isBlank()) {
            throw new IllegalArgumentException("Informe as datas e o diagnóstico da receita.");
        }
        if (request.dataValidade().isBefore(request.dataEmissao())) {
            throw new IllegalArgumentException("A validade não pode ser anterior à data de emissão.");
        }
        if (request.pacienteId() == null) {
            throw new IllegalArgumentException("Selecione um paciente para a receita.");
        }
        if (request.medicamentos() == null || request.medicamentos().isEmpty()) {
            throw new IllegalArgumentException("Adicione pelo menos um medicamento à receita.");
        }
        for (ReceitaMedicamentoRequest item : request.medicamentos()) {
            if (item == null || item.medicamentoId() == null
                    || item.dosagemPrescrita() == null || item.dosagemPrescrita().isBlank()
                    || item.posologia() == null || item.posologia().isBlank()
                    || item.quantidadePrescrita() == null || item.quantidadePrescrita() <= 0
                    || item.duracaoTratamento() == null || item.duracaoTratamento().isBlank()) {
                throw new IllegalArgumentException("Preencha medicamento, dosagem, posologia, quantidade e duração de cada item.");
            }
        }
    }

    private void exigirEmpresa(UsuarioLogado identidade) {
        boolean permitido = identidade.empresaId() != null
                && identidade.authorities() != null
                && identidade.authorities().stream()
                .anyMatch(authority -> "ROLE_USER".equals(authority.getAuthority()));
        if (!permitido) {
            throw new AccessDeniedException("Somente uma empresa autenticada pode gerenciar receitas.");
        }
    }
}
