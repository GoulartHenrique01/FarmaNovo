package com.senac.br.farmacontroll.application.DTOs;

import com.senac.br.farmacontroll.domain.entidade.EnumStatusMedicamento;
import com.senac.br.farmacontroll.domain.entidade.Medicamento;

import java.time.LocalDate;

public record MedicamentoResponse(Long id, String nome, String tipo, LocalDate dataValidade,
                                  double dosagem, String unidadeDosagem, int quantidade,
                                  String marca, String precisaReceita, EnumStatusMedicamento status) {
    public MedicamentoResponse(Medicamento medicamentoEntidade){
        this(
                medicamentoEntidade.getId(),
                medicamentoEntidade.getNome(),
                medicamentoEntidade.getTipo(),
                medicamentoEntidade.getDataValidade(),
                medicamentoEntidade.getDosagem(),
                medicamentoEntidade.getUnidadeDosagem(),
                medicamentoEntidade.getQuantidade(),
                medicamentoEntidade.getMarca(),
                medicamentoEntidade.getPrecisaReceita(),
                medicamentoEntidade.getStatus()
        );
    }
}
