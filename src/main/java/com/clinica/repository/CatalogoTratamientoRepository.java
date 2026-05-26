package com.clinica.repository;

import com.clinica.model.entity.CatalogoTratamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CatalogoTratamientoRepository extends JpaRepository<CatalogoTratamiento, Long> {
    
    // Método mágico de Spring Data: Trae solo los tratamientos que no han sido borrados
    List<CatalogoTratamiento> findByActivoTrue();
}