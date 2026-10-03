package com.senac.br.farmacontroll.application.DTOs;

import com.senac.br.farmacontroll.domain.entidade.EnumStatusPaciente;

public record AtualizarStatusPacienteRequest(EnumStatusPaciente status) {
}