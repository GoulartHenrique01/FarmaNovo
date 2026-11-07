package com.senac.br.farmacontroll.presentation;

import com.senac.br.farmacontroll.application.DTOs.EnderecoResponse;
import com.senac.br.farmacontroll.application.services.EnderecoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/endereco")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    @GetMapping("/{cep}")
    @Operation(summary = "Método de busca de endereço",
    description = "Método que busca endereços completos por CEP")
    public EnderecoResponse buscarEndereco(@PathVariable String cep){
        return enderecoService.BuscarEndereco(cep);
    }

}
