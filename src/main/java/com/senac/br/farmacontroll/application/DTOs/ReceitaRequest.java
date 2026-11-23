package com.senac.br.farmacontroll.application.DTOs;

import java.time.LocalDate;
import java.util.List;

public record ReceitaRequest(
        LocalDate dataEmissao,
        LocalDate dataValidade,
        String diagnostico,
        String observacoes,
        Long pacienteId,
        List<ReceitaMedicamentoRequest> medicamentos
) {
}
