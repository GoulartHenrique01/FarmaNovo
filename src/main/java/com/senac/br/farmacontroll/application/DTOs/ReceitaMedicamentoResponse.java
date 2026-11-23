package com.senac.br.farmacontroll.application.DTOs;

import com.senac.br.farmacontroll.domain.entidade.ReceitaMedicamento;

public record ReceitaMedicamentoResponse(
        Long id,
        Long medicamentoId,
        String medicamentoNome,
        String dosagemPrescrita,
        String posologia,
        Integer quantidadePrescrita,
        String duracaoTratamento
) {
    public ReceitaMedicamentoResponse(ReceitaMedicamento item) {
        this(
                item.getId(),
                item.getMedicamento().getId(),
                item.getMedicamento().getNome(),
                item.getDosagemPrescrita(),
                item.getPosologia(),
                item.getQuantidadePrescrita(),
                item.getDuracaoTratamento()
        );
    }
}
