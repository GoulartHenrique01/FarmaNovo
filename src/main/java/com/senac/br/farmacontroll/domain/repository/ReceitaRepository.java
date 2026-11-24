package com.senac.br.farmacontroll.domain.repository;

import com.senac.br.farmacontroll.domain.entidade.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
// As consultas por empresa_id impedem que o service busque receitas de outra empresa.
public interface ReceitaRepository extends JpaRepository<Receita, Long> {
    List<Receita> findByEmpresa_Id(Long empresaId);
    Optional<Receita> findByIdAndEmpresa_Id(Long id, Long empresaId);
}
