package com.senac.br.farmacontroll.application.DTOs;

import com.senac.br.farmacontroll.domain.entidade.Receita;

import java.time.LocalDate;

public record ReceitaResponse(
        Long id,
        LocalDate dataEmissao,
        LocalDate dataValidade,
        String diagnostico,
        String observacoes,
        String tipo,
        EnumStatusReceita status,
        Long pacienteId,
        String pacienteNome
) {
    public ReceitaResponse(Receita receita) {
        this(
                receita.getId(),
                receita.getDataEmissao(),
                receita.getDataValidade(),
                receita.getDiagnostico(),
                receita.getObservacoes(),
                receita.getTipo(),
                receita.getStatus(),
                receita.getPaciente() == null ? null : receita.getPaciente().getId(),
                receita.getPaciente() == null ? null : receita.getPaciente().getNome()
        );
    }
}
