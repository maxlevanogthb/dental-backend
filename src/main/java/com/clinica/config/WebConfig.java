package com.clinica.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LicenciaInterceptor licenciaInterceptor;

    public WebConfig(LicenciaInterceptor licenciaInterceptor) {
        this.licenciaInterceptor = licenciaInterceptor;
    }

    /**
     * LÓGICA DE LICENCIA: 
     * Protege los endpoints de la API, excepto los de activación.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(licenciaInterceptor)
                .addPathPatterns("/api/**") // Bloquea todo bajo /api/
                .excludePathPatterns(
                    "/api/licencia/activar", 
                    "/api/licencia/estado"
                ); 
    }

    /**
     * LÓGICA DE ARCHIVOS: 
     * Mapea la URL /archivos/** a la carpeta física en el Home del usuario.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String userHome = System.getProperty("user.home");
        
        String uploadPath = Paths.get(userHome, "RieDentalData").toUri().toString();

        registry.addResourceHandler("/archivos/**")
                .addResourceLocations(uploadPath);
    }
}