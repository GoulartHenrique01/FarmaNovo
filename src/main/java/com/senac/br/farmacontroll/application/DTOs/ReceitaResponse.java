package com.senac.br.farmacontroll.application.DTOs;

import com.senac.br.farmacontroll.domain.entidade.Receita;
import java.time.LocalDate;
import java.util.List;

public record ReceitaResponse(
        Long id,
        LocalDate dataEmissao,
        LocalDate dataValidade,
        String diagnostico,
        String observacoes,
        EnumStatusReceita status,
        Long pacienteId,
        String pacienteNome,
        List<ReceitaMedicamentoResponse> medicamentos
) {
    public ReceitaResponse(Receita receita) {
        this(
                receita.getId(),
                receita.getDataEmissao(),
                receita.getDataValidade(),
                receita.getDiagnostico(),
                receita.getObservacoes(),
                receita.getStatus(),
                receita.getPaciente() == null ? null : receita.getPaciente().getId(),
                receita.getPaciente() == null ? null : receita.getPaciente().getNome(),
                receita.getMedicamentos().stream().map(ReceitaMedicamentoResponse::new).toList()
        );
    }
}
