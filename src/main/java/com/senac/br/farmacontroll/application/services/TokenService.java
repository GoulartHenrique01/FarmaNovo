package com.senac.br.farmacontroll.application.services;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.senac.br.farmacontroll.application.DTOs.LoginRequest;
import com.senac.br.farmacontroll.application.DTOs.UsuarioLogado;
import com.senac.br.farmacontroll.domain.entidade.Token;
import com.senac.br.farmacontroll.domain.repository.TokenRepository;
import com.senac.br.farmacontroll.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    /*Esse TokenService existe para resolver um problema específico: depois que o usuário faz login,
    como a API vai saber que ele está autenticado nas próximas requisições?
    A resposta é: usando um token JWT.*/
    @Value("${spring.secretkey}")
    private String secret;

    @Value("${spring.expiracao}")
    private Long expiracao;

    @Value("${spring.emissor}")
    private String emissor;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenRepository tokenRepository;


    public String gerarToken(LoginRequest loginRequestDto) {

        Algorithm algorithm = Algorithm.HMAC256(secret);

        var usuario = usuarioRepository
                .findByEmail(loginRequestDto.email())
                .orElseThrow(() ->
                        new IllegalArgumentException("Usuário não encontrado")
                );

        String token = JWT.create()
                .withIssuer(emissor)
                .withSubject(usuario.getEmail())
                .withExpiresAt(this.gerarDataExpiracao())
                .sign(algorithm);

        tokenRepository.save(new Token(token, usuario));

        return token;
    }

    public UsuarioLogado verificarToken(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        JWTVerifier verificador = JWT.require(algorithm).withIssuer(emissor).build();

        verificador.verify(token);

        var tokenBanco = tokenRepository.findTokenByToken(token).orElse(null);

        if (tokenBanco==null){
            throw new IllegalArgumentException("Token Inválido");
        }

        return new UsuarioLogado(tokenBanco.getUsuario());

    }

    private Instant gerarDataExpiracao(){
        var dataAtual = LocalDateTime.now();
        dataAtual = dataAtual.plusMinutes(expiracao);

        return dataAtual.toInstant(ZoneOffset.of("-03:00"));

    }
}
