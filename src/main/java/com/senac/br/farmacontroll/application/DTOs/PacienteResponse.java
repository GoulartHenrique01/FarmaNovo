package com.senac.br.farmacontroll.application.DTOs;

import com.senac.br.farmacontroll.domain.entidade.Paciente;

public record PacienteResponse(
        Long id,
        String nome,
        String cpf,
        String telefone,
        EnumStatusPaciente status
) {
    public PacienteResponse(Paciente paciente) {
        this(
                paciente.getId(),
                paciente.getNome(),
                paciente.getCpf() == null ? null : paciente.getCpf().toString(),
                paciente.getTelefone(),
                paciente.getStatus()
        );
    }
}
