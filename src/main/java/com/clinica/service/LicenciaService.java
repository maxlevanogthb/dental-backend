package com.clinica.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File; // Importación necesaria para File
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths; // Importación necesaria para Paths
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Map;

@Service
public class LicenciaService {

    @Value("classpath:public_key.pem")
    private Resource publicKeyResource;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String PATH_LICENCIA = "./.rie_license";

    public boolean sistemaActivado() {
        try {
            File archivo = new File(PATH_LICENCIA);
            if (!archivo.exists()) return false;
            String token = new String(Files.readAllBytes(Paths.get(PATH_LICENCIA))).trim();
            return validarLicencia(token);
        } catch (Exception e) {
            return false;
        }
    }

    public void guardarLicencia(String token) throws Exception {
        Files.write(Paths.get(PATH_LICENCIA), token.getBytes());
    }

    @SuppressWarnings("unchecked") 
    public boolean validarLicencia(String token) {
    try {
        if (token == null || !token.contains(".")) {
            System.out.println("❌ Formato de token incorrecto (falta el punto)");
            return false;
        }

        String[] parts = token.split("\\.");
        byte[] bodyBytes = Base64.getDecoder().decode(parts[0]);
        byte[] signatureBytes = Base64.getDecoder().decode(parts[1]);

        PublicKey publicKey = leerPublicKey();

        // SHA256withRSA/PSS es sensible a la longitud del "salt"
        Signature sig = Signature.getInstance("SHA256withRSA"); 
        sig.initVerify(publicKey);
        sig.update(bodyBytes);

        if (!sig.verify(signatureBytes)) {
            System.out.println("❌ Firma no coincide. ¿Usaste la llave correcta?");
            return false;
        }

        Map<String, Object> payload = objectMapper.readValue(bodyBytes, Map.class);
        String fechaStr = (String) payload.get("expira");
        LocalDate fechaExpira = LocalDate.parse(fechaStr);
        
        System.out.println("🔎 Validando clinica: " + payload.get("clinica"));
        System.out.println("🔎 Fecha expira: " + fechaExpira);
        
        if (LocalDate.now().isAfter(fechaExpira)) {
            System.out.println("❌ Error: La licencia expiro el " + fechaExpira);
            return false;
        }

        return true;
    } catch (Exception e) {
        System.out.println("❌ Error critico en validacion: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}

    private PublicKey leerPublicKey() throws Exception {
        // Obtenemos el path del recurso de forma segura
        byte[] keyBytes;
        try (var inputStream = publicKeyResource.getInputStream()) {
            String key = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8)
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");
            keyBytes = Base64.getDecoder().decode(key);
        }

        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }
}