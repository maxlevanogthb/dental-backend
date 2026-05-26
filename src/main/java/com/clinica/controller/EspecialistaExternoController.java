package com.clinica.controller;

import com.clinica.model.entity.EspecialistaExterno;
import com.clinica.service.EspecialistaExternoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/especialistas")
@CrossOrigin(origins = "http://localhost:5173")
public class EspecialistaExternoController {

    @Autowired
    private EspecialistaExternoService service;

    @GetMapping
    public List<EspecialistaExterno> listarActivos() {
        return service.listarActivos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspecialistaExterno> obtenerPorId(@PathVariable Long id) {
        EspecialistaExterno especialista = service.buscarPorId(id);
        
        if (especialista != null) {
            return ResponseEntity.ok(especialista);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public EspecialistaExterno crear(@RequestBody EspecialistaExterno especialista) {
        especialista.setActivo(true);
        return service.guardar(especialista);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EspecialistaExterno> actualizar(@PathVariable Long id, @RequestBody EspecialistaExterno datosNuevos) {
        EspecialistaExterno existente = service.buscarPorId(id);
        if (existente == null) return ResponseEntity.notFound().build();

        BeanUtils.copyProperties(datosNuevos, existente, "id", "activo");
        return ResponseEntity.ok(service.guardar(existente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.darDeBaja(id);
        return ResponseEntity.noContent().build();
    }
}