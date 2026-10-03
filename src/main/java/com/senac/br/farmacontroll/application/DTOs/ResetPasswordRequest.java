package com.senac.br.farmacontroll.application.DTOs;

public record ResetPasswordRequest(String token, String novaSenha) {
}
