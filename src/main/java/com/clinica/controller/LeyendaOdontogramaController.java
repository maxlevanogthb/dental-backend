package com.clinica.controller;

import com.clinica.model.entity.LeyendaOdontograma;
import com.clinica.service.LeyendaOdontogramaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leyendas-odontograma")
@CrossOrigin(origins = "http://localhost:5173") 
public class LeyendaOdontogramaController {

    @Autowired
    private LeyendaOdontogramaService service;

    @GetMapping
    public List<LeyendaOdontograma> obtenerActivas() {
        return service.obtenerActivas();
    }

    @PostMapping
    public ResponseEntity<LeyendaOdontograma> crear(@RequestBody LeyendaOdontograma leyenda) {
        return ResponseEntity.ok(service.guardar(leyenda));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeyendaOdontograma> actualizar(@PathVariable Long id, @RequestBody LeyendaOdontograma leyenda) {
        leyenda.setId(id);
        return ResponseEntity.ok(service.guardar(leyenda));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminarLogico(id);
        return ResponseEntity.ok().build();
    }
}