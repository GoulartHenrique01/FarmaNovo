package com.senac.br.farmacontroll.domain.entidade;

import com.senac.br.farmacontroll.application.DTOs.CriarAdminRequest;
import com.senac.br.farmacontroll.domain.valueObject.CPF;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    @Embedded
    private CPF cpf;
    private String senha;
    private String email;
    private String role = "ROLE_USER";

    private EnumStatusUsuario status = EnumStatusUsuario.ATIVO;

    public Usuario(CriarAdminRequest criarAdminRequest) {
        this.setCpf(new CPF(criarAdminRequest.cpf()));
        this.setNome(criarAdminRequest.nome());
        this.setSenha(criarAdminRequest.senha());
        this.setEmail(criarAdminRequest.email());
        this.setRole("ROLE_ADMIN");

    }
}
