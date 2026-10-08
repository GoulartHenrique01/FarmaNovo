package com.senac.br.farmacontroll.application.services;

import com.senac.br.farmacontroll.application.DTOs.ReceitaResponse;
import com.senac.br.farmacontroll.domain.repository.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceitaService {

    @Autowired
    private ReceitaRepository receitaRepository;

    public List<ReceitaResponse> listarTodosReceitasTable(){

        return (receitaRepository.findAll()
                .stream()
                .map(ReceitaResponse::new)
                .toList());
    }
}

