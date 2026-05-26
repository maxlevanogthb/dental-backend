package com.clinica.controller;

import com.clinica.model.entity.Visita;
import com.clinica.service.VisitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visitas")
@CrossOrigin(origins = "http://localhost:5173")
public class VisitaController {

    @Autowired
    private VisitaService visitaService;

    @GetMapping("/paciente/{pacienteId}")
    public List<Visita> obtenerPorPaciente(@PathVariable Long pacienteId) {
        return visitaService.obtenerVisitasPorPaciente(pacienteId);
    }

    @PostMapping
    public ResponseEntity<Visita> crearVisita(@RequestBody Visita visita) {
        Visita nuevaVisita = visitaService.guardarVisita(visita);
        return ResponseEntity.ok(nuevaVisita);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVisita(@PathVariable Long id) {
        visitaService.eliminarVisita(id);
        return ResponseEntity.ok().build();
    }
}