package com.senac.br.farmacontroll.controller;

import com.senac.br.farmacontroll.entidade.EnumStatusPaciente;
import com.senac.br.farmacontroll.entidade.Paciente;
import com.senac.br.farmacontroll.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pacientes")

@Tag(name = "Pacientes", description = "Grupo de APIs Responsável por controlar a estrutura de criação e consulta de pacientes cadastrados no sistema!")    
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @GetMapping
    @Operation(summary = "Método de consulta da lista de pacientes", description = "Método responsável em efetuar a consulta de todos os pacientes sem filtro!")
    public ResponseEntity<?> listarTodos(){
        return ResponseEntity.ok(pacienteRepository.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Método de busca de pacientes!",
            description = "Método responsável em efetuar a busca de um paciente especifico utilizando o ID!")
    public ResponseEntity<Paciente> buscarPorId(@PathVariable Long id){
        Paciente pacienteBanco = pacienteRepository.findById(id).orElse(null);
        if (pacienteBanco!=null){
            return ResponseEntity.ok(pacienteBanco);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Método de cadastro de pacientes",
            description = "Método responsável em efetuar o cadastro de pacientes!")
    public ResponseEntity<Paciente> criar(@RequestBody Paciente paciente){

        var pacienteBanco = pacienteRepository.save(paciente);
        return ResponseEntity.ok(pacienteBanco);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Método de edição de usuarios",
            description = "Método responsável pela edição de usuarios cadastrados no sistema")
    public ResponseEntity<Paciente> atualizar(@PathVariable Long id, @RequestBody Paciente paciente){
        try {
            Paciente pacienteBanco = pacienteRepository.findById(id).orElse(null);
            if (pacienteBanco!= null){
                pacienteBanco.setStatus(paciente.getStatus());
                pacienteBanco.setNome(paciente.getNome());
                pacienteBanco.setCpf(paciente.getCpf());
                pacienteBanco.setDataNascimento(paciente.getDataNascimento());
                pacienteBanco.setSexo(paciente.getSexo());
                pacienteBanco.setTelefone(paciente.getTelefone());
                pacienteBanco.setEmail(paciente.getEmail());
                pacienteBanco.setAlergias(paciente.getAlergias());
                pacienteBanco.setObservacoes(paciente.getObservacoes());
                pacienteRepository.save(pacienteBanco);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}/excluir")
    public ResponseEntity<Void> excluir(@PathVariable Long id){

        Paciente pacienteBanco = pacienteRepository.findById(id).orElse(null);
        if (pacienteBanco!= null){
            pacienteBanco.setStatus(EnumStatusPaciente.EXCLUIDO);
            pacienteRepository.save(pacienteBanco);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
