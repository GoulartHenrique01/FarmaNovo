
package com.senac.br.farmacontroll.presentation;

import com.senac.br.farmacontroll.application.DTOs.LoginRequest;
import com.senac.br.farmacontroll.application.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// FLUXO DE AUTENTICAÇÃO: recebe as credenciais, valida a conta e devolve o token usado nas próximas requisições.

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Controller de autenticação")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    @Operation(summary = "Autenticação de usuários", description = "Método de login")
    // Entrada do login: recebe e-mail/senha, delega a validação ao UsuarioService e devolve o JWT ou 401.
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        var resultado = usuarioService.validarUsuarioAutenticadoRetornaToken(request);

        if (resultado != null) {
            return ResponseEntity.ok(resultado);
        }

        return ResponseEntity.status(401).body("Usuário ou senha inválidos.");
    }

    @GetMapping("/me")
    // Continuação do login: o JWT já foi validado pelo JwtFilter; retorna os dados da conta autenticada.
    public ResponseEntity<?> identidadeAutenticada() {
        return ResponseEntity.ok(usuarioService.buscarIdentidadeAutenticada());
    }
}
