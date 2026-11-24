package com.senac.br.farmacontroll.domain.entidade;

import com.senac.br.farmacontroll.application.DTOs.EnumStatusPaciente;
import com.senac.br.farmacontroll.application.DTOs.PacienteRequest;
import com.senac.br.farmacontroll.domain.valueObject.CPF;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// CADASTRO DE PACIENTE: cada paciente pertence a uma empresa, mantendo os dados separados entre empresas.

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
// PACIENTE: pertence a uma Empresa; o vínculo é definido no service a partir da identidade do JWT.
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Embedded
    private CPF cpf;

    private String telefone;

    private EnumStatusPaciente status = EnumStatusPaciente.ATIVO;

    @ManyToOne
    @JoinColumn(name = "empresa_id", referencedColumnName = "id")
    private Empresa empresa;

    // Mantido como vínculo opcional para compatibilidade com registros antigos.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = true)
    private Usuario usuario;

    public Paciente(PacienteRequest paciente, Empresa empresa) {
        this.setNome(paciente.nome().trim());
        this.setCpf(new CPF(paciente.cpf()));
        this.setTelefone(paciente.telefone());
        this.setEmpresa(empresa);
        this.setUsuario(null);
    }
}
