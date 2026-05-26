package com.clinica.controller; // <-- Ajusta tu paquete aquí

import com.clinica.service.ArchivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/archivos")
@CrossOrigin(origins = "http://localhost:5173") 
public class ArchivoController {

    @Autowired
    private ArchivoService archivoService;

    @PostMapping("/upload")
    public ResponseEntity<?> subirArchivo(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("El archivo está vacío.");
            }

            String urlArchivo = archivoService.guardarArchivoLocal(file);

            Map<String, String> response = new HashMap<>();
            response.put("url", urlArchivo);
            response.put("mensaje", "Archivo subido correctamente");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar el archivo: " + e.getMessage());
        }
    }
}