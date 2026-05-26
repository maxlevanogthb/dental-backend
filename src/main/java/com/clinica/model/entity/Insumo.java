package com.clinica.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "insumos")
@Data
public class Insumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String codigo; 

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String categoria; 

    @Column(nullable = false)
    private String unidadMedida; 

    @Column(nullable = false)
    private Integer stockActual;

    @Column(nullable = false)
    private Integer stockMinimo; 

    private Double precioCompra; 

    private String ubicacion; 

    @Column(name = "fecha_ultima_actualizacion")
    private LocalDate fechaUltimaActualizacion;

    @Transient
    public boolean isBajoStock() {
        return this.stockActual != null && this.stockMinimo != null && this.stockActual <= this.stockMinimo;
    }
}