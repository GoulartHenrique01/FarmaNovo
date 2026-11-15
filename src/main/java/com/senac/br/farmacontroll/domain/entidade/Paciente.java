package com.senac.br.farmacontroll.domain.entidade;

import com.senac.br.farmacontroll.application.DTOs.EnumStatusPaciente;
import com.senac.br.farmacontroll.application.DTOs.PacienteRequest;
import com.senac.br.farmacontroll.domain.valueObject.CPF;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Embedded
    private CPF cpf;

    private String telefone;

    private EnumStatusPaciente status = EnumStatusPaciente.ATIVO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", referencedColumnName = "id", nullable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    private Usuario usuario;

    public Paciente(PacienteRequest paciente, Usuario usuario, Empresa empresa) {
        this.setNome(paciente.nome().trim());
        this.setCpf(new CPF(paciente.cpf()));
        this.setTelefone(paciente.telefone());
        this.setEmpresa(empresa);
        this.setUsuario(usuario);
    }
}
