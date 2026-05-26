package com.clinica.model.dto.odontograma;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Diente {
    private CaraDental caras;
    private String estadoGlobal; // Ej: "Ausente", "Corona", "Implante" (Afecta todo el diente)
    private String notas;        // Notas específicas de esta pieza
}