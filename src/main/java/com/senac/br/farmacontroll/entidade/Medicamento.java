package com.senac.br.farmacontroll.entidade;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    public String nome;
    public String tipo;
    public Date dataValidade;
    public double dosagem;
    public String unidadeDosagem;
    public int quantidade;
    public String marca;
    public boolean precisaReceita;

}
