package com.senac.br.farmacontroll.presentation;

import com.senac.br.farmacontroll.application.DTOs.EmpresaRequest;
import com.senac.br.farmacontroll.application.DTOs.EmpresaResponse;
import com.senac.br.farmacontroll.application.DTOs.EnumStatusEmpresa;
import com.senac.br.farmacontroll.application.services.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/empresas")
public class EmpresaController {
    @Autowired
    private EmpresaService empresaService;

    @GetMapping
    public ResponseEntity<List<EmpresaResponse>> listarTodos(){
        return ResponseEntity.ok(empresaService.listarTodasEmpresasTable());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaResponse> buscarPorId(@PathVariable Long id){
        EmpresaResponse empresaBanco = empresaService.buscarPorId(id);
        if(empresaBanco != null){
            return ResponseEntity.ok(empresaBanco);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody EmpresaRequest empresa){
        try {
            EmpresaResponse empresaBanco = empresaService.salvarEmpresa(empresa, 0L);

            return ResponseEntity.ok(empresaBanco);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody EmpresaRequest empresa){
        try {
            EmpresaResponse empresaBanco = empresaService.salvarEmpresa(empresa, id);
            return ResponseEntity.ok(empresaBanco);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/excluir")
    public ResponseEntity<Void> excluir(@PathVariable Long id){
        var atualizarEmpresa = empresaService.atualizarStatus(EnumStatusEmpresa.EXCLUIDO, id);
        if (atualizarEmpresa){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
