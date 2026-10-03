package com.senac.br.farmacontroll.application.services;

import com.senac.br.farmacontroll.application.DTOs.LoginRequest;
import com.senac.br.farmacontroll.application.DTOs.LoginResponse;
import com.senac.br.farmacontroll.application.DTOs.UsuarioResponse;
import com.senac.br.farmacontroll.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;


    public LoginResponse validarUsuarioAutenticadoRetornaToken(LoginRequest request) {

        if (usuarioRepository.existsUsuarioByEmailAndSenha(request.email(), request.senha())) {

            var token = tokenService.gerarToken(request.email());
            //Gerar o token
            return new LoginResponse(token);
        }
        return null;
    }

    public List<UsuarioResponse> listarTodosUsuariosTable(){

        return (usuarioRepository.findAll()
                .stream()
                .map(UsuarioResponse::new)
                .toList());
    }
}
