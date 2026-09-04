package com.senac.br.farmacontroll.DTOs;

public record ResetPasswordRequest(String token, String novaSenha) {
}
