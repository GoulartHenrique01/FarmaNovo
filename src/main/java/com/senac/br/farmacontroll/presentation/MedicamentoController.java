package com.senac.br.farmacontroll.presentation;

import com.senac.br.farmacontroll.application.DTOs.*;
import com.senac.br.farmacontroll.application.services.MedicamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// FLUXO HTTP: expõe os endpoints de medicamentos consumidos pelas telas do frontend.

@RestController
@RequestMapping("/medicamentos")
@Tag(name = "Medicamentos", description = "Grupo de APIs Responsável por controlar a estrutura de criação e consulta de medicamentos cadastrados no sistema!")
public class MedicamentoController {

    @Autowired
    private MedicamentoService medicamentoService;

    @GetMapping
    @Operation(summary = "Método de consulta da lista de medicamentos",
            description = "Método responsável em efetuar a consulta de todos os medicamentos sem filtro!")
    // GET /medicamentos: o service identifica a empresa pelo JWT e retorna apenas o catálogo dela.
    public ResponseEntity<List<MedicamentoResponse>> listarTodos(){
        return ResponseEntity.ok(medicamentoService.listarTodosMedicamentosTable());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Método de consulta de um medicamento!",
            description = "Método responsável em efetuar a consulta de um medicamento específico, buscando pelo ID")
    // GET /medicamentos/{id}: consulta pelo ID dentro do escopo da empresa autenticada.
    public ResponseEntity<MedicamentoResponse> buscarPorId(@PathVariable Long id){
        MedicamentoResponse medicamentoBanco = medicamentoService.buscarPorId(id);
        if (medicamentoBanco!=null){
            return ResponseEntity.ok(medicamentoBanco);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Método de criação de medicamentos para listar no sistema",
            description = "Método responsável em efetuar a criação de todos os medicamentos que serão passados em receitas!")
    // POST /medicamentos: recebe os dados do formulário e delega validação/vínculo ao MedicamentoService.
    public ResponseEntity<?> criar(@RequestBody MedicamentoRequest medicamento){
        try {
            MedicamentoResponse medicamentoBanco = medicamentoService.salvarMedicamento(medicamento, 0L);

            return ResponseEntity.ok(medicamentoBanco);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/tipo")
    // POST /medicamentos/tipo: cadastra uma opção de tipo vinculada à empresa autenticada.
    public ResponseEntity<?> criarTipo(@RequestBody CriarMedicamentoRequest criarMedicamentoRequest){
        try {
            CriarMedicamentoResponse respostaSalvar = medicamentoService.criarTipo(criarMedicamentoRequest);
            return ResponseEntity.ok(respostaSalvar);
        } catch (IllegalArgumentException e) {
           return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Método de edição de medicamentos",
            description = "Método responsável pela edição de medicamentos cadastrados no sistema")
    // PUT /medicamentos/{id}: atualiza somente medicamento pertencente à empresa do token.
    public ResponseEntity<?> atualizar(@PathVariable Long id,
                                                 @RequestBody MedicamentoRequest medicamento){
        try {
            MedicamentoResponse medicamentoBanco = medicamentoService.salvarMedicamento(medicamento,id);
            if (medicamentoBanco == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(medicamentoBanco);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/excluir")
    @Operation(summary = "Método de exclusão de medicamentos",
            description = "Método responsável pela exclusão de medicamentos cadastrados no sistema")
    // DELETE lógico: marca o medicamento como EXCLUIDO, preservando histórico e referências.
    public ResponseEntity<Void> excluir(@PathVariable Long id){
        if (medicamentoService.excluir(id)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}