package com.senac.br.farmacontroll.application.services;

import com.senac.br.farmacontroll.application.DTOs.*;
import com.senac.br.farmacontroll.domain.entidade.Medicamento;
import com.senac.br.farmacontroll.domain.repository.MedicamentoRepository;
import com.senac.br.farmacontroll.domain.valueObject.DataValidade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MedicamentoService {

    @Autowired
    private MedicamentoRepository medicamentoRepository;


    public List<MedicamentoResponse> listarTodosMedicamentosTable(){

        return (medicamentoRepository.findAll()
                .stream()
                .map(MedicamentoResponse::new)
                .toList());
    }

    public CriarMedicamentoResponse criarTipo(CriarMedicamentoRequest criarMedicamentoRequest){
        Medicamento medicamentoTipoSalvar = new Medicamento(criarMedicamentoRequest);
        medicamentoRepository.save(medicamentoTipoSalvar);

        return new CriarMedicamentoResponse(
                medicamentoTipoSalvar.getId(),
                medicamentoTipoSalvar.getTipo());
    }

    public MedicamentoResponse salvarMedicamento(MedicamentoRequest medicamento, Long id){
        if(id == 0L){
            Medicamento medicamentoBanco = new Medicamento();
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
            Medicamento medicamentoBanco = medicamentoRepository.findById(id).orElse(null);
            if (medicamento!= null){
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
        var medicamento = medicamentoRepository.findById(id).orElse(null);
        if (medicamento == null){
            return null;
        }
        return new MedicamentoResponse(medicamento);
    }

    public boolean atualizarStatus(EnumStatusMedicamento status, Long id){
        Medicamento medicamentoBanco = medicamentoRepository.findById(id).orElse(null);
        if (medicamentoBanco!= null){
            medicamentoBanco.setStatus(status);
            medicamentoRepository.save(medicamentoBanco);
            return true;
        }
        return false;
    }
}
