package com.senac.br.farmacontroll.application.DTOs;

public record IdentidadeResponse(
        Long id,
        Long usuarioId,
        Long empresaId,
        String nome,
        String email,
        String cpf,
        String role,
        String status
) {
    public IdentidadeResponse(UsuarioLogado usuarioLogado) {
        this(
                usuarioLogado.id(),
                usuarioLogado.usuarioId(),
                usuarioLogado.empresaId(),
                usuarioLogado.nome(),
                usuarioLogado.email(),
                usuarioLogado.cpf() == null ? null : usuarioLogado.cpf().toString(),
                usuarioLogado.authorities().stream()
                        .map(authority -> authority.getAuthority())
                        .findFirst()
                        .orElse("ROLE_USER"),
                usuarioLogado.status()
        );
    }
}
