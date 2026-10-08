package com.senac.br.farmacontroll.application.DTOs;

import com.senac.br.farmacontroll.domain.entidade.EnumStatusReceita;
import com.senac.br.farmacontroll.domain.entidade.Receita;

import java.time.LocalDate;

public record ReceitaResponse(Long id, LocalDate dataEmissao,
                              LocalDate dataValidade, String diagnostico,
                              String observacoes, String tipo, EnumStatusReceita status) {
    public ReceitaResponse(Receita receitaEntidade){
        this(
                receitaEntidade.getId(),
                receitaEntidade.getDataEmissao(),
                receitaEntidade.getDataValidade(),
                receitaEntidade.getDiagnostico(),
                receitaEntidade.getObservacoes(),
                receitaEntidade.getTipo(),
                receitaEntidade.getStatus()
        );
    }
}
