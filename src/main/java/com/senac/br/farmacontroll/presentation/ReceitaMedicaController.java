package com.senac.br.farmacontroll.presentation;

import com.senac.br.farmacontroll.application.DTOs.EnumStatusUsuario;
import com.senac.br.farmacontroll.application.DTOs.ReceitaRequest;
import com.senac.br.farmacontroll.application.DTOs.ReceitaResponse;
import com.senac.br.farmacontroll.application.services.ReceitaService;
import com.senac.br.farmacontroll.application.DTOs.EnumStatusReceita;
import com.senac.br.farmacontroll.domain.entidade.Receita;
import com.senac.br.farmacontroll.domain.repository.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/receitas")
@Tag(name = "Receitas", description = "Grupo de APIs Responsável por controlar a estrutura de criação e consulta de receitas emitidas no sistema!")    
public class ReceitaMedicaController {

    @Autowired
    private ReceitaRepository receitaRepository;
    @Autowired
    private ReceitaService receitaService;

    @GetMapping
    @Operation(summary = "Método de consulta da lista de receitas", description = "Método responsável em efetuar a consulta de todos as receitas sem filtro!")
    public ResponseEntity<List<ReceitaResponse>> listarTodos(){
        return ResponseEntity.ok(receitaService.listarTodosReceitasTable());
    }


    @GetMapping("/{id}")
    @Operation(summary = "Método de busca de receitas!",
            description = "Método responsável em efetuar a busca de uma receita especifica utilizando o ID!")
    public ResponseEntity<Receita> buscarPorId(@PathVariable Long id){
        Receita receitaBanco = receitaRepository.findById(id).orElse(null);
        if (receitaBanco!=null){
            return ResponseEntity.ok(receitaBanco);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Método de edição de usuarios",
            description = "Método responsável pela edição de usuarios cadastrados no sistema")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody ReceitaRequest receita){
        try {
            ReceitaResponse receitaBanco = receitaService.salvarReceita(receita, id);
            return ResponseEntity.ok(receitaBanco);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Método de criação de receitas",
            description = "Método responsável em efetuar a emissão de receitas médicas")
    public ResponseEntity<?> criar(@RequestBody ReceitaRequest receita){
        try {
            ReceitaResponse receitaBanco = receitaService.salvarReceita(receita, 0L);

            return ResponseEntity.ok(receitaBanco);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/excluir")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        var atualizarReceita = receitaService.atualizarStatus(EnumStatusReceita.EXCLUIDO, id);

        if (atualizarReceita){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
