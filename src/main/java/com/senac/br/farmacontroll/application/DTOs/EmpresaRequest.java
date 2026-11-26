package com.senac.br.farmacontroll.application.DTOs;

public record EmpresaRequest(
        String razaoSocial,
        String nomeFantasia,
        String cnpj,
        String administradorNome,
        String administradorEmail,
        String administradorSenha,
        String administradorCpf,
        String cep,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String uf
) {
}
