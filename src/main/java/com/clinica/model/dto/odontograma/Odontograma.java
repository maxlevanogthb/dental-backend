package com.clinica.model.dto.odontograma;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Odontograma {
    
    // El mapa relacionará el número del diente (Nomenclatura FDI: "18", "21", "85") con su estado
    private Map<String, Diente> piezasAdulto;
    private Map<String, Diente> piezasInfantil;
    
    // Podemos agregar aquí los índices epidemiológicos que venían en tu PDF
    private String cpod; // Índice para adultos (Cariados, Perdidos, Obturados)
    private String ceod; // Índice para niños
    
    private String observacionesGenerales;

    private String forzarVista;
}