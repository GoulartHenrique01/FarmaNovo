package com.senac.br.farmacontroll.application.services;

import com.senac.br.farmacontroll.application.DTOs.FarmaceuticoResponse;
import com.senac.br.farmacontroll.domain.repository.FarmaceuticoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FarmaceuticoService {

    @Autowired
    private FarmaceuticoRepository farmaceuticoRepository;


    public List<FarmaceuticoResponse> listarTodosFarmaceuticoTable(){

        return (farmaceuticoRepository.findAll()
                .stream()
                .map(FarmaceuticoResponse::new)
                .toList());
    }
}
