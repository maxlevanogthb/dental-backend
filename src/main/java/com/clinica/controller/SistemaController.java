package com.clinica.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.InetAddress;
import java.util.Map;

@RestController
@RequestMapping("/api/sistema")
public class SistemaController {

    @GetMapping("/red")
    public ResponseEntity<Map<String, String>> obtenerIpLocal() {
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            return ResponseEntity.ok(Map.of(
                "ip", ip,
                "url", "http://" + ip + ":8080"
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                "ip", "Desconocida",
                "url", "http://localhost:8080"
            ));
        }
    }
}