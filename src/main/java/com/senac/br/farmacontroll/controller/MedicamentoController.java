package com.senac.br.farmacontroll.controller;

import com.senac.br.farmacontroll.DTOs.AtualizarStatusRequest;
import com.senac.br.farmacontroll.entidade.EnumStatusUsuario;
import com.senac.br.farmacontroll.entidade.Medicamento;
import com.senac.br.farmacontroll.entidade.Usuario;
import com.senac.br.farmacontroll.repository.MedicamentoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medicamentos")

@Tag(name = "Medicamentos", description = "Grupo de APIs Responsável por controlar a estrutura de criação e consulta de medicamentos cadastrados no sistema!")    
public class MedicamentoController {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @GetMapping
    @Operation(summary = "Método de consulta da lista de medicamentos",
            description = "Método responsável em efetuar a consulta de todos os medicamentos sem filtro!")
    public ResponseEntity<?> listarTodos(){

        return ResponseEntity.ok(medicamentoRepository.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Método de consulta de um medicamento!",
            description = "Método responsável em efetuar a consulta de um medicamento específico, buscando pelo ID")
    public ResponseEntity<Medicamento> buscarPorId(@PathVariable Long id){
        Medicamento medicamentoBanco = medicamentoRepository.findById(id).orElse(null);
        if (medicamentoBanco!=null){
            return ResponseEntity.ok(medicamentoBanco);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Método de criação de medicamentos para listar no sistema",
            description = "Método responsável em efetuar a criação de todos os medicamentos que serão passados em receitas!")
    public ResponseEntity<?> criar(@RequestBody Medicamento medicamento){

        var medicamentoBanco = medicamentoRepository.save(medicamento);

        return ResponseEntity.ok(medicamentoBanco);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Método de edição de medicamentos",
            description = "Método responsável pela edição de medicamentos cadastrados no sistema")
    public ResponseEntity<Medicamento> atualizar(@PathVariable Long id, @RequestBody Medicamento medicamento){
        try {
            Medicamento medicamentoBanco = medicamentoRepository.findById(id).orElse(null);
            if (medicamentoBanco!= null){
                medicamentoBanco.setNome(medicamento.getNome());
                medicamentoBanco.setTipo(medicamento.getTipo());
                medicamentoBanco.setDataValidade(medicamento.getDataValidade());
                medicamentoBanco.setDosagem(medicamento.getDosagem());
                medicamentoBanco.setUnidadeDosagem(medicamento.getUnidadeDosagem());
                medicamentoBanco.setQuantidade(medicamento.getQuantidade());
                medicamentoBanco.setMarca(medicamento.getMarca());
                medicamentoBanco.setPrecisaReceita(medicamento.isPrecisaReceita());
                medicamentoRepository.save(medicamentoBanco);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}/excluir")
    public ResponseEntity<Void> excluir(@PathVariable Long id){

        Medicamento medicamentoBanco = medicamentoRepository.findById(id).orElse(null);
        if (medicamentoBanco!= null){
            medicamentoRepository.save(medicamentoBanco);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}