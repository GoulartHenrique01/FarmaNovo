package com.senac.br.farmacontroll.repository;

import com.senac.br.farmacontroll.entidade.EnumStatusUsuario;
import com.senac.br.farmacontroll.entidade.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsUsuarioByEmailAndSenha(String email, String senha);

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByStatusNot(EnumStatusUsuario status);

}
