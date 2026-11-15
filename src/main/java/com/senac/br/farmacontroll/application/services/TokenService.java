package com.senac.br.farmacontroll.application.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.senac.br.farmacontroll.application.DTOs.LoginRequest;
import com.senac.br.farmacontroll.application.DTOs.UsuarioLogado;
import com.senac.br.farmacontroll.domain.entidade.Empresa;
import com.senac.br.farmacontroll.domain.entidade.Token;
import com.senac.br.farmacontroll.domain.entidade.Usuario;
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
        Usuario usuario = usuarioRepository.findByEmail(loginRequestDto.email())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        return gerarToken(usuario);
    }

    public String gerarToken(Usuario usuario) {
        String token = criarToken(usuario.getEmail());
        tokenRepository.save(new Token(token, usuario));
        return token;
    }

    public String gerarToken(Empresa empresa) {
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

    public UsuarioLogado verificarToken(String token) throws JWTVerificationException {
        JWTVerifier verificador = JWT.require(Algorithm.HMAC256(secret))
                .withIssuer(emissor)
                .build();
        var jwt = verificador.verify(token);
        Token tokenBanco = tokenRepository.findTokenByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token inválido."));

        if (tokenBanco.getEmpresa() != null && tokenBanco.getUsuario() == null) {
            Empresa empresa = tokenBanco.getEmpresa();
            if (!jwt.getSubject().equals(empresa.getEmail())
                    || empresa.getStatus() != com.senac.br.farmacontroll.application.DTOs.EnumStatusEmpresa.ATIVO) {
                throw new IllegalArgumentException("Empresa do token inválida ou inativa.");
            }
            return new UsuarioLogado(empresa);
        }

        Usuario usuario = tokenBanco.getUsuario();
        if (usuario == null || !jwt.getSubject().equals(usuario.getEmail())
                || usuario.getStatus() != com.senac.br.farmacontroll.application.DTOs.EnumStatusUsuario.ATIVO) {
            throw new IllegalArgumentException("Usuário do token inválido ou inativo.");
        }
        if (usuario.getEmpresa() != null
                && usuario.getEmpresa().getStatus() != com.senac.br.farmacontroll.application.DTOs.EnumStatusEmpresa.ATIVO) {
            throw new IllegalArgumentException("Empresa do usuário inválida ou inativa.");
        }
        if ("ROLE_EMPRESA_ADMIN".equals(usuario.getRole()) && usuario.getEmpresa() != null) {
            return new UsuarioLogado(usuario.getEmpresa());
        }
        return new UsuarioLogado(usuario);
    }

    private Instant gerarDataExpiracao() {
        return LocalDateTime.now()
                .plusMinutes(expiracao)
                .toInstant(ZoneOffset.of("-03:00"));
    }
}
