package com.senac.br.farmacontroll.application.services;

import com.senac.br.farmacontroll.application.DTOs.CriarMedicamentoRequest;
import com.senac.br.farmacontroll.application.DTOs.CriarMedicamentoResponse;
import com.senac.br.farmacontroll.application.DTOs.MedicamentoResponse;
import com.senac.br.farmacontroll.domain.entidade.Medicamento;
import com.senac.br.farmacontroll.domain.repository.MedicamentoRepository;
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

        return new CriarMedicamentoResponse(medicamentoTipoSalvar.getId(),"Medicamento Salvo com sucesso!");
    }
}

