package com.clinica.controller;

import com.clinica.service.LicenciaService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/licencia")
@CrossOrigin(origins = "http://localhost:5173")
public class LicenciaController {

    private final LicenciaService licenciaService;

    public LicenciaController(LicenciaService licenciaService) {
        this.licenciaService = licenciaService;
    }

    @GetMapping("/estado")
    public ResponseEntity<Map<String, Boolean>> verificarEstado() {
        return ResponseEntity.ok(Map.of("activado", licenciaService.sistemaActivado()));
    }

    @PostMapping(value = "/activar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> activarSistema(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        
        // Validar matemáticamente la firma
        if (licenciaService.validarLicencia(token)) {
            try {
                //  Si es real, guardarla en el archivo local .rie_license
                licenciaService.guardarLicencia(token);
                return ResponseEntity.ok("Sistema activado con exito.");
            } catch (Exception e) {
                return ResponseEntity.internalServerError().body("Error al guardar archivo de licencia.");
            }
        } else {
            return ResponseEntity.badRequest().body("Codigo de licencia invalido o expirado.");
        }
    }
}