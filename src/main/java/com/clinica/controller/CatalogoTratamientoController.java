package com.clinica.controller;

import com.clinica.model.entity.CatalogoTratamiento;
import com.clinica.service.CatalogoTratamientoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalogo-tratamientos")
@CrossOrigin(origins = "http://localhost:5173") // Ajusta el puerto de tu Vue si es distinto
public class CatalogoTratamientoController {

    private final CatalogoTratamientoService service;

    public CatalogoTratamientoController(CatalogoTratamientoService service) {
        this.service = service;
    }

    @GetMapping("/activos")
    public ResponseEntity<List<CatalogoTratamiento>> obtenerActivos() {
        return ResponseEntity.ok(service.obtenerActivos());
    }

    @GetMapping
    public ResponseEntity<List<CatalogoTratamiento>> obtenerTodos() {
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @PostMapping
    public ResponseEntity<CatalogoTratamiento> crear(@RequestBody CatalogoTratamiento tratamiento) {
        return ResponseEntity.ok(service.guardar(tratamiento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatalogoTratamiento> actualizar(@PathVariable Long id, @RequestBody CatalogoTratamiento tratamiento) {
        tratamiento.setId(id);
        return ResponseEntity.ok(service.guardar(tratamiento));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminarLogico(id);
        return ResponseEntity.noContent().build();
    }
}