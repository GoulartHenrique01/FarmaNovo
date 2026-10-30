package com.senac.br.farmacontroll.domain.entidade;


import com.senac.br.farmacontroll.application.DTOs.EnumStatusReceita;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Receita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dataEmissao;
    private LocalDate dataValidade;
    private String diagnostico;
    private String observacoes;
    private String tipo;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255)")
    private EnumStatusReceita status;
}
