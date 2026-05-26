package com.clinica.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "especialistas_externos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EspecialistaExterno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String especialidad; 
    
    private String telefono;
    private String correo;
    
    private String clinicaProcedencia;

    private String tipoEspecialista; 
    
    private Boolean activo = true; 
}