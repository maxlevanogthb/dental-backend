package com.clinica.controller;

import com.clinica.model.entity.Paciente;
import com.clinica.repository.PacienteRepository;
import com.clinica.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Controlador REST encargado de exponer los endpoints para la gestión de pacientes.
 */
@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "http://localhost:5173")
public class PacienteController {

    private final PacienteService pacienteService;
    private final PacienteRepository pacienteRepository;

    @Autowired
    public PacienteController(PacienteService pacienteService, PacienteRepository pacienteRepository) {
        this.pacienteService = pacienteService;
        this.pacienteRepository = pacienteRepository;
    }

    @GetMapping
    public List<Paciente> listar() {
        return pacienteService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paciente> obtenerPorId(@PathVariable Long id) {
        Paciente paciente = pacienteService.buscarPorId(id);
        return paciente != null ? ResponseEntity.ok(paciente) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Paciente crear(@RequestBody Paciente paciente) {
        paciente.setEstomatologo("Dra. Ana Cristina Escamilla Chavez"); 
        return pacienteService.guardar(paciente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Paciente> actualizar(@PathVariable Long id, @RequestBody Paciente pacienteActualizado) {
        Paciente pacienteExistente = pacienteService.buscarPorId(id);
        if (pacienteExistente == null) {
            return ResponseEntity.notFound().build();
        }
        
        if (pacienteActualizado.getVisitas() != null) {
            pacienteActualizado.getVisitas().forEach(visita -> {
                visita.setPaciente(pacienteActualizado);
            });
        }
    
        pacienteActualizado.setId(id);
        return ResponseEntity.ok(pacienteService.guardar(pacienteActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (pacienteService.buscarPorId(id) == null) {
            return ResponseEntity.notFound().build();
        }
        pacienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Paciente>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(pacienteRepository.findByNombreContainingIgnoreCase(nombre));
    }

    @PatchMapping("/{id}/convertir")
    public ResponseEntity<Paciente> convertirAPaciente(@PathVariable Long id) {
        Paciente p = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente/Lead no encontrado"));
        
        p.setTipoRegistro("PACIENTE");
        return ResponseEntity.ok(pacienteRepository.save(p));
    }

    @GetMapping("/paginados")
    public ResponseEntity<Page<Paciente>> obtenerPaginados(
            @RequestParam String tipoRegistro,
            @RequestParam(defaultValue = "") String buscar, // ¡Nuevo parámetro opcional!
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sort[1]), sort[0]));
        
        Page<Paciente> pacientes;
        if (buscar.trim().isEmpty()) {
            pacientes = pacienteRepository.findByTipoRegistro(tipoRegistro, pageable);
        } else {
            pacientes = pacienteRepository.buscarEnDirectorio(tipoRegistro, buscar, pageable);
        }
        
        return ResponseEntity.ok(pacientes);
    }
}