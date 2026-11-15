package com.senac.br.farmacontroll.application.DTOs;

public record EmpresaRequest(
        String razaoSocial,
        String nomeFantasia,
        String cnpj,
        String administradorNome,
        String administradorEmail,
        String administradorSenha,
        String administradorCpf
) {
}
