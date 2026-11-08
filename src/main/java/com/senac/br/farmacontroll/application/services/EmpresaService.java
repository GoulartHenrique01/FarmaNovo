package com.senac.br.farmacontroll.application.services;

import com.senac.br.farmacontroll.application.DTOs.EmpresaRequest;
import com.senac.br.farmacontroll.application.DTOs.EmpresaResponse;
import com.senac.br.farmacontroll.application.DTOs.EnumStatusEmpresa;
import com.senac.br.farmacontroll.application.DTOs.UsuarioRequest;
import com.senac.br.farmacontroll.application.DTOs.UsuarioResponse;
import com.senac.br.farmacontroll.domain.entidade.Empresa;
import com.senac.br.farmacontroll.domain.entidade.Usuario;
import com.senac.br.farmacontroll.domain.repository.EmpresaRepository;
import com.senac.br.farmacontroll.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Value("${spring.secretkey}")
    private String secret;

    public List<EmpresaResponse> listarTodasEmpresasTable(){
        return (empresaRepository.findAll()
                .stream()
                .filter(empresa -> empresa.getStatus() != EnumStatusEmpresa.EXCLUIDO)
                .map(EmpresaResponse::new)
                .toList()
        );
    }

    @Transactional
    public EmpresaResponse salvarEmpresa(EmpresaRequest empresa, Long id){
        if(id == 0L){
            if (empresa.secretKey() == null || !secret.equals(empresa.secretKey())) {
                throw new IllegalArgumentException("Chave de cadastro inválida.");
            }
            boolean possuiDadosAdministrador = empresa.administradorNome() != null
                    || empresa.administradorEmail() != null
                    || empresa.administradorSenha() != null
                    || empresa.administradorCpf() != null;
            boolean administradorCompleto = empresa.administradorNome() != null
                    && !empresa.administradorNome().isBlank()
                    && empresa.administradorEmail() != null
                    && !empresa.administradorEmail().isBlank()
                    && empresa.administradorSenha() != null
                    && !empresa.administradorSenha().isBlank()
                    && empresa.administradorCpf() != null
                    && !empresa.administradorCpf().isBlank();
            if (possuiDadosAdministrador && !administradorCompleto) {
                throw new IllegalArgumentException("Informe os dados do administrador da empresa.");
            }
            if (administradorCompleto && usuarioRepository.existsByEmail(empresa.administradorEmail())) {
                throw new IllegalArgumentException("Já existe um usuário cadastrado com este e-mail.");
            }

            Empresa empresaBanco = new Empresa();
            empresaBanco.setRazaoSocial(empresa.razaoSocial());
            empresaBanco.setNomeFantasia(empresa.nomeFantasia());
            empresaBanco.setCnpj(empresa.cnpj());
            empresaBanco = empresaRepository.save(empresaBanco);

            if (administradorCompleto) {
                UsuarioRequest administradorRequest = new UsuarioRequest(
                        empresa.administradorNome(),
                        empresa.administradorEmail(),
                        empresa.administradorSenha(),
                        empresa.administradorCpf()
                );
                Usuario administrador = new Usuario(administradorRequest, empresaBanco);
                administrador.setRole("ROLE_EMPRESA_ADMIN");
                usuarioRepository.save(administrador);
            }

            return new EmpresaResponse(empresaBanco);
        }else{
            Empresa empresaBanco = empresaRepository.findById(id).orElse(null);
            if(empresaBanco != null){
                empresaBanco.setRazaoSocial(empresa.razaoSocial());
                empresaBanco.setNomeFantasia(empresa.nomeFantasia());
                empresaBanco.setCnpj(empresa.cnpj());
                empresaRepository.save(empresaBanco);

                return new EmpresaResponse(empresaBanco);

            }
        }
        return null;
    }

    public EmpresaResponse buscarPorId(Long id){
        var empresa = empresaRepository.findById(id).orElse(null);

        if(empresa == null){
            return null;
        }
        return new EmpresaResponse(empresa);
    }

    @Transactional
    public UsuarioResponse criarAdministrador(Long empresaId, UsuarioRequest administradorRequest) {
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada."));
        if (administradorRequest.nome() == null || administradorRequest.nome().isBlank()
                || administradorRequest.email() == null || administradorRequest.email().isBlank()
                || administradorRequest.senha() == null || administradorRequest.senha().isBlank()
                || administradorRequest.cpf() == null || administradorRequest.cpf().isBlank()) {
            throw new IllegalArgumentException("Informe todos os dados do administrador da empresa.");
        }
        if (usuarioRepository.existsByEmail(administradorRequest.email())) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com este e-mail.");
        }

        Usuario administrador = new Usuario(administradorRequest, empresa);
        administrador.setRole("ROLE_EMPRESA_ADMIN");
        usuarioRepository.save(administrador);
        return new UsuarioResponse(administrador);
    }

    public boolean atualizarStatus(EnumStatusEmpresa status, Long id){
        Empresa empresaBanco = empresaRepository.findById(id).orElse(null);
        if(empresaBanco != null){
            empresaBanco.setStatus(status);
            empresaRepository.save(empresaBanco);
            return true;
        }
        return false;
    }

}
