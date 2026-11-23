package com.senac.br.farmacontroll.presentation;

import com.senac.br.farmacontroll.application.DTOs.CriarAdminRequest;
import com.senac.br.farmacontroll.application.DTOs.CriarAdminResponse;
import com.senac.br.farmacontroll.application.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Cadastro inicial protegido por uma chave configurada no backend.
    @PostMapping("/admin")
    public ResponseEntity<?> criarAdmin(@RequestBody CriarAdminRequest request) {
        try {
            CriarAdminResponse resposta = usuarioService.criarAdmin(request);
            return ResponseEntity.ok(resposta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
