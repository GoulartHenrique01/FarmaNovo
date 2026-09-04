package com.senac.br.farmacontroll.DTOs;

import com.senac.br.farmacontroll.entidade.EnumStatusUsuario;

public record AtualizarStatusRequest(EnumStatusUsuario status) {
}
