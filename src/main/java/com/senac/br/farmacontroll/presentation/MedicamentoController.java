package com.senac.br.farmacontroll.presentation;

import com.senac.br.farmacontroll.application.DTOs.*;
import com.senac.br.farmacontroll.application.services.MedicamentoService;
import com.senac.br.farmacontroll.domain.entidade.Medicamento;
import com.senac.br.farmacontroll.domain.repository.MedicamentoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/medicamentos")
@Tag(name = "Medicamentos", description = "Grupo de APIs Responsável por controlar a estrutura de criação e consulta de medicamentos cadastrados no sistema!")
public class MedicamentoController {

    @Autowired
    private MedicamentoRepository medicamentoRepository;
    @Autowired
    private MedicamentoService medicamentoService;

    @GetMapping
    @Operation(summary = "Método de consulta da lista de medicamentos",
            description = "Método responsável em efetuar a consulta de todos os medicamentos sem filtro!")
    public ResponseEntity<List<MedicamentoResponse>> listarTodos(){
        return ResponseEntity.ok(medicamentoService.listarTodosMedicamentosTable());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Método de consulta de um medicamento!",
            description = "Método responsável em efetuar a consulta de um medicamento específico, buscando pelo ID")
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
    public ResponseEntity<?> criar(@RequestBody MedicamentoRequest medicamento){
        try {
            MedicamentoResponse medicamentoBanco = medicamentoService.salvarMedicamento(medicamento, 0L);

            return ResponseEntity.ok(medicamentoBanco);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/tipo")
    public ResponseEntity<CriarMedicamentoResponse> criarTipo(@RequestBody CriarMedicamentoRequest criarMedicamentoRequest){
        try {
            CriarMedicamentoResponse respostaSalvar = medicamentoService.criarTipo(criarMedicamentoRequest);
            return ResponseEntity.ok(respostaSalvar);
        } catch (RuntimeException e) {
           return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Método de edição de medicamentos",
            description = "Método responsável pela edição de medicamentos cadastrados no sistema")
    public ResponseEntity<?> atualizar(@PathVariable Long id,
                                                 @RequestBody MedicamentoRequest medicamento){
        try {
            MedicamentoResponse medicamentoBanco = medicamentoService.salvarMedicamento(medicamento,id);
            return ResponseEntity.ok(medicamentoBanco);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/excluir")
    @Operation(summary = "Método de exclusão de medicamentos",
            description = "Método responsável pela exclusão de medicamentos cadastrados no sistema")
    public ResponseEntity<Void> excluir(@PathVariable Long id){
        Medicamento medicamentoBanco = medicamentoRepository.findById(id).orElse(null);
        if (medicamentoBanco!= null){
            medicamentoBanco.setStatus(EnumStatusMedicamento.EXCLUIDO);
            medicamentoRepository.save(medicamentoBanco);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}