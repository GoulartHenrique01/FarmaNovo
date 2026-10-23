package com.senac.br.farmacontroll.domain.repository;


import com.senac.br.farmacontroll.domain.entidade.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findTokenByToken(String token);
}
