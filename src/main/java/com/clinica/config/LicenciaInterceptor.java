package com.clinica.config;

import com.clinica.service.LicenciaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LicenciaInterceptor implements HandlerInterceptor {

    private final LicenciaService licenciaService;

    public LicenciaInterceptor(LicenciaService licenciaService) {
        this.licenciaService = licenciaService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Si el sistema NO está activado, bloqueamos y mandamos error 402
        if (!licenciaService.sistemaActivado()) {
            response.setStatus(402); // Código estándar para "Pago/Licencia Requerida"
            response.getWriter().write("SISTEMA_BLOQUEADO: Se requiere activacion de licencia.");
            return false;
        }
        return true; // Sistema activado, adelante
    }
}