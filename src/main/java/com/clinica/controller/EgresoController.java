package com.clinica.controller;

import com.clinica.model.entity.Egreso;
import com.clinica.repository.EgresoRepository;
import com.clinica.repository.VisitaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/finanzas/egresos")
@CrossOrigin(origins = "http://localhost:5173")
public class EgresoController {

    @Autowired
    private EgresoRepository egresoRepository;

    @Autowired
    private VisitaRepository visitaRepository;

    @GetMapping
    public List<Egreso> obtenerEgresos(
            @RequestParam(required = false) LocalDate inicio,
            @RequestParam(required = false) LocalDate fin) {
        
        if (inicio == null || fin == null) {
            YearMonth mesActual = YearMonth.now();
            inicio = mesActual.atDay(1);
            fin = mesActual.atEndOfMonth();
        }
        return egresoRepository.findByFechaBetweenOrderByFechaDesc(inicio, fin);
    }
    @PostMapping
    public ResponseEntity<Egreso> crear(@RequestBody Egreso egreso) {
        if (egreso.getFecha() == null) {
            egreso.setFecha(LocalDate.now()); 
        }
        Egreso guardado = egresoRepository.save(egreso);
        return ResponseEntity.ok(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Egreso> actualizar(@PathVariable Long id, @RequestBody Egreso egresoActualizado) {
        Egreso egreso = egresoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gasto no encontrado"));

        egreso.setConcepto(egresoActualizado.getConcepto());
        egreso.setMonto(egresoActualizado.getMonto());
        egreso.setFecha(egresoActualizado.getFecha());
        egreso.setCategoria(egresoActualizado.getCategoria());
        egreso.setMetodoPago(egresoActualizado.getMetodoPago());
        egreso.setNotas(egresoActualizado.getNotas());

        return ResponseEntity.ok(egresoRepository.save(egreso));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        egresoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/ingresos")
    public ResponseEntity<List<java.util.Map<String, Object>>> obtenerIngresosDetalle(
            @RequestParam(required = false) LocalDate inicio,
            @RequestParam(required = false) LocalDate fin) {

        if (inicio == null || fin == null) {
            YearMonth mesActual = YearMonth.now();
            inicio = mesActual.atDay(1);
            fin = mesActual.atEndOfMonth();
        }
        final LocalDate finalInicio = inicio;
        final LocalDate finalFin = fin;

        List<java.util.Map<String, Object>> ingresos = visitaRepository.findAll().stream()
                .filter(v -> v.getFecha() != null && 
                            !v.getFecha().isBefore(finalInicio) && 
                            !v.getFecha().isAfter(finalFin) && 
                            v.getTotalAbonado() != null && 
                            Double.parseDouble(v.getTotalAbonado().toString()) > 0)
                .map(v -> {
                    java.util.Map<String, Object> map = new java.util.HashMap<>();
                    map.put("id", v.getId());
                    map.put("fecha", v.getFecha());
                    map.put("paciente", v.getPaciente() != null ? v.getPaciente().getNombre() : "Sin Paciente");
                    map.put("motivo", v.getMotivo());
                    map.put("monto", v.getTotalAbonado());
                    return map;
                })
                .collect(java.util.stream.Collectors.toList());

        return ResponseEntity.ok(ingresos);
    }
}