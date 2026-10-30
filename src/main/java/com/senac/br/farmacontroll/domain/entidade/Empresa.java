package com.senac.br.farmacontroll.domain.entidade;

import com.senac.br.farmacontroll.application.DTOs.EnumStatusEmpresa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String razaoSocial;

    private String nomeFantasia;

    private String cnpj;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255)")
    private EnumStatusEmpresa status = EnumStatusEmpresa.ATIVO;

    //OneToMany private sempre será uma LIST
    @OneToMany(mappedBy = "empresa")
    private List<Usuario> usuarios;
}
