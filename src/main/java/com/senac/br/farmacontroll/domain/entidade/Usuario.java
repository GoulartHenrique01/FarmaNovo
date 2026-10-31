package com.senac.br.farmacontroll.domain.entidade;

import com.senac.br.farmacontroll.application.DTOs.CriarAdminRequest;
import com.senac.br.farmacontroll.application.DTOs.EnumStatusUsuario;
import com.senac.br.farmacontroll.application.DTOs.UsuarioLogado;
import com.senac.br.farmacontroll.application.DTOs.UsuarioRequest;
import com.senac.br.farmacontroll.domain.valueObject.CPF;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    @Embedded
    private CPF cpf;
    private String senha;
    private String email;
    private String role = "ROLE_FARMACEUTICO";
    private EnumStatusUsuario status = EnumStatusUsuario.ATIVO;

    //EAGER carrega a lista de registros instantaneamente
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "empresa_id", referencedColumnName = "id")
    private Empresa empresa;

    //DDD
    public void setEmail(String email) {
        if(!email.isEmpty() && !email.contains("@")){
            throw new IllegalArgumentException("Email Invalido!");
        }
        this.email = email;
    }


    public Usuario(CriarAdminRequest criarAdminRequest) {
        this.setCpf(new CPF(criarAdminRequest.cpf()));
        this.setNome(criarAdminRequest.nome());
        this.setSenha(criarAdminRequest.senha());
        this.setEmail(criarAdminRequest.email());
        this.setRole("ROLE_ADMIN");
    }

    public Usuario(UsuarioRequest usuario) {
        this.setCpf(new CPF(usuario.cpf()));
        this.setNome(usuario.nome());
        this.setSenha(usuario.senha());
        this.setEmail(usuario.email());
        this.setRole("ROLE_USER");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleUsuario = this.role == null || this.role.isBlank()
                ? "ROLE_USER"
                : this.role;
        return List.of(new SimpleGrantedAuthority(roleUsuario));
    }

    @Override
    public @Nullable String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }
}
