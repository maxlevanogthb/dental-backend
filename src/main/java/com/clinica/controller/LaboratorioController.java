package com.clinica.controller;

import com.clinica.model.entity.Laboratorio;
import com.clinica.service.LaboratorioService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/laboratorios")
@CrossOrigin(origins = "http://localhost:5173")
public class LaboratorioController {

    @Autowired
    private LaboratorioService service;

    @GetMapping
    public List<Laboratorio> listarActivos() {
        return service.listarActivos();
    }

    @PostMapping
    public Laboratorio crear(@RequestBody Laboratorio laboratorio) {
        laboratorio.setActivo(true);
        return service.guardar(laboratorio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Laboratorio> actualizar(@PathVariable Long id, @RequestBody Laboratorio datosNuevos) {
        Laboratorio existente = service.buscarPorId(id);
        if (existente == null) return ResponseEntity.notFound().build();

        existente.setNombre(datosNuevos.getNombre());
        existente.setTelefono(datosNuevos.getTelefono());
        existente.setDireccion(datosNuevos.getDireccion());
        existente.setPersonaContacto(datosNuevos.getPersonaContacto());
        existente.setDiasPromedioEntrega(datosNuevos.getDiasPromedioEntrega());

        if (existente.getCatalogoPrecios() != null) {
            existente.getCatalogoPrecios().clear();
            if (datosNuevos.getCatalogoPrecios() != null) {
                existente.getCatalogoPrecios().addAll(datosNuevos.getCatalogoPrecios());
            }
        } else {
            existente.setCatalogoPrecios(datosNuevos.getCatalogoPrecios());
        }

        return ResponseEntity.ok(service.guardar(existente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.darDeBaja(id);
        return ResponseEntity.noContent().build();
    }
}