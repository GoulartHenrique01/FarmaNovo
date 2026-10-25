package com.senac.br.farmacontroll.domain.repository;

import com.senac.br.farmacontroll.domain.entidade.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
}
