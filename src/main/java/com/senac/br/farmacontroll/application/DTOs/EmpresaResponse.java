package com.senac.br.farmacontroll.application.DTOs;

import com.senac.br.farmacontroll.domain.entidade.Empresa;

public record EmpresaResponse(Long id, String razaoSocial, String nomeFantasia, String cnpj, EnumStatusEmpresa status) {

    public EmpresaResponse(Empresa empresaEntidade){
        this(
                empresaEntidade.getId(),
                empresaEntidade.getRazaoSocial(),
                empresaEntidade.getNomeFantasia(),
                empresaEntidade.getCnpj(),
                empresaEntidade.getStatus()
        );
    }
}
