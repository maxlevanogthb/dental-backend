package com.clinica.controller;

import com.clinica.model.entity.Cita;
import com.clinica.service.CitaService;
import com.clinica.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/citas")
@CrossOrigin(origins = "http://localhost:5173") 
public class CitaController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private CitaRepository citaRepository;

    @GetMapping
    public List<Cita> obtenerTodas() {
        return citaRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Cita> agendarCita(@RequestBody Cita cita) {
        return ResponseEntity.ok(citaService.agendarCita(cita));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Cita> actualizarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Cita cita = citaRepository.findById(id).orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        cita.setEstado(body.get("estado"));
        return ResponseEntity.ok(citaRepository.save(cita));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cita> actualizarCita(@PathVariable Long id, @RequestBody Cita cita) {
        return ResponseEntity.ok(citaService.actualizarCita(id, cita));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCita(@PathVariable Long id) {
        citaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}