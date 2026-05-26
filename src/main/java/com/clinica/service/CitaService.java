package com.clinica.service;

import com.clinica.model.entity.Cita;
import com.clinica.model.entity.Paciente;
import com.clinica.repository.CitaRepository;
import com.clinica.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CitaService {

    @Autowired private CitaRepository citaRepository;
    @Autowired private PacienteRepository pacienteRepository;
    @Autowired private NotificacionService notificacionService;

    public Cita agendarCita(Cita cita) {
        Paciente p = cita.getPaciente();

        if (p.getId() == null) {
            p.setTipoRegistro("LEAD");
            p = pacienteRepository.save(p);
            cita.setPaciente(p);
        }

        Cita nuevaCita = citaRepository.save(cita);

        if (p.getCorreo() != null && !p.getCorreo().isEmpty()) {
            try {
                String mensaje = "Hola " + p.getNombre() + ", RíeDental confirma su cita para el " + 
                                 cita.getFechaHora() + ". ¡Le esperamos!";
                notificacionService.enviarCorreoCita(p.getCorreo(), "Confirmación de Cita - RíeDental", mensaje);
                System.out.println("Correo enviado con éxito.");
            } catch (Exception e) {
                System.err.println("Alerta: Cita guardada, pero no se pudo enviar el correo: " + e.getMessage());
            }
        }

        return nuevaCita;
    }

    public Cita actualizarCita(Long id, Cita datosNuevos) {
        Cita citaExistente = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        
        citaExistente.setFechaHora(datosNuevos.getFechaHora());
        citaExistente.setMotivo(datosNuevos.getMotivo());
        
        if(datosNuevos.getPaciente() != null && datosNuevos.getPaciente().getId() != null) {
            Paciente p = pacienteRepository.findById(datosNuevos.getPaciente().getId()).orElse(citaExistente.getPaciente());
            citaExistente.setPaciente(p);
        }
        
        return citaRepository.save(citaExistente);
    }
    
    public void convertirLeadAPaciente(Long pacienteId) {
        Paciente p = pacienteRepository.findById(pacienteId).orElseThrow();
        p.setTipoRegistro("PACIENTE");
        pacienteRepository.save(p);
    }
}