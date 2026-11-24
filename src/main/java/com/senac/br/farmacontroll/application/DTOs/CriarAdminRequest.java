package com.senac.br.farmacontroll.application.DTOs;

// Mantém secretKey no DTO por compatibilidade com o JSON enviado pelo Desktop antigo.
// O backend não valida nem exige esse campo.
public record CriarAdminRequest(String nome, String email, String senha, String cpf, String secretKey) {
}
