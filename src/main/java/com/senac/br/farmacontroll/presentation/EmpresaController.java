package com.senac.br.farmacontroll.presentation;

import com.senac.br.farmacontroll.application.DTOs.EmpresaRequest;
import com.senac.br.farmacontroll.application.DTOs.AtualizarStatusEmpresaRequest;
import com.senac.br.farmacontroll.application.DTOs.EmpresaResponse;
import com.senac.br.farmacontroll.application.DTOs.EnumStatusEmpresa;
import com.senac.br.farmacontroll.application.services.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// FLUXO HTTP: disponibiliza ao administrador os endpoints de gerenciamento das empresas.

@RestController

@RequestMapping("/empresas")
public class EmpresaController {
    @Autowired
    private EmpresaService empresaService;

    @GetMapping
    // GET /empresas: recebe a consulta do frontend e encaminha ao service, que exige ROLE_ADMIN.
    public ResponseEntity<List<EmpresaResponse>> listarTodos(){
        return ResponseEntity.ok(empresaService.listarTodasEmpresasTable());
    }

    @GetMapping("/{id}")
    // GET /empresas/{id}: busca uma empresa específica; null no service vira HTTP 404.
    public ResponseEntity<EmpresaResponse> buscarPorId(@PathVariable Long id){
        EmpresaResponse empresaBanco = empresaService.buscarPorId(id);
        if(empresaBanco != null){
            return ResponseEntity.ok(empresaBanco);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    // POST /empresas: cria a conta única ROLE_USER da empresa; este cadastro inicial é público.
    public ResponseEntity<?> criar(@RequestBody EmpresaRequest empresa){
        try {
            EmpresaResponse empresaBanco = empresaService.salvarEmpresa(empresa, 0L);

            return ResponseEntity.ok(empresaBanco);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    // PUT /empresas/{id}: edição administrativa; o service valida o administrador e preserva senha vazia.
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody EmpresaRequest empresa){
        try {
            EmpresaResponse empresaBanco = empresaService.salvarEmpresa(empresa, id);
            if (empresaBanco == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(empresaBanco);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PatchMapping("/{id}/status")
    // PATCH /empresas/{id}/status: altera ATIVO/BLOQUEADO sem apagar o registro.
    public ResponseEntity<Void> atualizarStatus(
            @PathVariable Long id,
            @RequestBody AtualizarStatusEmpresaRequest request
    ) {
        return empresaService.atualizarStatus(request.status(), id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }



    @DeleteMapping("/{id}/excluir")
    // DELETE lógico: marca a empresa como EXCLUIDO; não remove a linha do banco.
    public ResponseEntity<Void> excluir(@PathVariable Long id){
        var atualizarEmpresa = empresaService.atualizarStatus(EnumStatusEmpresa.EXCLUIDO, id);
        if (atualizarEmpresa){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
