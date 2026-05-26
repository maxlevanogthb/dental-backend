package com.clinica.service;

import com.clinica.model.entity.CatalogoTratamiento;
import com.clinica.repository.CatalogoTratamientoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CatalogoTratamientoService {

    private final CatalogoTratamientoRepository repository;

    public CatalogoTratamientoService(CatalogoTratamientoRepository repository) {
        this.repository = repository;
    }

    // Para el odontograma (solo los que se pueden usar hoy)
    public List<CatalogoTratamiento> obtenerActivos() {
        return repository.findByActivoTrue();
    }

    // Para el panel de configuración (para ver incluso los desactivados)
    public List<CatalogoTratamiento> obtenerTodos() {
        return repository.findAll();
    }

    public CatalogoTratamiento guardar(CatalogoTratamiento tratamiento) {
        // Aseguramos que por defecto esté activo al crearse
        if (tratamiento.getActivo() == null) {
            tratamiento.setActivo(true);
        }
        return repository.save(tratamiento);
    }

    // Borrado lógico: En lugar de hacer DELETE en SQL, cambiamos el estado a false
    public void eliminarLogico(Long id) {
        repository.findById(id).ifPresent(tratamiento -> {
            tratamiento.setActivo(false);
            repository.save(tratamiento);
        });
    }
}