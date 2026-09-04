package com.senac.br.farmacontroll.repository;

import com.senac.br.farmacontroll.entidade.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicamentoRepository extends JpaRepository<Medicamento,Long> {
}
