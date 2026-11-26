package com.senac.br.farmacontroll.application.services;

import com.senac.br.farmacontroll.application.DTOs.*;
import com.senac.br.farmacontroll.domain.entidade.Empresa;
import com.senac.br.farmacontroll.domain.repository.EmpresaRepository;
import com.senac.br.farmacontroll.domain.repository.UsuarioRepository;
import com.senac.br.farmacontroll.domain.valueObject.CPF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// FLUXO: concentra as operações administrativas de cadastro, consulta, edição e bloqueio das empresas.

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UsuarioService usuarioService;


    // Fluxo: exige ROLE_ADMIN -> consulta EmpresaRepository -> remove EXCLUIDO -> converte entidade em DTO.
    public List<EmpresaResponse> listarTodasEmpresasTable() {
        exigirAdministrador();
        return empresaRepository.findAll().stream()
                .filter(empresa -> empresa.getStatus() != EnumStatusEmpresa.EXCLUIDO)
                .map(EmpresaResponse::new)
                .toList();
    }


    // Fluxo: id vazio cria uma nova conta ROLE_USER; id preenchido exige ADMIN e atualiza a empresa existente.
    public EmpresaResponse salvarEmpresa(EmpresaRequest request, Long id) {
        if (id == null || id == 0L) {
            validarCadastroEmpresa(request);
            Empresa nova = new Empresa();
            aplicarDados(nova, request, true);
            return new EmpresaResponse(empresaRepository.save(nova));
        }

        exigirAdministrador();
        Empresa empresa = empresaRepository.findById(id).orElse(null);
        if (empresa == null || empresa.getStatus() == EnumStatusEmpresa.EXCLUIDO) {
            return null;
        }
        validarAtualizacaoEmpresa(request, empresa);
        aplicarDados(empresa, request, false);
        return new EmpresaResponse(empresaRepository.save(empresa));
    }


    public EmpresaResponse buscarPorId(Long id) {
        exigirAdministrador();
        Empresa empresa = empresaRepository.findById(id).orElse(null);
        if (empresa == null || empresa.getStatus() == EnumStatusEmpresa.EXCLUIDO) {
            return null;
        }
        return new EmpresaResponse(empresa);
    }


    public boolean atualizarStatus(EnumStatusEmpresa status, Long id) {
        exigirAdministrador();
        if (status == null) {
            throw new IllegalArgumentException("Informe o status da empresa.");
        }
        Empresa empresa = empresaRepository.findById(id).orElse(null);
        if (empresa == null || empresa.getStatus() == EnumStatusEmpresa.EXCLUIDO) {
            return false;
        }
        empresa.setStatus(status);
        empresaRepository.save(empresa);
        return true;
    }

    private void validarCadastroEmpresa(EmpresaRequest request) {
        if (request == null || request.razaoSocial() == null || request.razaoSocial().isBlank()
                || request.nomeFantasia() == null || request.nomeFantasia().isBlank()
                || request.cnpj() == null || request.cnpj().isBlank()
                || request.administradorNome() == null || request.administradorNome().isBlank()
                || request.administradorEmail() == null || request.administradorEmail().isBlank()
                || request.administradorSenha() == null || request.administradorSenha().isBlank()
                || request.administradorCpf() == null || request.administradorCpf().isBlank()) {
            throw new IllegalArgumentException("Informe todos os dados da empresa e da conta de acesso.");
        }
        if (request.cep() != null && !request.cep().replaceAll("\\D", "").matches("\\d{8}")) {
            throw new IllegalArgumentException("Informe um CEP válido com 8 dígitos.");
        }
        validarEmailDisponivel(request.administradorEmail().trim().toLowerCase(), null);
        if (empresaRepository.existsByCnpj(request.cnpj().trim())) {
            throw new IllegalArgumentException("Já existe uma empresa cadastrada com este CNPJ.");
        }
    }

    private void validarAtualizacaoEmpresa(EmpresaRequest request, Empresa empresa) {
        if (request == null || request.razaoSocial() == null || request.razaoSocial().isBlank()
                || request.nomeFantasia() == null || request.nomeFantasia().isBlank()
                || request.cnpj() == null || request.cnpj().isBlank()
                || request.administradorNome() == null || request.administradorNome().isBlank()
                || request.administradorEmail() == null || request.administradorEmail().isBlank()
                || request.administradorCpf() == null || request.administradorCpf().isBlank()) {
            throw new IllegalArgumentException("Informe os dados obrigatórios da empresa.");
        }
        validarEmailDisponivel(request.administradorEmail().trim().toLowerCase(), empresa.getId());
        if (empresaRepository.existsByCnpjAndIdNot(request.cnpj().trim(), empresa.getId())) {
            throw new IllegalArgumentException("Já existe uma empresa cadastrada com este CNPJ.");
        }
    }

    private void validarEmailDisponivel(String email, Long empresaIdAtual) {
        Empresa empresaComEmail = empresaRepository.findByEmailIgnoreCase(email).orElse(null);
        if (empresaComEmail != null && !empresaComEmail.getId().equals(empresaIdAtual)) {
            throw new IllegalArgumentException("Já existe uma empresa cadastrada com este e-mail.");
        }
        if (usuarioRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("Este e-mail já pertence a uma conta administrativa.");
        }
    }

    // Centraliza a cópia dos campos do DTO para a entidade; senha vazia em edição mantém a senha anterior.
    private void aplicarDados(Empresa empresa, EmpresaRequest request, boolean novoCadastro) {
        empresa.setRazaoSocial(request.razaoSocial().trim());
        empresa.setNomeFantasia(request.nomeFantasia().trim());
        empresa.setCnpj(request.cnpj().trim());
        empresa.setNomeResponsavel(request.administradorNome().trim());
        empresa.setCpfResponsavel(new CPF(request.administradorCpf()));
        empresa.setEmail(request.administradorEmail().trim().toLowerCase());
        // Endereço informado no cadastro; em atualizações, campos ausentes preservam os dados existentes.
        if (request.cep() != null) empresa.setCep(request.cep().trim());
        if (request.logradouro() != null) empresa.setLogradouro(request.logradouro().trim());
        if (request.numero() != null) empresa.setNumero(request.numero().trim());
        if (request.complemento() != null) empresa.setComplemento(request.complemento().trim());
        if (request.bairro() != null) empresa.setBairro(request.bairro().trim());
        if (request.cidade() != null) empresa.setCidade(request.cidade().trim());
        if (request.uf() != null) empresa.setUf(request.uf().trim());
        if (novoCadastro || (request.administradorSenha() != null && !request.administradorSenha().isBlank())) {
            empresa.setSenha(request.administradorSenha());
        }
    }

    private void exigirAdministrador() {
        UsuarioLogado identidade = usuarioService.getUsuarioLogado();
        boolean admin = identidade.authorities() != null && identidade.authorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
        if (!admin) {
            throw new AccessDeniedException("Somente o administrador pode gerenciar empresas.");
        }
    }
}
