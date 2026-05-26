package com.clinica.service;

import com.clinica.model.entity.TrabajoLaboratorio;
import com.clinica.model.entity.Paciente;
import com.clinica.model.entity.Laboratorio;
import com.clinica.repository.TrabajoLaboratorioRepository;
import com.clinica.repository.PacienteRepository;
import com.clinica.repository.LaboratorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TrabajoLaboratorioService {

    @Autowired
    private TrabajoLaboratorioRepository repository;

    @Autowired
    private PacienteRepository pacienteRepository;
    
    @Autowired
    private LaboratorioRepository laboratorioRepository;

    public List<TrabajoLaboratorio> obtenerTodos() {
        return repository.findAll();
    }

    public TrabajoLaboratorio actualizarEstado(Long id, String nuevoEstado) {
        TrabajoLaboratorio trabajo = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        trabajo.setEstado(nuevoEstado);
        return repository.save(trabajo);
    }

    public TrabajoLaboratorio crearOrdenDirecta(TrabajoLaboratorio orden) {
        Paciente paciente = pacienteRepository.findById(orden.getPaciente().getId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        orden.setPaciente(paciente);

        Laboratorio lab = laboratorioRepository.findById(orden.getLaboratorioId())
                .orElseThrow(() -> new RuntimeException("Laboratorio no encontrado"));
        orden.setLaboratorio(lab);

        orden.setEstado("SOLICITADO");
        orden.setFechaEnvio(LocalDate.now());

        return repository.save(orden);
    }

    public TrabajoLaboratorio actualizarOrden(Long id, TrabajoLaboratorio datosNuevos) {
        TrabajoLaboratorio orden = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        // Actualizar relaciones (Paciente y Laboratorio)
        if(datosNuevos.getPaciente() != null && datosNuevos.getPaciente().getId() != null) {
            orden.setPaciente(pacienteRepository.findById(datosNuevos.getPaciente().getId()).orElse(orden.getPaciente()));
        }
        if(datosNuevos.getLaboratorioId() != null) {
            orden.setLaboratorio(laboratorioRepository.findById(datosNuevos.getLaboratorioId()).orElse(orden.getLaboratorio()));
        }

        // --- ACTUALIZACIÓN SEGURA DE LA LISTA DE DETALLES PARA HIBERNATE ---
        if (orden.getDetalles() != null) {
            orden.getDetalles().clear();
            if (datosNuevos.getDetalles() != null) {
                orden.getDetalles().addAll(datosNuevos.getDetalles());
            }
        } else {
            orden.setDetalles(datosNuevos.getDetalles());
        }

        // Actualizar datos planos
        orden.setCostoLaboratorio(datosNuevos.getCostoLaboratorio());
        orden.setIndicaciones(datosNuevos.getIndicaciones());
        orden.setFechaEsperadaEntrega(datosNuevos.getFechaEsperadaEntrega());

        return repository.save(orden);
    }
}