package com.senac.br.farmacontroll.domain.repository;

import com.senac.br.farmacontroll.domain.entidade.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicamentoRepository extends JpaRepository<Medicamento,Long> {
    List<Medicamento> findByEmpresa_Id(Long empresaId);
    Optional<Medicamento> findByIdAndEmpresa_Id(Long id, Long empresaId);
}
