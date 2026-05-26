package com.clinica.repository;

import com.clinica.model.entity.Visita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitaRepository extends JpaRepository<Visita, Long> {
    // Busca las visitas de un paciente y las ordena por fecha descendente
    List<Visita> findByPacienteIdOrderByFechaDesc(Long pacienteId);
}