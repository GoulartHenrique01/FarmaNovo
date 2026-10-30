package com.senac.br.farmacontroll.presentation;

import com.senac.br.farmacontroll.application.DTOs.AtualizarStatusFarmaceuticoRequest;
import com.senac.br.farmacontroll.application.DTOs.FarmaceuticoRequest;
import com.senac.br.farmacontroll.application.DTOs.FarmaceuticoResponse;
import com.senac.br.farmacontroll.application.services.FarmaceuticoService;
import com.senac.br.farmacontroll.application.DTOs.EnumStatusFarmaceutico;
import com.senac.br.farmacontroll.domain.entidade.Farmaceutico;
import com.senac.br.farmacontroll.domain.repository.FarmaceuticoRepository;
import com.senac.br.farmacontroll.domain.valueObject.CPF;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/farmaceuticos")

@Tag(name = "Farmaceuticos", description = "Grupo de APIs Responsável por controlar a estrutura de criação e consulta de farmaceuticos cadastrados no sistema!")
public class FarmaceuticoController {

    @Autowired
    private FarmaceuticoRepository farmaceuticoRepository;

    @Autowired
    private FarmaceuticoService farmaceuticoService;

    @GetMapping
    @Operation(summary = "Método de consulta da lista de farmaceuticos", description = "Método responsável em efetuar a consulta de todos os farmaceuticos sem filtro!")
    public ResponseEntity<List<FarmaceuticoResponse>> listarTodos(){
        return ResponseEntity.ok(farmaceuticoService.listarTodosFarmaceuticoTable());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Método de busca de farmaceuticos!",
            description = "Método responsável em efetuar a busca de um farmaceutico especifico utilizando o ID!")
    public ResponseEntity<FarmaceuticoResponse> buscarPorId(@PathVariable Long id){
        Farmaceutico farmaceuticoBanco = farmaceuticoRepository.findById(id).orElse(null);
        if (farmaceuticoBanco!=null){
            return ResponseEntity.ok(new FarmaceuticoResponse(farmaceuticoBanco));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Método de cadastro de Farmaceutico",
            description = "Método responsável em efetuar o cadastro de Farmaceutico!")
    public ResponseEntity<?> criar(@RequestBody FarmaceuticoRequest farmaceutico){
        try {
            Farmaceutico farmaceuticoBanco = new Farmaceutico(farmaceutico);
            farmaceuticoBanco.setStatus(EnumStatusFarmaceutico.ATIVO);
            return ResponseEntity.ok(new FarmaceuticoResponse(farmaceuticoRepository.save(farmaceuticoBanco)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Método de edição de farmacêuticos",
            description = "Método responsável pela edição de farmacêuticos cadastrados no sistema")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody FarmaceuticoRequest farmaceutico){
        Farmaceutico farmaceuticoBanco = farmaceuticoRepository.findById(id).orElse(null);
        if (farmaceuticoBanco == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            farmaceuticoBanco.setNome(farmaceutico.nome());
            farmaceuticoBanco.setCpf(new CPF(farmaceutico.cpf()));
            farmaceuticoBanco.setTelefone(farmaceutico.telefone());
            farmaceuticoBanco.setEmail(farmaceutico.email());
            if (farmaceutico.senha() != null && !farmaceutico.senha().isBlank()) {
                farmaceuticoBanco.setSenha(farmaceutico.senha());
            }
            return ResponseEntity.ok(new FarmaceuticoResponse(farmaceuticoRepository.save(farmaceuticoBanco)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/excluir")
    public ResponseEntity<Void> excluir(@PathVariable Long id){

        Farmaceutico farmaceuticoBanco = farmaceuticoRepository.findById(id).orElse(null);
        if (farmaceuticoBanco!= null){
            farmaceuticoBanco.setStatus(EnumStatusFarmaceutico.EXCLUIDO);
            farmaceuticoRepository.save(farmaceuticoBanco);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> alterarStatus(@PathVariable Long id, @RequestBody AtualizarStatusFarmaceuticoRequest request) {
        Farmaceutico farmaceutico = farmaceuticoRepository.findById(id).orElse(null);
        if (farmaceutico == null) return ResponseEntity.notFound().build();
        farmaceutico.setStatus(request.status());
        farmaceuticoRepository.save(farmaceutico);
        return ResponseEntity.ok().build();
    }
}
