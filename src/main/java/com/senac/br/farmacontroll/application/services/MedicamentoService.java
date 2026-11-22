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
import java.util.List;

@Service
public class MedicamentoService {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private UsuarioService usuarioService;

    public List<MedicamentoResponse> listarTodosMedicamentosTable(){
        UsuarioLogado usuarioLogado = usuarioService.getUsuarioLogado();
        List<Medicamento> medicamentos;

        if (isAdministradorGlobal(usuarioLogado)) {
            medicamentos = medicamentoRepository.findAll();
        } else {
            medicamentos = medicamentoRepository.findByEmpresa_Id(requireEmpresaId(usuarioLogado));
        }

        return medicamentos
                .stream()
                .filter(medicamento -> medicamento.getStatus() != EnumStatusMedicamento.EXCLUIDO)
                .map(MedicamentoResponse::new)
                .toList();
    }

    public CriarMedicamentoResponse criarTipo(CriarMedicamentoRequest criarMedicamentoRequest){
        UsuarioLogado identidade = usuarioService.getUsuarioLogado();
        exigirGerenciamentoDeMedicamentos(identidade);
        Medicamento medicamentoTipoSalvar = new Medicamento(criarMedicamentoRequest);
        associarEmpresa(medicamentoTipoSalvar, identidade);
        medicamentoRepository.save(medicamentoTipoSalvar);

        return new CriarMedicamentoResponse(
                medicamentoTipoSalvar.getId(),
                medicamentoTipoSalvar.getTipo());
    }

    public MedicamentoResponse salvarMedicamento(MedicamentoRequest medicamento, Long id){
        UsuarioLogado usuarioLogado = usuarioService.getUsuarioLogado();
        if(id == 0L){
            exigirGerenciamentoDeMedicamentos(usuarioLogado);
            Medicamento medicamentoBanco = new Medicamento();
            associarEmpresa(medicamentoBanco, usuarioLogado);
            medicamentoBanco.setNome(medicamento.nome());
            medicamentoBanco.setTipo(medicamento.tipo());
            medicamentoBanco.setDataValidade(new DataValidade(medicamento.dataValidade()));
            medicamentoBanco.setDosagem(medicamento.dosagem());
            medicamentoBanco.setUnidadeDosagem(medicamento.unidadeDosagem());
            medicamentoBanco.setQuantidade(medicamento.quantidade());
            medicamentoBanco.setMarca(medicamento.marca());
            medicamentoBanco.setPrecisaReceita(medicamento.precisaReceita());
            medicamentoBanco.setStatus(EnumStatusMedicamento.ATIVO);
            medicamentoRepository.save(medicamentoBanco);

            return new MedicamentoResponse(medicamentoBanco);
        }else{
            if (!isAdministradorGlobal(usuarioLogado) && !isEmpresa(usuarioLogado)) {
                throw new AccessDeniedException("Somente a empresa pode alterar medicamentos.");
            }
            Medicamento medicamentoBanco = buscarMedicamentoNoEscopo(id, usuarioLogado);
            if (medicamentoBanco != null){
                medicamentoBanco.setNome(medicamento.nome());
                medicamentoBanco.setTipo(medicamento.tipo());
                medicamentoBanco.setDataValidade(new DataValidade(medicamento.dataValidade()));
                medicamentoBanco.setDosagem(medicamento.dosagem());
                medicamentoBanco.setUnidadeDosagem(medicamento.unidadeDosagem());
                medicamentoBanco.setQuantidade(medicamento.quantidade());
                medicamentoBanco.setMarca(medicamento.marca());
                medicamentoBanco.setPrecisaReceita(medicamento.precisaReceita());
                medicamentoRepository.save(medicamentoBanco);

                return new MedicamentoResponse(medicamentoBanco);
            }
        }
        return null;
    }

    public MedicamentoResponse buscarPorId(Long id){
        var medicamento = buscarMedicamentoNoEscopo(id, usuarioService.getUsuarioLogado());
        if (medicamento == null){
            return null;
        }
        return new MedicamentoResponse(medicamento);
    }

    public boolean atualizarStatus(EnumStatusMedicamento status, Long id){
        UsuarioLogado identidade = usuarioService.getUsuarioLogado();
        if (!isAdministradorGlobal(identidade) && !isEmpresa(identidade)) {
            throw new AccessDeniedException("Somente a empresa pode alterar medicamentos.");
        }
        Medicamento medicamentoBanco = buscarMedicamentoNoEscopo(id, identidade);
        if (medicamentoBanco!= null){
            medicamentoBanco.setStatus(status);
            medicamentoRepository.save(medicamentoBanco);
            return true;
        }
        return false;
    }

    public boolean excluir(Long id) {
        return atualizarStatus(EnumStatusMedicamento.EXCLUIDO, id);
    }

    private Medicamento buscarMedicamentoNoEscopo(Long id, UsuarioLogado usuarioLogado) {
        if (isAdministradorGlobal(usuarioLogado)) {
            return medicamentoRepository.findById(id).orElse(null);
        }
        return medicamentoRepository.findByIdAndEmpresa_Id(id, requireEmpresaId(usuarioLogado)).orElse(null);
    }

    private void associarEmpresa(Medicamento medicamento, UsuarioLogado usuarioLogado) {
        Long empresaId = requireEmpresaId(usuarioLogado);
        Empresa empresa = empresaRepository.findById(empresaId).orElse(null);
        if (empresa == null) {
            throw new AccessDeniedException("Empresa do usuário autenticado não encontrada.");
        }
        medicamento.setEmpresa(empresa);
    }

    private Long requireEmpresaId(UsuarioLogado usuarioLogado) {
        if (usuarioLogado.empresaId() == null) {
            throw new AccessDeniedException("Usuário sem empresa vinculada.");
        }
        return usuarioLogado.empresaId();
    }

    private boolean isAdministradorGlobal(UsuarioLogado usuarioLogado) {
        return usuarioLogado.authorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
    }

    private boolean isEmpresa(UsuarioLogado usuarioLogado) {
        return usuarioLogado.usuarioId() == null && usuarioLogado.empresaId() != null;
    }

    private void exigirGerenciamentoDeMedicamentos(UsuarioLogado usuarioLogado) {
        if (!isEmpresa(usuarioLogado)) {
            throw new AccessDeniedException("Somente uma empresa autenticada pode cadastrar medicamentos.");
        }
    }
}
