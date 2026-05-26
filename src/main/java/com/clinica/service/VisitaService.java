package com.clinica.service;

import com.clinica.model.entity.Paciente;
import com.clinica.model.entity.Visita;
import com.clinica.model.entity.TrabajoLaboratorio;
import com.clinica.model.entity.Laboratorio;
import com.clinica.repository.PacienteRepository;
import com.clinica.repository.VisitaRepository;
import com.clinica.repository.LaboratorioRepository; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class VisitaService {

    @Autowired
    private VisitaRepository visitaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private LaboratorioRepository laboratorioRepository; 

    public List<Visita> obtenerVisitasPorPaciente(Long pacienteId) {
        return visitaRepository.findByPacienteIdOrderByFechaDesc(pacienteId);
    }

    public Visita guardarVisita(Visita visita) {
        if (visita.getPaciente() != null && visita.getPaciente().getId() != null) {
            Paciente pacienteBD = pacienteRepository.findById(visita.getPaciente().getId())
                    .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
            visita.setPaciente(pacienteBD);
        }

        if (visita.getOrdenesLaboratorio() != null && !visita.getOrdenesLaboratorio().isEmpty()) {
            for (TrabajoLaboratorio orden : visita.getOrdenesLaboratorio()) {
                // Relaciones bidireccionales
                orden.setVisita(visita);
                orden.setPaciente(visita.getPaciente());
                
                if (orden.getEstado() == null) {
                    orden.setEstado("SOLICITADO");
                    orden.setFechaEnvio(LocalDate.now());
                }

                if (orden.getLaboratorioId() != null) {
                    Laboratorio labBD = laboratorioRepository.findById(orden.getLaboratorioId())
                            .orElseThrow(() -> new RuntimeException("Laboratorio no encontrado"));
                    orden.setLaboratorio(labBD);
                }
            }
        }
        
        return visitaRepository.save(visita);
    }

    public void eliminarVisita(Long id) {
        visitaRepository.deleteById(id);
    }
}