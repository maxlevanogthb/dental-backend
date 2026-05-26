package com.clinica.model.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrecioTrabajo {
    
    private String nombreTrabajo;
    
    private BigDecimal costo; 
}