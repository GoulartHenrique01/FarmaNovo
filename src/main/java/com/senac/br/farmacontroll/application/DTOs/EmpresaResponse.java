package com.senac.br.farmacontroll.application.DTOs;

import com.senac.br.farmacontroll.domain.entidade.Empresa;

public record EmpresaResponse(
        Long id, String razaoSocial, String nomeFantasia, String cnpj,
        EnumStatusEmpresa status, String nomeResponsavel, String cpfResponsavel, String email,
        String cep, String logradouro, String numero, String complemento,
        String bairro, String cidade, String uf
) {
    public EmpresaResponse(Empresa empresa) {
        this(empresa.getId(), empresa.getRazaoSocial(), empresa.getNomeFantasia(),
                empresa.getCnpj(), empresa.getStatus(), empresa.getNomeResponsavel(),
                empresa.getCpfResponsavel() == null ? null : empresa.getCpfResponsavel().toString(),
                empresa.getEmail(), empresa.getCep(), empresa.getLogradouro(), empresa.getNumero(),
                empresa.getComplemento(), empresa.getBairro(), empresa.getCidade(), empresa.getUf());
    }
}
