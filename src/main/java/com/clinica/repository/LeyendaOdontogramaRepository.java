package com.clinica.repository;

import com.clinica.model.entity.LeyendaOdontograma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeyendaOdontogramaRepository extends JpaRepository<LeyendaOdontograma, Long> {
    List<LeyendaOdontograma> findByActivoTrueOrderByIdAsc();
    
    Optional<LeyendaOdontograma> findByNombreIgnoreCase(String nombre);
}