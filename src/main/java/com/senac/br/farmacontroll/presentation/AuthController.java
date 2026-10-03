package com.senac.br.farmacontroll.presentation;

import com.senac.br.farmacontroll.application.DTOs.ForgotPasswordRequest;
import com.senac.br.farmacontroll.application.DTOs.LoginRequest;
import com.senac.br.farmacontroll.application.DTOs.ResetPasswordRequest;
import com.senac.br.farmacontroll.application.services.PasswordResetService;
import com.senac.br.farmacontroll.application.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.HttpURLConnection;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Controller de autenticação")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;


    /*
    @Autowired
    private PasswordResetService passwordResetService;
    */

    @PostMapping("/login")
    @Operation(summary = "Autenticação de usuarios", description = "Método de login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){

        var resultadoAutenticacaoRetornoToken = usuarioService.validarUsuarioAutenticadoRetornaToken(request);

            if (resultadoAutenticacaoRetornoToken != null){
            return ResponseEntity.ok(resultadoAutenticacaoRetornoToken);
        }
        return ResponseEntity.badRequest().body("Usuario ou senha invalido!");
    }

    //Recuperação Senha
    /*@PostMapping("/login/esqueci-senha")
    @Operation(
            summary = "Solicitar recuperação de senha",
            description = "Gera um token temporário para recuperação da senha"
    )
    public ResponseEntity<?> esqueciSenha(
            @RequestBody ForgotPasswordRequest request) {

        String token = passwordResetService.gerarTokenRecuperacao(
                request.email()
        );

        return ResponseEntity.ok(token);
    }

    @PostMapping("/login/recuperar-senha")
    @Operation(
            summary = "Recuperar senha",
            description = "Valida o token e altera a senha do usuário"
    )
    public ResponseEntity<?> recuperarSenha(
            @RequestBody ResetPasswordRequest request) {

        passwordResetService.recuperarSenha(
                request.token(),
                request.novaSenha()
        );

        return ResponseEntity.ok("Senha alterada com sucesso!");
    }*/
}

