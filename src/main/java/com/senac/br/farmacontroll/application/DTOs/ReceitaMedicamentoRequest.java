package com.senac.br.farmacontroll.application.DTOs;

public record ReceitaMedicamentoRequest(
        Long medicamentoId,
        String dosagemPrescrita,
        String posologia,
        Integer quantidadePrescrita,
        String duracaoTratamento
) {
}
