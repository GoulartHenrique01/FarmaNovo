package com.senac.br.farmacontroll.domain.repository;

import com.senac.br.farmacontroll.domain.entidade.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Long> {

    List<Receita> findByEmpresa_Id(Long empresaId);

    List<Receita> findByResponsavel_IdAndEmpresa_Id(Long responsavelId, Long empresaId);

    Optional<Receita> findByIdAndEmpresa_Id(Long id, Long empresaId);

    Optional<Receita> findByIdAndResponsavel_IdAndEmpresa_Id(
            Long id, Long responsavelId, Long empresaId);
}
