package com.clinica.controller;

import com.clinica.model.entity.TrabajoLaboratorio;
import com.clinica.service.TrabajoLaboratorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trabajos-laboratorio")
@CrossOrigin(origins = "http://localhost:5173")
public class TrabajoLaboratorioController {

    @Autowired
    private TrabajoLaboratorioService service;

    @GetMapping
    public List<TrabajoLaboratorio> listarTodos() {
        return service.obtenerTodos();
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<TrabajoLaboratorio> actualizarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String nuevoEstado = body.get("estado");
        return ResponseEntity.ok(service.actualizarEstado(id, nuevoEstado));
    }

    @PostMapping
    public ResponseEntity<TrabajoLaboratorio> crearOrden(@RequestBody TrabajoLaboratorio orden) {
        return ResponseEntity.ok(service.crearOrdenDirecta(orden));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrabajoLaboratorio> actualizarOrden(@PathVariable Long id, @RequestBody TrabajoLaboratorio orden) {
        return ResponseEntity.ok(service.actualizarOrden(id, orden));
    }
}