package com.senac.br.farmacontroll.domain.repository;

import com.senac.br.farmacontroll.domain.entidade.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    List<Paciente> findByEmpresa_IdAndUsuario_Id(Long empresaId, Long usuarioId);
    Optional<Paciente> findByIdAndEmpresa_IdAndUsuario_Id(Long id, Long empresaId, Long usuarioId);
}
