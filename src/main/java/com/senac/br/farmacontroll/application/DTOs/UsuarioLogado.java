package com.senac.br.farmacontroll.application.DTOs;

import com.senac.br.farmacontroll.domain.entidade.Empresa;
import com.senac.br.farmacontroll.domain.entidade.Usuario;
import com.senac.br.farmacontroll.domain.valueObject.CPF;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public record UsuarioLogado(
        Long id,
        Long usuarioId,
        Long empresaId,
        String nome,
        String email,
        CPF cpf,
        String status,
        Collection<? extends GrantedAuthority> authorities
) {

    public UsuarioLogado(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getId(),
                usuario.getEmpresa() == null ? null : usuario.getEmpresa().getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCpf(),
                usuario.getStatus() == null ? null : usuario.getStatus().name(),
                usuario.getAuthorities()
        );
    }

    public UsuarioLogado(Empresa empresa) {
        this(
                empresa.getId(),
                null,
                empresa.getId(),
                empresa.getNomeFantasia(),
                empresa.getEmail(),
                empresa.getCpfResponsavel(),
                empresa.getStatus() == null ? null : empresa.getStatus().name(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
