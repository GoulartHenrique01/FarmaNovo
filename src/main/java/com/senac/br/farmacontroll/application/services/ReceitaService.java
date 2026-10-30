package com.senac.br.farmacontroll.application.services;

import com.senac.br.farmacontroll.application.DTOs.EnumStatusReceita;
import com.senac.br.farmacontroll.application.DTOs.EnumStatusUsuario;
import com.senac.br.farmacontroll.application.DTOs.ReceitaRequest;
import com.senac.br.farmacontroll.application.DTOs.ReceitaResponse;
import com.senac.br.farmacontroll.domain.entidade.Receita;
import com.senac.br.farmacontroll.domain.repository.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceitaService {

    @Autowired
    private ReceitaRepository receitaRepository;

    public List<ReceitaResponse> listarTodosReceitasTable(){

        return (receitaRepository.findAll()
                .stream()
                .map(ReceitaResponse::new)
                .toList());
    }

    public ReceitaResponse salvarReceita(ReceitaRequest receita, Long id){
        if (id == 0L){
            Receita receitaBanco = new Receita();
            receitaBanco.setDataEmissao(receita.dataEmissao());
            receitaBanco.setDataValidade(receita.dataValidade());
            receitaBanco.setDiagnostico(receita.diagnostico());
            receitaBanco.setObservacoes(receita.observacoes());
            receitaBanco.setTipo(receita.tipo());
            receitaBanco.setStatus(EnumStatusReceita.ATIVO);
            receitaRepository.save(receitaBanco);

            return new ReceitaResponse(receitaBanco);
        }else{
            Receita receitaBanco = receitaRepository.findById(id).orElse(null);
            if (receitaBanco!= null){
                receitaBanco.setDataEmissao(receita.dataEmissao());
                receitaBanco.setDataValidade(receita.dataValidade());
                receitaBanco.setDiagnostico(receita.diagnostico());
                receitaBanco.setObservacoes(receita.observacoes());
                receitaBanco.setTipo(receita.tipo());
                receitaRepository.save(receitaBanco);
                return new ReceitaResponse(receitaBanco);
            }
        }
        return null;
    }

    public boolean atualizarStatus(EnumStatusReceita status, Long id){
        Receita receitaBanco = receitaRepository.findById(id).orElse(null);
        if (receitaBanco!= null){
            receitaBanco.setStatus(status);
            receitaRepository.save(receitaBanco);
            return true;
        }
        return false;
    }
}
