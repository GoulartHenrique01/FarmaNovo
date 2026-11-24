package com.senac.br.farmacontroll.domain.entidade;

import com.senac.br.farmacontroll.application.DTOs.CriarMedicamentoRequest;
import com.senac.br.farmacontroll.application.DTOs.EnumStatusMedicamento;
import com.senac.br.farmacontroll.domain.valueObject.DataValidade;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// CADASTRO DE ESTOQUE: representa o medicamento disponível no catálogo da empresa; receitas apenas referenciam este registro.

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
// MEDICAMENTO: pertence ao catálogo de uma Empresa. ReceitaMedicamento referencia este registro existente.
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
    private EnumStatusMedicamento status = EnumStatusMedicamento.ATIVO;

    @ManyToOne
    @JoinColumn(name = "empresa_id", referencedColumnName = "id")
    private Empresa empresa;



    public Medicamento(CriarMedicamentoRequest criarMedicamentoRequest){
        this.setTipo(criarMedicamentoRequest.tipo());
        this.setPrecisaReceita(criarMedicamentoRequest.precisaReceita());
    }
//Alt + J
}
