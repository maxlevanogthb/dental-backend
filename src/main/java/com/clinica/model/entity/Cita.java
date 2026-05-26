package com.clinica.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "citas")
@Data
public class Cita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    private LocalDateTime fechaHora;
    
    private String motivo;

    private String estado = "PENDIENTE";

    @Column(columnDefinition = "TEXT")
    private String notas;

    private Boolean recordatorioEnviado = false;
}