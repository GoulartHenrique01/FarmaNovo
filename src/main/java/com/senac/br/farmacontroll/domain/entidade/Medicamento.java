package com.senac.br.farmacontroll.domain.entidade;

import com.senac.br.farmacontroll.application.DTOs.CriarMedicamentoRequest;
import com.senac.br.farmacontroll.application.DTOs.EnumStatusMedicamento;
import com.senac.br.farmacontroll.domain.valueObject.DataValidade;
import jakarta.persistence.*;
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
    private Long id;
    private String nome;
    private String tipo;

    @Embedded
    private DataValidade dataValidade;

    private double dosagem;
    private String unidadeDosagem;
    private int quantidade;
    private String marca;
    private String precisaReceita;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255)")
    private EnumStatusMedicamento status;



    public Medicamento(CriarMedicamentoRequest criarMedicamentoRequest){
        this.setTipo(criarMedicamentoRequest.tipo());
        this.setPrecisaReceita(criarMedicamentoRequest.precisaReceita());
    }
//Alt + J
}
