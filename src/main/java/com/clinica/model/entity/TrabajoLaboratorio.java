package com.clinica.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trabajos_laboratorio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrabajoLaboratorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- RELACIONES ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "laboratorio_id")
    private Laboratorio laboratorio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "visitas", "trabajosLaboratorio"})
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visita_id")
    @JsonIgnore 
    private Visita visita;

    @Transient
    private Long laboratorioId;

    // --- DESGLOSE DE TRABAJOS (PRESUPUESTO) ---
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "orden_laboratorio_detalles", joinColumns = @JoinColumn(name = "orden_id"))
    private List<DetalleOrdenLaboratorio> detalles = new ArrayList<>();
    
    @Column(columnDefinition = "TEXT")
    private String indicaciones;

    // --- CONTROL DE TIEMPOS Y COSTOS ---
    private LocalDate fechaEnvio;
    private LocalDate fechaEsperadaEntrega;
    private LocalDate fechaRecepcionReal;

    private BigDecimal costoLaboratorio; 
    private BigDecimal saldoPendienteLab;
    
    private String estado; 
}