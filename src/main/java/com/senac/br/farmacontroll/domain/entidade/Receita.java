package com.senac.br.farmacontroll.domain.entidade;

import com.senac.br.farmacontroll.application.DTOs.EnumStatusReceita;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    private EnumStatusReceita status = EnumStatusReceita.ATIVO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", referencedColumnName = "id")
    private Paciente paciente;

    @OneToMany(mappedBy = "receita", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private List<ReceitaMedicamento> medicamentos = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "empresa_id", referencedColumnName = "id")
    private Empresa empresa;

    // Mantido como vínculo opcional para compatibilidade com receitas antigas.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = true)
    private Usuario usuario;
}
