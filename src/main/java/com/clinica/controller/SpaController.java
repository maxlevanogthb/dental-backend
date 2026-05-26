package com.clinica.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpaController {

    /**
     * Mapeamos EXPLÍCITAMENTE las rutas que existen en Vue Router.
     * Si el usuario recarga la página (F5) en cualquiera de estas rutas,
     * Spring Boot le entregará el index.html sin interferir con la API.
     */
    @RequestMapping(value = {
        "/login",
        "/paciente/**",
        "/configuracion",
        "/laboratorios",
        "/citas",
        "/dashboard",
        "/finanzas",
        "/inventario",
        "/activacion"
    })
    public String forwardToVue() {
        return "forward:/index.html";
    }
}