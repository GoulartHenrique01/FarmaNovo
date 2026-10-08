package com.senac.br.farmacontroll.application.DTOs;

import com.senac.br.farmacontroll.domain.entidade.EnumStatusPaciente;
import com.senac.br.farmacontroll.domain.entidade.Paciente;

import java.time.LocalDate;

public record PacienteResponse(Long id, String nome, String cpf, LocalDate dataNascimento, String sexo, String telefone,
                               String email, String alergias,
                               String observacoes, EnumStatusPaciente status) {
    public PacienteResponse(Paciente pacienteEntidade){
        this(
            pacienteEntidade.getId(),
            pacienteEntidade.getNome(),
            pacienteEntidade.getCpf(),
            pacienteEntidade.getDataNascimento(),
            pacienteEntidade.getSexo(),
            pacienteEntidade.getTelefone(),
            pacienteEntidade.getEmail(),
            pacienteEntidade.getAlergias(),
            pacienteEntidade.getObservacoes(),
            pacienteEntidade.getStatus()
        );
    }
}
