package com.senac.br.farmacontroll.services;

import com.senac.br.farmacontroll.entidade.PasswordResetToken;
import com.senac.br.farmacontroll.entidade.Usuario;
import com.senac.br.farmacontroll.repository.PasswordResetTokenRepository;
import com.senac.br.farmacontroll.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    public String gerarTokenRecuperacao(String email){

        var usuario = usuarioRepository.findByEmail(email);

        if (usuario.isEmpty()){
            throw new RuntimeException("Usuário não encontrado");
        }

        String token = UUID.randomUUID().toString();

        PasswordResetToken passwordResetToken = new PasswordResetToken();

        passwordResetToken.setToken(token);
        passwordResetToken.setUsuario(usuario.get());
        passwordResetToken.setExpiracao(LocalDateTime.now().plusMinutes(15));

        passwordResetTokenRepository.save(passwordResetToken);

        return token;
    }

    public boolean tokenExpirado(PasswordResetToken passwordResetToken){
        return LocalDateTime.now().isAfter(passwordResetToken.getExpiracao());
    }


    public PasswordResetToken validarToken(String token) {

        var tokenEncontrado = passwordResetTokenRepository.findByToken(token);

        if (tokenEncontrado.isEmpty()) {
            throw new RuntimeException("Token inválido");
        }

        PasswordResetToken passwordResetToken = tokenEncontrado.get();

        if (LocalDateTime.now().isAfter(passwordResetToken.getExpiracao())) {
            throw new RuntimeException("Token expirado");
        }

        return passwordResetToken;
    }

    public void recuperarSenha(String token, String novaSenha) {

        PasswordResetToken passwordResetToken = validarToken(token);

        Usuario usuario = passwordResetToken.getUsuario();

        usuario.setSenha(novaSenha);

        usuarioRepository.save(usuario);

        passwordResetTokenRepository.delete(passwordResetToken);
    }
}
