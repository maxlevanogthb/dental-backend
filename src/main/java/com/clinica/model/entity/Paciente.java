package com.clinica.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Entidad Paciente optimizada para Ríe Dental.
 * Las nuevas secciones (Cabeza/Cuello y Examen Bucal) se integran
 * dentro del mapa 'exploracionFisica' para mantener la escalabilidad.
 */
@Entity
@Table(name = "pacientes")
@Data
@AllArgsConstructor
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Paciente() {
    }

    public Paciente(Long id) {
        this.id = id;
    }
    // --- SECCIÓN: CONTROL DE REGISTRO ---

    @Column(updatable = false)
    private String estomatologo; // Se llenará con el usuario en sesión

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime fechaRegistro; // Se genera automáticamente al insertar

    // --- SECCIÓN: DATOS PERSONALES (Relacionales para búsquedas rápidas) ---

    @Column(nullable = false)
    private String nombre;

    private Integer edad;
    private String sexo;
    private String domicilio;
    private String telefono;
    private String celular;
    private String otroTelefono;
    private String estadoCivil;
    private String correo;
    private String ocupacion;
    private LocalDate fechaNacimiento;
    private String rfc;
    private String curp;
    private String nss; 
    private String urlIneFrontal; 
    private String urlIneReverso;

    //@Column(nullable = false)
    private String tipoRegistro = "PACIENTE";

    @Column(columnDefinition = "TEXT")
    private String motivoConsulta;

    // --- SECCIÓN: INTELIGENCIA CLÍNICA (JSONB) ---

    /**
     * Contendrá:
     * - responsable: { nombre, parentesco, etc }
     * - generales: { grupoSanguineo, alergias, transfusiones, etc }
     * - patologicos: { diabetes: true, asma: false, otros: "" }
     * - habitos: { higiene, deporte, etc }
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> antecedentesMedicos;

    /**
     * Contendrá las nuevas secciones:
     * - signosVitales: { ta, fc, fr, temp, peso, etc }
     * - cabezaCuello: { craneo, cara, cuello, atm: {...}, medidas: {...} }
     * - examenBucal: { piel, labios, lengua, paladar, etc }
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> exploracionFisica;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> odontograma;

   @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonIgnore 
    private List<Visita> historialVisitas;

    @Column(columnDefinition = "TEXT")
    private String galeriaMultimedia;

    @Column(columnDefinition = "TEXT")
    private String documentosLegales;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonIgnore // 🔥 Nuestro escudo anti-bucles
    private List<Cita> historialCitas;

    @Transient
    @com.fasterxml.jackson.annotation.JsonProperty("ultimaVisita")
    public String getUltimaVisita() {
        if (this.historialVisitas == null || this.historialVisitas.isEmpty()) {
            return "Nuevo Ingreso";
        }
        // Busca la fecha más grande (la más reciente)
        return this.historialVisitas.stream()
                .filter(v -> v.getFecha() != null)
                .map(v -> v.getFecha().toString())
                .max(String::compareTo)
                .orElse("Nuevo Ingreso");
    }

    @com.fasterxml.jackson.annotation.JsonIgnore 
    public List<Visita> getVisitas() {
        return historialVisitas;
    }

    public void setVisitas(List<Visita> visitas) {
        this.historialVisitas = visitas;
    }
}