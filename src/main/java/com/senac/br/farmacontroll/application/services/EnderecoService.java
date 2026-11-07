package com.senac.br.farmacontroll.application.services;

import com.senac.br.farmacontroll.application.DTOs.EnderecoResponse;
import com.senac.br.farmacontroll.domain.interfaces.IViaCepExternalRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class EnderecoService {

    @Autowired
    private IViaCepExternalRepository viaCepExternalRepository;

    public  EnderecoResponse BuscarEndereco(String cep){
        var enderecoCompleto = viaCepExternalRepository.buscarEnderecoViaCepPorCep(cep);

        return new EnderecoResponse(enderecoCompleto.getCep(),
                enderecoCompleto.getLogradouro(),
                enderecoCompleto.getBairro(),
                enderecoCompleto.getLocalidade(),
                enderecoCompleto.getUf());
    }
}
