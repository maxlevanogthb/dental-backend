package com.clinica.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "leyendas_odontograma")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeyendaOdontograma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre; 
    
    @Column(nullable = false)
    private String colorTailwind; 


    @Column(nullable = false)
    private boolean activo = true;
}