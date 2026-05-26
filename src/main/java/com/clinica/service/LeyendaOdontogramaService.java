package com.clinica.service;

import com.clinica.model.entity.LeyendaOdontograma;
import com.clinica.repository.LeyendaOdontogramaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeyendaOdontogramaService {

    @Autowired
    private LeyendaOdontogramaRepository repository;

    public List<LeyendaOdontograma> obtenerActivas() {
        return repository.findByActivoTrueOrderByIdAsc();
    }

    public LeyendaOdontograma guardar(LeyendaOdontograma leyenda) {
        java.util.Optional<LeyendaOdontograma> existente = repository.findByNombreIgnoreCase(leyenda.getNombre());
        
        if (existente.isPresent() && (leyenda.getId() == null || !leyenda.getId().equals(existente.get().getId()))) {
            LeyendaOdontograma recuperada = existente.get();
            recuperada.setActivo(true);
            recuperada.setColorTailwind(leyenda.getColorTailwind());
            return repository.save(recuperada);
        }

        leyenda.setActivo(true);
        return repository.save(leyenda);
    }

    public void eliminarLogico(Long id) {
        LeyendaOdontograma leyenda = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leyenda no encontrada"));
        leyenda.setActivo(false);
        repository.save(leyenda);
    }
}