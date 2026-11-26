package com.senac.br.farmacontroll.application.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.senac.br.farmacontroll.application.DTOs.UsuarioLogado;
import com.senac.br.farmacontroll.domain.entidade.Empresa;
import com.senac.br.farmacontroll.domain.entidade.Token;
import com.senac.br.farmacontroll.domain.entidade.Usuario;
import com.senac.br.farmacontroll.domain.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

// FLUXO JWT: gera e valida o token; o filtro de segurança usa esse serviço para identificar quem fez a requisição.

@Service
public class TokenService {

    @Value("${spring.secretkey}")
    private String secret;

    @Value("${spring.expiracao}")
    private Long expiracao;

    @Value("${spring.emissor}")
    private String emissor;

    @Autowired
    private TokenRepository tokenRepository;

    // Ao iniciar a aplicação, revoga os tokens emitidos pela execução anterior.
    // Assim, reiniciar o backend invalida as sessões antigas, conforme o teste da avaliação.
    @EventListener(ApplicationReadyEvent.class)
    public void revogarTokensDaExecucaoAnterior() {
        tokenRepository.deleteAllInBatch();
    }

    // Gera JWT para o administrador global e registra o token no banco para permitir revogação/consulta posterior.
    public String gerarToken(Usuario usuario) {
        if (usuario == null || !"ROLE_ADMIN".equals(usuario.getRole()) || usuario.getEmpresa() != null) {
            throw new IllegalArgumentException("Somente o administrador global pode receber este token.");
        }
        String token = criarToken(usuario.getEmail());
        tokenRepository.save(new Token(token, usuario));
        return token;
    }

    // Gera JWT para a conta única da empresa (ROLE_USER), somente quando ela está ATIVA.
    public String gerarToken(Empresa empresa) {
        if (empresa == null || empresa.getStatus() != com.senac.br.farmacontroll.application.DTOs.EnumStatusEmpresa.ATIVO) {
            throw new IllegalArgumentException("Somente uma empresa ativa pode receber este token.");
        }
        String token = criarToken(empresa.getEmail());
        tokenRepository.save(new Token(token, empresa));
        return token;
    }

    private String criarToken(String email) {
        return JWT.create()
                .withIssuer(emissor)
                .withSubject(email)
                .withExpiresAt(gerarDataExpiracao())
                .sign(Algorithm.HMAC256(secret));
    }

    // Revoga no banco exatamente o JWT apresentado no logout.
    public void revogarToken(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token não informado.");
        }
        tokenRepository.deleteByToken(token);
    }

    // Fluxo inverso: valida assinatura/issuer/expiração, procura o token persistido e reconstrói a identidade.
    public UsuarioLogado verificarToken(String token) throws JWTVerificationException {
        JWTVerifier verificador = JWT.require(Algorithm.HMAC256(secret))
                .withIssuer(emissor)
                .build();
        var jwt = verificador.verify(token);
        Token tokenBanco = tokenRepository.findTokenByToken(token).orElse(null);
        if (tokenBanco == null) {
            throw new IllegalArgumentException("Token inválido.");
        }

        if (tokenBanco.getEmpresa() != null && tokenBanco.getUsuario() == null) {
            Empresa empresa = tokenBanco.getEmpresa();
            if (!jwt.getSubject().equals(empresa.getEmail())
                    || empresa.getStatus() != com.senac.br.farmacontroll.application.DTOs.EnumStatusEmpresa.ATIVO) {
                throw new IllegalArgumentException("Empresa do token inválida ou inativa.");
            }
            return new UsuarioLogado(empresa);
        }

        Usuario usuario = tokenBanco.getUsuario();
        if (usuario == null || !"ROLE_ADMIN".equals(usuario.getRole()) || usuario.getEmpresa() != null
                || !jwt.getSubject().equals(usuario.getEmail())
                || usuario.getStatus() != com.senac.br.farmacontroll.application.DTOs.EnumStatusUsuario.ATIVO) {
            throw new IllegalArgumentException("Usuário do token inválido ou inativo.");
        }
        if (usuario.getEmpresa() != null
                && usuario.getEmpresa().getStatus() != com.senac.br.farmacontroll.application.DTOs.EnumStatusEmpresa.ATIVO) {
            throw new IllegalArgumentException("Empresa do usuário inválida ou inativa.");
        }
        return new UsuarioLogado(usuario);
    }

    private Instant gerarDataExpiracao() {
        return LocalDateTime.now()
                .plusMinutes(expiracao)
                .toInstant(ZoneOffset.of("-03:00"));
    }
}
