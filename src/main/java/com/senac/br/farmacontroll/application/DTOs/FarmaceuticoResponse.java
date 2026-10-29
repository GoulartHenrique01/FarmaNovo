package com.senac.br.farmacontroll.application.DTOs;

import com.senac.br.farmacontroll.domain.entidade.Farmaceutico;


public record FarmaceuticoResponse(Long id, String nome, String cpf, String telefone,
                                   String email, String senha, EnumStatusFarmaceutico status) {
    public FarmaceuticoResponse(Farmaceutico farmaceuticoEntidade){
        this(
            farmaceuticoEntidade.getId(),
            farmaceuticoEntidade.getNome(),
            farmaceuticoEntidade.getCpf().toString(),
            farmaceuticoEntidade.getTelefone(),
            farmaceuticoEntidade.getEmail(),
            farmaceuticoEntidade.getSenha(),
            farmaceuticoEntidade.getStatus()
        );
    }
}
