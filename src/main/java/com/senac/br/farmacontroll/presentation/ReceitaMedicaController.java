package com.senac.br.farmacontroll.presentation;

import com.senac.br.farmacontroll.application.DTOs.EnumStatusReceita;
import com.senac.br.farmacontroll.application.DTOs.ReceitaRequest;
import com.senac.br.farmacontroll.application.DTOs.ReceitaResponse;
import com.senac.br.farmacontroll.application.services.ReceitaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/receitas")
@Tag(name = "Receitas", description = "Cadastro, consulta e gerenciamento de receitas médicas.")
public class ReceitaMedicaController {

    @Autowired
    private ReceitaService receitaService;

    @GetMapping
    @Operation(summary = "Lista as receitas disponíveis para o usuário autenticado")
    public ResponseEntity<List<ReceitaResponse>> listarTodos() {
        return ResponseEntity.ok(receitaService.listarTodosReceitasTable());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma receita pelo identificador")
    public ResponseEntity<ReceitaResponse> buscarPorId(@PathVariable Long id) {
        ReceitaResponse receita = receitaService.buscarPorId(id);
        return receita == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(receita);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra uma receita para a empresa autenticada")
    public ResponseEntity<?> criar(@RequestBody ReceitaRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(receitaService.salvarReceita(request, 0L));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edita uma receita dentro do escopo do usuário autenticado")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody ReceitaRequest request) {
        try {
            ReceitaResponse receita = receitaService.salvarReceita(request, id);
            return receita == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(receita);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/excluir")
    @Operation(summary = "Exclui logicamente uma receita")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        return receitaService.atualizarStatus(EnumStatusReceita.EXCLUIDO, id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
}
