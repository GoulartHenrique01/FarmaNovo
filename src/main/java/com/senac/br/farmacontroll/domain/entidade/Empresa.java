package com.senac.br.farmacontroll.domain.entidade;

import com.senac.br.farmacontroll.application.DTOs.EnumStatusEmpresa;
import com.senac.br.farmacontroll.domain.valueObject.CPF;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

// CONTA DA EMPRESA: representa a empresa e suas credenciais únicas de acesso ao sistema.

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
// EMPRESA/CONTA USER: um registro reúne os dados da empresa e as credenciais únicas usadas no login ROLE_USER.
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String razaoSocial;

    private String nomeFantasia;

    private String cnpj;

    private String cep;

    private String logradouro;

    private String numero;

    private String complemento;

    private String bairro;

    private String cidade;

    private String uf;

    @Column(unique = true)
    private String email;

    private String senha;

    private String nomeResponsavel;

    @Embedded
    @AttributeOverride(name = "cpf", column = @Column(name = "responsavel_cpf"))
    private CPF cpfResponsavel;

    private EnumStatusEmpresa status = EnumStatusEmpresa.ATIVO;

    public String getRole() {
        return "ROLE_USER";
    }

    public Collection<SimpleGrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(getRole()));
    }
}
