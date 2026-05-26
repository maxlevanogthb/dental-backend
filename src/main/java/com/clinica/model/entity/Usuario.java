package com.clinica.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Constructor vacío para que Jackson y JPA no se peleen
    public Usuario() {
    }

    @Column(nullable = false)
    private String nombre; 

    @Column(unique = true, nullable = false)
    private String username; 

    @Column(nullable = false)
    private String password; 

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol; 

    private boolean activo = true; 

    @Transient
    private String especialidad;

    @Transient
    private String telefono;
}