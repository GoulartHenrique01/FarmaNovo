package com.senac.br.farmacontroll.controller;

import com.senac.br.farmacontroll.entidade.Receita;
import com.senac.br.farmacontroll.repository.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/receitas")
@Tag(name = "Receitas", description = "Grupo de APIs Responsável por controlar a estrutura de criação e consulta de receitas emitidas no sistema!")    
public class ReceitaMedicaController {

    @Autowired
    private ReceitaRepository receitaRepository;

    @GetMapping
    @Operation(summary = "Método de consulta da lista de receitas", description = "Método responsável em efetuar a consulta de todos as receitas sem filtro!")
    public ResponseEntity<?> listarTodos(){
        return ResponseEntity.ok(receitaRepository.findAll());
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
    public ResponseEntity<Receita> atualizar(@PathVariable Long id, @RequestBody Receita receita){
        try {
            Receita receitaBanco = receitaRepository.findById(id).orElse(null);
            if (receitaBanco!= null){
                receitaBanco.setDataEmissao(receita.getDataEmissao());
                receitaBanco.setDataValidade(receita.getDataValidade());
                receitaBanco.setDiagnostico(receita.getDiagnostico());
                receitaBanco.setObservacoes(receita.getObservacoes());
                receitaBanco.setTipo(receita.getTipo());
                receitaRepository.save(receitaBanco);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Método de criação de receitas", description = "Método responsável em efetuar a emissão de receitas médicas")
    public ResponseEntity<Receita> criar(@RequestBody Receita receita){

        var receitaBanco = receitaRepository.save(receita);
        return ResponseEntity.ok(receitaBanco);
    }
}
