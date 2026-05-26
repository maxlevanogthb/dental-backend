package com.clinica.repository;

import com.clinica.model.entity.TrabajoLaboratorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrabajoLaboratorioRepository extends JpaRepository<TrabajoLaboratorio, Long> {
}