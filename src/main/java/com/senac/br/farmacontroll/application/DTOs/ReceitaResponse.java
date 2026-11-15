package com.senac.br.farmacontroll.application.DTOs;

import com.senac.br.farmacontroll.domain.entidade.Receita;

import java.time.LocalDate;

public record ReceitaResponse(Long id, LocalDate dataEmissao,
                              LocalDate dataValidade, String diagnostico,
                              String observacoes, String tipo, EnumStatusReceita status,
                              Long pacienteId, String pacienteNome) {
    public ReceitaResponse(Receita receitaEntidade){
        this(receitaEntidade, true);
    }

    public ReceitaResponse(Receita receitaEntidade, boolean incluirPaciente) {
        this(
                receitaEntidade.getId(),
                receitaEntidade.getDataEmissao(),
                receitaEntidade.getDataValidade(),
                receitaEntidade.getDiagnostico(),
                receitaEntidade.getObservacoes(),
                receitaEntidade.getTipo(),
                receitaEntidade.getStatus(),
                incluirPaciente && receitaEntidade.getPaciente() != null
                        ? receitaEntidade.getPaciente().getId() : null,
                incluirPaciente && receitaEntidade.getPaciente() != null
                        ? receitaEntidade.getPaciente().getNome() : null
        );
    }
}
