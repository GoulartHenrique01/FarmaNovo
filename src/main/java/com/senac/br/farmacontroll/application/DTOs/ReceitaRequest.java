package com.senac.br.farmacontroll.application.DTOs;

import java.time.LocalDate;

public record ReceitaRequest(LocalDate dataEmissao,
                             LocalDate dataValidade, String diagnostico,
                             String observacoes, String tipo) {
}
