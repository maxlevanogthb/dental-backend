package com.clinica.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "egresos")
@Data
public class Egreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String concepto; 

    @Column(nullable = false)
    private BigDecimal monto;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private String categoria; 

    @Column(name = "metodo_pago")
    private String metodoPago; 

    @Column(columnDefinition = "TEXT")
    private String notas;

    @Column(name = "registrado_por")
    private String registradoPor; 
}