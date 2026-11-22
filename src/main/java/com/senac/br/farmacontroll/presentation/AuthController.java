
package com.senac.br.farmacontroll.presentation;

import com.senac.br.farmacontroll.application.DTOs.LoginRequest;
import com.senac.br.farmacontroll.application.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Controller de autenticação")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    @Operation(summary = "Autenticação de usuários", description = "Método de login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        var resultado = usuarioService.validarUsuarioAutenticadoRetornaToken(request);

        if (resultado != null) {
            return ResponseEntity.ok(resultado);
        }

        return ResponseEntity.status(401).body("Usuário ou senha inválidos.");
    }

    @GetMapping("/me")
    public ResponseEntity<?> identidadeAutenticada() {
        return ResponseEntity.ok(usuarioService.buscarIdentidadeAutenticada());
    }
}
