package com.senac.br.farmacontroll.presentation;

import com.senac.br.farmacontroll.application.DTOs.*;
import com.senac.br.farmacontroll.application.services.UsuarioService;
import com.senac.br.farmacontroll.application.DTOs.EnumStatusUsuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/usuarios")
/*A @Tag é uma anotação do Swagger/OpenAPI usada para agrupar e organizar os endpoints da sua API na documentação do Swagger UI.*/
@Tag(name = "Usuario", description = "Grupo de APIs Responsável por controlar a estrutura de criação e consulta de usuários do sistema!")
public class UsuarioController {


    @Autowired
    UsuarioService usuarioService;

    @PostMapping("/admin")
    public ResponseEntity<CriarAdminResponse> criarAdmin(@RequestBody CriarAdminRequest criarAdminRequest){

        try{
            CriarAdminResponse respostaSalvar = usuarioService.criarAdmin(criarAdminRequest);
            return ResponseEntity.ok(respostaSalvar);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Método de busca de usuários!",
            description = "Método responsável em efetuar a busca de um usuario especifico utilizando o ID!")

    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id){

        UsuarioResponse usuarioBanco = usuarioService.buscarPorId(id);
        if(usuarioBanco!= null ){
            return  ResponseEntity.ok(usuarioBanco);
        }

        return  ResponseEntity.notFound().build();
    }

    @GetMapping
    /*Esse @Operation também é do Swagger/OpenAPI, mas ele serve para documentar uma operação específica da API,
    enquanto o @Tag organiza o controller em um grupo.*/
    @Operation(summary = "Método de consulta de lista de usuários!",
            description = "Método responsável em efetuar a consulta de todos os usuários sem filtro!")
    public ResponseEntity<List<UsuarioResponse>> listarTodos(){

        return ResponseEntity.ok(usuarioService.listarTodosUsuariosTable());
    }

    /*Acesso -> saida -> Nome -> Entrada*/
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Método de criação de usuários!",
            description = "Método responsável em efetuar a criação de novos usuários!")
    public ResponseEntity<?> criar(@RequestBody UsuarioRequest usuario){

        try {
        UsuarioResponse usuarioBanco = usuarioService.salvarUsuario(usuario, 0L);
        return ResponseEntity.ok(usuarioBanco);
    }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    /* Quando o cliente precisa atualizar apenas alguns atributos (ex: mudar apenas o e-mail de um usuário)*/
    @PatchMapping("/{id}/status")
    @Operation(summary = "Método de edição do Status!",
            description = "Método responsável em editar o Status do Usuario!")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @RequestBody AtualizarStatusRequest statusRequest){
        var atualizarUsuario = usuarioService.atualizarStatus(statusRequest.status(),id);

        if (atualizarUsuario){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }


    /*É utilizado para atualizações completas, onde o corpo da requisição deve conter todos os campos do objeto,
    caso algum campo não seja incluído no corpo da requisição, ele pode ser resolvido para null ou valor padrão*/
    @PutMapping("/{id}")
    @Operation(summary = "Método de edição de usuarios",
            description = "Método responsável pela edição de usuarios cadastrados no sistema")
    public ResponseEntity<?> atualizar(@PathVariable Long id,
                                       @RequestBody UsuarioRequest usuario){
        try {
            UsuarioResponse usuarioBanco = usuarioService.salvarUsuario(usuario,id);
            return ResponseEntity.ok(usuarioBanco);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping("/{id}/excluir")
    public ResponseEntity<Void> excluir(@PathVariable Long id){

        var atualizarUsuario = usuarioService.atualizarStatus(EnumStatusUsuario.EXCLUIDO,id);

        if(atualizarUsuario){
            return ResponseEntity.ok().build();
        }
        return  ResponseEntity.notFound().build();
    }

    @GetMapping("/buscarusuariologado")
    @Operation(summary = "Usuario logado", description = "Usuario logado")
    public  ResponseEntity<UsuarioResponse> buscarUsuarioLogado(){
        return ResponseEntity.ok(usuarioService.BuscarUsuarioLogado());
    }
}