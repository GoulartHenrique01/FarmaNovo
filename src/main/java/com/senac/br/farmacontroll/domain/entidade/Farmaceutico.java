package com.senac.br.farmacontroll.domain.entidade;

import com.senac.br.farmacontroll.application.DTOs.EnumStatusFarmaceutico;
import com.senac.br.farmacontroll.application.DTOs.FarmaceuticoRequest;
import com.senac.br.farmacontroll.domain.valueObject.CPF;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Farmaceutico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @Embedded
    private CPF cpf;
    private String telefone;
    private String email;
    private String senha;
    private EnumStatusFarmaceutico status;
    private String role = "ROLE_FARMACEUTICO";


    public Farmaceutico(FarmaceuticoRequest farmaceutico) {
        this.setNome(farmaceutico.nome());
        this.setSenha(farmaceutico.senha());
        this.setEmail(farmaceutico.email());
        this.setCpf(new CPF(farmaceutico.cpf()));
        this.setRole("ROLE_FARMACEUTICO");
    }
}
