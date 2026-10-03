package com.senac.br.farmacontroll.application.DTOs;

import com.senac.br.farmacontroll.domain.entidade.EnumStatusUsuario;

public record AtualizarStatusRequest(EnumStatusUsuario status) {
}
