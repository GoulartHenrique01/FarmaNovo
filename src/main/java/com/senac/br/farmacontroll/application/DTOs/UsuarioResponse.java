package com.senac.br.farmacontroll.application.DTOs;

import com.senac.br.farmacontroll.domain.entidade.EnumStatusUsuario;
import com.senac.br.farmacontroll.domain.entidade.Usuario;

public record UsuarioResponse(Long id, String nome, String cpf, String email, EnumStatusUsuario status) {

    public UsuarioResponse(Usuario usuarioEntidade){
        this(
                usuarioEntidade.getId(),
                usuarioEntidade.getNome(),
                usuarioEntidade.getCpf(),
                usuarioEntidade.getEmail(),
                usuarioEntidade.getStatus()
        );
    }
}
