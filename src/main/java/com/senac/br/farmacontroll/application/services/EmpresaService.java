package com.senac.br.farmacontroll.application.services;

import com.senac.br.farmacontroll.application.DTOs.EmpresaRequest;
import com.senac.br.farmacontroll.application.DTOs.EmpresaResponse;
import com.senac.br.farmacontroll.application.DTOs.EnumStatusEmpresa;
import com.senac.br.farmacontroll.domain.entidade.Empresa;
import com.senac.br.farmacontroll.domain.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    public List<EmpresaResponse> listarTodasEmpresasTable(){
        return (empresaRepository.findAll()
                .stream()
                .map(EmpresaResponse::new)
                .toList()
        );
    }

    public EmpresaResponse salvarEmpresa(EmpresaRequest empresa, Long id){
        if(id == 0L){
            Empresa empresaBanco = new Empresa();

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
