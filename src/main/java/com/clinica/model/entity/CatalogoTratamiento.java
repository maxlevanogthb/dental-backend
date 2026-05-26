package com.clinica.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "catalogo_tratamientos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatalogoTratamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre; 

    @Column(nullable = false)
    private String categoria; 

    @Column(precision = 10, scale = 2)
    private BigDecimal precioSugerido; 

    @Column(precision = 10, scale = 2)
    private BigDecimal precioEspecialista; 

    @Column(nullable = false)
    private Boolean activo = true; 
}