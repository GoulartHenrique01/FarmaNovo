package com.senac.br.farmacontroll.application.DTOs;

import com.senac.br.farmacontroll.domain.valueObject.CPF;

public record UsuarioRequest(String nome, String email, String senha, String cpf) {
}
