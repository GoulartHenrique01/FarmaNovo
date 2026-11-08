package com.senac.br.farmacontroll.domain.valueObject;

import jakarta.persistence.Embeddable;

import java.time.LocalDate;

@Embeddable
public class DataValidade {

    private LocalDate valor;

    public DataValidade() {
    }

    public DataValidade(LocalDate valor){
        if (valor == null){
            throw new IllegalArgumentException("Data de Validade Obrigatória");
        }

        if (valor.isBefore(LocalDate.now())){
            throw new IllegalArgumentException("Data de Validade Não Pode Estar Vencida");
        }
        this.valor = valor;
    }

    public LocalDate getValor(){
        return valor;
    }

    public boolean estaVencido(){
        return valor.isBefore(LocalDate.now());
    }
}
