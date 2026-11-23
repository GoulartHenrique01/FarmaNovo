package com.senac.br.farmacontroll.domain.entidade;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// ITEM DA RECEITA: representa um medicamento já cadastrado e os dados específicos da sua prescrição nesta receita.

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceitaMedicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "receita_id", nullable = false)
    @lombok.ToString.Exclude
    @lombok.EqualsAndHashCode.Exclude
    private Receita receita;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "medicamento_id", nullable = false)
    private Medicamento medicamento;

    private String dosagemPrescrita;

    @Column(length = 1000)
    private String posologia;

    private Integer quantidadePrescrita;
    private String duracaoTratamento;
}
