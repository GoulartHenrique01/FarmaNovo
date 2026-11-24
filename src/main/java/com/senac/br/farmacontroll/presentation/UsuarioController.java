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

    // Cadastro inicial do administrador global, sem chave adicional.
    @PostMapping("/admin")
    // POST /usuarios/admin: endpoint de primeiro acesso, não é o cadastro de contas das empresas.
    // Se já existir ROLE_ADMIN, UsuarioService bloqueia uma segunda criação; empresas usam POST /empresas.
    public ResponseEntity<?> criarAdmin(@RequestBody CriarAdminRequest request) {
        try {
            CriarAdminResponse resposta = usuarioService.criarAdmin(request);
            return ResponseEntity.ok(resposta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
