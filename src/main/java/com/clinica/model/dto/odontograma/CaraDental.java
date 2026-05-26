package com.clinica.model.dto.odontograma;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaraDental {
    private String oclusal;    // Arriba/Masticatoria
    private String vestibular; // Frente/Labios
    private String palatino;   // Atrás/Paladar-Lengua (Lingual)
    private String mesial;     // Lado derecho del diente
    private String distal;     // Lado izquierdo del diente
}