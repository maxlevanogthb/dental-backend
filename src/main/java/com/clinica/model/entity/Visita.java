package com.clinica.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "visitas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Visita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private String motivo;

    private String estado;
    
    private Long doctorAtiendeId;

    // --- Módulo de Notas de Evolución ---
    @Column(columnDefinition = "TEXT")
    private String notasEvolucion;

    // --- Módulo de Presupuesto / Plan de Tratamiento ---
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<Map<String, Object>> tratamientosRealizados;

    // --- Módulo Financiero (Pagos de la visita) ---
    @Column(precision = 10, scale = 2)
    private BigDecimal totalCobrar;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalAbonado;

    // --- Relación con el Paciente ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Paciente paciente;

    @Column(columnDefinition = "TEXT")
    private String historialPagos;

    @Column(columnDefinition = "TEXT")
    private String galeriaMultimedia;

    @Column(columnDefinition = "TEXT")
    private String documentosLegales;

    @OneToMany(mappedBy = "visita", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrabajoLaboratorio> ordenesLaboratorio;
    

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
}