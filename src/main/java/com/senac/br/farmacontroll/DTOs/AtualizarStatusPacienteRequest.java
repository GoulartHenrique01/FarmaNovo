package com.senac.br.farmacontroll.DTOs;

import com.senac.br.farmacontroll.entidade.EnumStatusPaciente;

public record AtualizarStatusPacienteRequest(EnumStatusPaciente status) {
}