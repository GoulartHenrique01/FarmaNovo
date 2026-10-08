package com.senac.br.farmacontroll.application.services;

import com.senac.br.farmacontroll.application.DTOs.PacienteResponse;
import com.senac.br.farmacontroll.domain.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;


    public List<PacienteResponse> listarTodosPacientesTable(){

        return (pacienteRepository.findAll()
                .stream()
                .map(PacienteResponse::new)
                .toList());
    }
}
