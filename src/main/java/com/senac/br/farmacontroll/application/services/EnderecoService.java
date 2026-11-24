package com.senac.br.farmacontroll.application.services;

import com.senac.br.farmacontroll.application.DTOs.EnderecoResponse;
import com.senac.br.farmacontroll.domain.interfaces.IViaCepExternalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnderecoService {

    @Autowired
    private IViaCepExternalRepository viaCepExternalRepository;

    // Encaminha o CEP ao adaptador ViaCEP e transforma a resposta externa no DTO público da API.
    public  EnderecoResponse BuscarEndereco(String cep){
        var enderecoCompleto = viaCepExternalRepository.buscarEnderecoViaCepPorCep(cep);

        return new EnderecoResponse(enderecoCompleto.getCep(),
                enderecoCompleto.getLogradouro(),
                enderecoCompleto.getBairro(),
                enderecoCompleto.getLocalidade(),
                enderecoCompleto.getUf());
    }
}
