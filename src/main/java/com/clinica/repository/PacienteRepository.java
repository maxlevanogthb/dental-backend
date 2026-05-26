package com.clinica.repository;

import com.clinica.model.entity.Paciente;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Interfaz para el acceso a datos de la entidad Paciente utilizando Spring Data JPA.
 */
@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    List<Paciente> findByNombreContainingIgnoreCase(String nombre);

    Page<Paciente> findByTipoRegistro(String tipoRegistro, Pageable pageable);
    
    Page<Paciente> findAll(Pageable pageable);

    Page<Paciente> findByNombreContainingIgnoreCaseOrTelefonoContainingIgnoreCase(
    String nombre, String telefono, Pageable pageable);

    @Query("SELECT p FROM Paciente p WHERE p.tipoRegistro = :tipoRegistro AND (LOWER(p.nombre) LIKE LOWER(CONCAT('%', :buscar, '%')) OR p.telefono LIKE CONCAT('%', :buscar, '%'))")
    Page<Paciente> buscarEnDirectorio(@Param("tipoRegistro") String tipoRegistro, @Param("buscar") String buscar, Pageable pageable);
}