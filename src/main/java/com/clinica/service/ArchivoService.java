package com.clinica.service; 

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ArchivoService {

    public String guardarArchivoLocal(MultipartFile archivo) throws IOException {
        String userHome = System.getProperty("user.home");
        Path directorioDestino = Paths.get(userHome, "RieDentalData");
        
        if (!Files.exists(directorioDestino)) {
            Files.createDirectories(directorioDestino);
        }

        String nombreUnico = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename().replaceAll("\\s+", "_");
        Path rutaFinal = directorioDestino.resolve(nombreUnico);
        
        Files.copy(archivo.getInputStream(), rutaFinal);

        return "/archivos/" + nombreUnico;
    }
}