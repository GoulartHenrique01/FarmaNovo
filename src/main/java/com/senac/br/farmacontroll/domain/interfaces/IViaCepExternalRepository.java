package com.senac.br.farmacontroll.domain.interfaces;

import com.senac.br.farmacontroll.infra.dto.ViaCepResponse;

//Define saida/nome/entrada
public interface IViaCepExternalRepository{
    ViaCepResponse buscarEnderecoViaCepPorCep(String cep);
}
