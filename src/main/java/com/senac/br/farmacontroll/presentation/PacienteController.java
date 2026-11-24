package com.senac.br.farmacontroll.presentation;

import com.senac.br.farmacontroll.application.DTOs.AtualizarStatusPacienteRequest;
import com.senac.br.farmacontroll.application.DTOs.EnumStatusPaciente;
import com.senac.br.farmacontroll.application.DTOs.PacienteRequest;
import com.senac.br.farmacontroll.application.DTOs.PacienteResponse;
import com.senac.br.farmacontroll.application.services.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// FLUXO HTTP: recebe as operações de pacientes do frontend e encaminha as validações ao serviço.

@RestController
@RequestMapping("/pacientes")
@Tag(name = "Pacientes", description = "Cadastro e consulta de pacientes vinculados ao usuário autenticado.")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping
    @Operation(summary = "Lista os pacientes do usuário autenticado")
    // GET /pacientes: lista somente pacientes associados à empresa identificada no JWT.
    public ResponseEntity<List<PacienteResponse>> listarTodos() {
        return ResponseEntity.ok(pacienteService.listarTodosPacientesTable());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um paciente do usuário autenticado")
    // GET /pacientes/{id}: o service também confere o empresa_id para impedir acesso cruzado.
    public ResponseEntity<PacienteResponse> buscarPorId(@PathVariable Long id) {
        PacienteResponse paciente = pacienteService.buscarPorId(id);
        return paciente == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(paciente);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra um paciente para o usuário autenticado")
    // POST /pacientes: recebe os dados, valida e associa automaticamente a empresa autenticada.
    public ResponseEntity<?> criar(@RequestBody PacienteRequest request) {
        try {
            return ResponseEntity.ok(pacienteService.salvarPaciente(request, 0L));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edita um paciente do usuário autenticado")
    // PUT /pacientes/{id}: altera paciente somente se o ID pertencer à empresa do token.
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody PacienteRequest request) {
        try {
            PacienteResponse paciente = pacienteService.salvarPaciente(request, id);
            return paciente == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(paciente);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/excluir")
    // DELETE lógico: altera o status para EXCLUIDO sem remover os dados do paciente.
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        return pacienteService.atualizarStatus(EnumStatusPaciente.EXCLUIDO, id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> alterarStatus(
            @PathVariable Long id,
            @RequestBody AtualizarStatusPacienteRequest request
    ) {
        return pacienteService.atualizarStatus(request.status(), id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
}
