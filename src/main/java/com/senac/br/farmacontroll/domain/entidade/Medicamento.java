package com.senac.br.farmacontroll.domain.entidade;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String nome;
    private String tipo;
    private LocalDate dataValidade;
    private double dosagem;
    private String unidadeDosagem;
    private int quantidade;
    private String marca;
    private String precisaReceita;
    private EnumStatusMedicamento status;

//Alt + J
}
