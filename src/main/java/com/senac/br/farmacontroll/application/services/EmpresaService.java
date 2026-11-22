package com.senac.br.farmacontroll.application.services;

import com.senac.br.farmacontroll.application.DTOs.EmpresaRequest;
import com.senac.br.farmacontroll.application.DTOs.EmpresaResponse;
import com.senac.br.farmacontroll.application.DTOs.EnumStatusEmpresa;
import com.senac.br.farmacontroll.application.DTOs.UsuarioRequest;
import com.senac.br.farmacontroll.application.DTOs.UsuarioResponse;
import com.senac.br.farmacontroll.application.DTOs.UsuarioLogado;
import com.senac.br.farmacontroll.domain.entidade.Empresa;
import com.senac.br.farmacontroll.domain.entidade.Usuario;
import com.senac.br.farmacontroll.domain.repository.EmpresaRepository;
import com.senac.br.farmacontroll.domain.repository.UsuarioRepository;
import com.senac.br.farmacontroll.domain.valueObject.CPF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    public List<EmpresaResponse> listarTodasEmpresasTable(){
        UsuarioLogado identidade = usuarioService.getUsuarioLogado();
        List<Empresa> empresas = new ArrayList<>();
        if (isAdministradorGlobal(identidade)) {
            empresas = empresaRepository.findAll();
        } else if (isEmpresa(identidade)) {
            Empresa empresaLogada = empresaRepository.findById(identidade.empresaId()).orElse(null);
            if (empresaLogada != null) {
                empresas.add(empresaLogada);
            }
        } else {
            throw new AccessDeniedException("Usuário sem permissão para consultar empresas.");
        }

        return empresas.stream()
                .filter(empresa -> empresa.getStatus() != EnumStatusEmpresa.EXCLUIDO)
                .map(EmpresaResponse::new)
                .toList();
    }

    @Transactional
    public EmpresaResponse salvarEmpresa(EmpresaRequest empresa, Long id){
        if(id == 0L){
            validarCadastroEmpresa(empresa);

            Empresa empresaBanco = new Empresa();
            empresaBanco.setRazaoSocial(empresa.razaoSocial());
            empresaBanco.setNomeFantasia(empresa.nomeFantasia());
            empresaBanco.setCnpj(empresa.cnpj());
            empresaBanco.setNomeResponsavel(empresa.administradorNome());
            empresaBanco.setCpfResponsavel(new CPF(empresa.administradorCpf()));
            empresaBanco.setEmail(empresa.administradorEmail().trim().toLowerCase());
            empresaBanco.setSenha(empresa.administradorSenha());
            empresaBanco = empresaRepository.save(empresaBanco);
            return new EmpresaResponse(empresaBanco);
        }

        UsuarioLogado identidade = usuarioService.getUsuarioLogado();
        if (!isAdministradorGlobal(identidade) && (!isEmpresa(identidade) || !id.equals(identidade.empresaId()))) {
            return null;
        }

        Empresa empresaBanco = empresaRepository.findById(id).orElse(null);
        if (empresaBanco == null) {
            return null;
        }
        validarAtualizacaoEmpresa(empresa, empresaBanco);
        empresaBanco.setRazaoSocial(empresa.razaoSocial());
        empresaBanco.setNomeFantasia(empresa.nomeFantasia());
        empresaBanco.setCnpj(empresa.cnpj());
        empresaBanco.setNomeResponsavel(empresa.administradorNome());
        empresaBanco.setCpfResponsavel(new CPF(empresa.administradorCpf()));
        empresaBanco.setEmail(empresa.administradorEmail().trim().toLowerCase());
        if (empresa.administradorSenha() != null && !empresa.administradorSenha().isBlank()) {
            empresaBanco.setSenha(empresa.administradorSenha());
        }
        return new EmpresaResponse(empresaRepository.save(empresaBanco));
    }

    public EmpresaResponse buscarPorId(Long id){
        UsuarioLogado identidade = usuarioService.getUsuarioLogado();
        if (!isAdministradorGlobal(identidade) && (!isEmpresa(identidade) || !id.equals(identidade.empresaId()))) {
            return null;
        }
        Empresa empresaBanco = empresaRepository.findById(id).orElse(null);
        if (empresaBanco == null) {
            return null;
        }
        return new EmpresaResponse(empresaBanco);
    }

    @Transactional
    public UsuarioResponse criarAdministrador(Long empresaId, UsuarioRequest administradorRequest) {
        UsuarioLogado identidade = usuarioService.getUsuarioLogado();
        if (!isAdministradorGlobal(identidade)
                && (!isEmpresa(identidade) || !empresaId.equals(identidade.empresaId()))) {
            throw new AccessDeniedException("Sem permissão para cadastrar usuários nesta empresa.");
        }
        Empresa empresa = empresaRepository.findById(empresaId).orElse(null);
        if (empresa == null) {
            throw new IllegalArgumentException("Empresa não encontrada.");
        }
        if (administradorRequest.nome() == null || administradorRequest.nome().isBlank()
                || administradorRequest.email() == null || administradorRequest.email().isBlank()
                || administradorRequest.senha() == null || administradorRequest.senha().isBlank()
                || administradorRequest.cpf() == null || administradorRequest.cpf().isBlank()) {
            throw new IllegalArgumentException("Informe todos os dados do administrador da empresa.");
        }
        String email = administradorRequest.email().trim().toLowerCase();
        if (usuarioRepository.existsByEmail(email) || empresaRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com este e-mail.");
        }

        Usuario administrador = new Usuario(administradorRequest, empresa);
        administrador.setEmail(email);
        administrador.setSenha(administradorRequest.senha());
        usuarioRepository.save(administrador);
        return new UsuarioResponse(administrador);
    }

    public boolean atualizarStatus(EnumStatusEmpresa status, Long id){
        UsuarioLogado identidade = usuarioService.getUsuarioLogado();
        if (!isAdministradorGlobal(identidade) && (!isEmpresa(identidade) || !id.equals(identidade.empresaId()))) {
            return false;
        }
        Empresa empresaBanco = empresaRepository.findById(id).orElse(null);
        if(empresaBanco != null){
            empresaBanco.setStatus(status);
            empresaRepository.save(empresaBanco);
            return true;
        }
        return false;
    }

    private void validarCadastroEmpresa(EmpresaRequest empresa) {
        if (empresa.razaoSocial() == null || empresa.razaoSocial().isBlank()
                || empresa.nomeFantasia() == null || empresa.nomeFantasia().isBlank()
                || empresa.cnpj() == null || empresa.cnpj().isBlank()
                || empresa.administradorNome() == null || empresa.administradorNome().isBlank()
                || empresa.administradorEmail() == null || empresa.administradorEmail().isBlank()
                || empresa.administradorSenha() == null || empresa.administradorSenha().isBlank()
                || empresa.administradorCpf() == null || empresa.administradorCpf().isBlank()) {
            throw new IllegalArgumentException("Informe todos os dados da empresa e da conta de acesso.");
        }
        String email = empresa.administradorEmail().trim().toLowerCase();
        if (empresaRepository.existsByEmail(email) || usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Já existe uma conta cadastrada com este e-mail.");
        }
    }

    private void validarAtualizacaoEmpresa(EmpresaRequest request, Empresa empresa) {
        if (request.administradorEmail() == null || request.administradorEmail().isBlank()) {
            throw new IllegalArgumentException("Informe o e-mail de acesso da empresa.");
        }
        String email = request.administradorEmail().trim().toLowerCase();

        //Outra empresa ja usa este e-mail?
        Empresa empresaComEmail = empresaRepository.findByEmail(email).orElse(null);
        boolean outraEmpresaUsaEmail = empresaComEmail != null
                && !empresaComEmail.getId().equals(empresa.getId());

        //Algum usuario ja usa este e-mail? (a conta antiga da propria empresa nao conta)
        Usuario usuarioComEmail = usuarioRepository.findByEmail(email).orElse(null);
        boolean usuarioUsaEmail = false;
        if (usuarioComEmail != null) {
            boolean contaAntigaDaEmpresa = "ROLE_EMPRESA_ADMIN".equals(usuarioComEmail.getRole())
                    && usuarioComEmail.getEmpresa() != null
                    && usuarioComEmail.getEmpresa().getId().equals(empresa.getId());
            usuarioUsaEmail = !contaAntigaDaEmpresa;
        }

        if (outraEmpresaUsaEmail || usuarioUsaEmail) {
            throw new IllegalArgumentException("Já existe uma conta cadastrada com este e-mail.");
        }
    }

    private boolean isEmpresa(UsuarioLogado identidade) {
        return identidade.usuarioId() == null && identidade.empresaId() != null;
    }

    private boolean isAdministradorGlobal(UsuarioLogado identidade) {
        return identidade.authorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
    }
}
