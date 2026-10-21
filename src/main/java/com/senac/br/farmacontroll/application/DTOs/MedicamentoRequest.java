package com.senac.br.farmacontroll.application.DTOs;

import java.time.LocalDate;

public record MedicamentoRequest(String nome, String tipo, LocalDate dataValidade, double dosagem, String unidadeDosagem,
                                 int quantidade, String marca, String precisaReceita) {
}
