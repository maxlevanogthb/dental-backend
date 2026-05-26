package com.clinica.controller;

import com.clinica.model.entity.Insumo;
import com.clinica.repository.InsumoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/inventario/insumos")
@CrossOrigin(origins = "http://localhost:5173")
public class InsumoController {

    @Autowired
    private InsumoRepository insumoRepository;

    @GetMapping
    public List<Insumo> obtenerTodos() {
        return insumoRepository.findAll();
    }

    @GetMapping("/alertas")
    public List<Insumo> obtenerAlertasStock() {
        return insumoRepository.findInsumosBajoStock();
    }

    @PostMapping
    public ResponseEntity<Insumo> crear(@RequestBody Insumo insumo) {
        insumo.setFechaUltimaActualizacion(LocalDate.now());
        if (insumo.getStockActual() == null) insumo.setStockActual(0);
        return ResponseEntity.ok(insumoRepository.save(insumo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Insumo> actualizar(@PathVariable Long id, @RequestBody Insumo datosActualizados) {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insumo no encontrado"));

        insumo.setCodigo(datosActualizados.getCodigo());
        insumo.setNombre(datosActualizados.getNombre());
        insumo.setCategoria(datosActualizados.getCategoria());
        insumo.setUnidadMedida(datosActualizados.getUnidadMedida());
        insumo.setStockActual(datosActualizados.getStockActual());
        insumo.setStockMinimo(datosActualizados.getStockMinimo());
        insumo.setPrecioCompra(datosActualizados.getPrecioCompra());
        insumo.setUbicacion(datosActualizados.getUbicacion());
        insumo.setFechaUltimaActualizacion(LocalDate.now());

        return ResponseEntity.ok(insumoRepository.save(insumo));
    }

    // 5. Eliminar un producto del catálogo
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        insumoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}