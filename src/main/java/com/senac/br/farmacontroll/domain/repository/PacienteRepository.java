package com.senac.br.farmacontroll.domain.repository;

import com.senac.br.farmacontroll.domain.entidade.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
// As consultas por empresa_id mantêm pacientes isolados por conta de empresa.
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    List<Paciente> findByEmpresa_Id(Long empresaId);
    Optional<Paciente> findByIdAndEmpresa_Id(Long id, Long empresaId);
}
