package com.senac.br.farmacontroll.domain.repository;


import com.senac.br.farmacontroll.domain.entidade.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
// Consulta o token persistido depois da validação criptográfica do JWT.
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findTokenByToken(String token);

    // Remove somente o JWT da sessão atual, sem encerrar sessões de outros dispositivos.
    void deleteByToken(String token);
}
