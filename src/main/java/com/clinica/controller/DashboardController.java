package com.clinica.controller;

import com.clinica.model.entity.EspecialistaExterno;
import com.clinica.model.entity.TrabajoLaboratorio;
import com.clinica.model.entity.Visita;
import com.clinica.repository.EspecialistaExternoRepository;
import com.clinica.repository.PacienteRepository;
import com.clinica.repository.TrabajoLaboratorioRepository;
import com.clinica.repository.VisitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinica.model.entity.Egreso;
import com.clinica.repository.EgresoRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private VisitaRepository visitaRepository;
    @Autowired
    private TrabajoLaboratorioRepository trabajoLaboratorioRepository;
    @Autowired
    private EspecialistaExternoRepository especialistaExternoRepository;
    @Autowired
    private EgresoRepository egresoRepository;

    @GetMapping("/resumen")
    public ResponseEntity<Map<String, Object>> obtenerResumenDashboard() {
        Map<String, Object> respuesta = new HashMap<>();

        long totalPacientes = pacienteRepository.count();
        List<Visita> todasVisitas = visitaRepository.findAll();
        List<TrabajoLaboratorio> todosLabs = trabajoLaboratorioRepository.findAll();
        List<Egreso> todosEgresos = egresoRepository.findAll(); // 🔥 Traemos los egresos

        LocalDate inicioMesActual = LocalDate.now().withDayOfMonth(1);
        
        BigDecimal ingresosDelMes = BigDecimal.ZERO;
        BigDecimal egresosDelMes = BigDecimal.ZERO; // 🔥 Variable para egresos
        
        int tratamientosRealizadosTotales = 0;

        Map<String, Integer> conteoTratamientos = new HashMap<>();
        Map<String, BigDecimal> ingresosPorDoctor = new HashMap<>();
        Map<String, BigDecimal> egresosPorCategoria = new HashMap<>(); // 🔥 Para la nueva gráfica

        Map<Long, String> cacheEspecialistas = new HashMap<>();
        for (EspecialistaExterno e : especialistaExternoRepository.findAll()) {
            cacheEspecialistas.put(e.getId(), e.getNombre());
        }

        // Procesar Visitas (Ingresos)
        for (Visita v : todasVisitas) {
            if (v.getFecha() != null && !v.getFecha().isBefore(inicioMesActual)) {
                if (v.getTotalAbonado() != null) {
                    ingresosDelMes = ingresosDelMes.add(v.getTotalAbonado());
                }
            }
            if (v.getTratamientosRealizados() != null) {
                for (Map<String, Object> tratamiento : v.getTratamientosRealizados()) {
                    tratamientosRealizadosTotales++;
                    String nombreTx = (String) tratamiento.get("tratamientoNombre");
                    if (nombreTx != null) {
                        conteoTratamientos.put(nombreTx, conteoTratamientos.getOrDefault(nombreTx, 0) + 1);
                    }
                    BigDecimal precioTx = BigDecimal.ZERO;
                    if (tratamiento.get("precio") != null) {
                        precioTx = new BigDecimal(tratamiento.get("precio").toString());
                    }
                    String nombreDoctor = "Dr. Interno";
                    if (tratamiento.get("especialistaId") != null) {
                        Long espId = Long.valueOf(tratamiento.get("especialistaId").toString());
                        nombreDoctor = cacheEspecialistas.getOrDefault(espId, "Dr. Externo");
                    }
                    ingresosPorDoctor.put(nombreDoctor, ingresosPorDoctor.getOrDefault(nombreDoctor, BigDecimal.ZERO).add(precioTx));
                }
            }
        }

        // 🔥 Procesar Egresos
        for (Egreso e : todosEgresos) {
            if (e.getFecha() != null && !e.getFecha().isBefore(inicioMesActual)) {
                egresosDelMes = egresosDelMes.add(e.getMonto());
                egresosPorCategoria.put(e.getCategoria(), egresosPorCategoria.getOrDefault(e.getCategoria(), BigDecimal.ZERO).add(e.getMonto()));
            }
        }

        long pendientesLab = todosLabs.stream()
                .filter(l -> l.getEstado() == null || !l.getEstado().equalsIgnoreCase("ENTREGADO"))
                .count();

        // ==========================================
        // ARMADO DEL JSON FINAL PARA VUE
        // ==========================================

        //KPIs actualizados con Egresos en lugar de Tratamientos
        List<Map<String, Object>> kpis = Arrays.asList(
                crearKpi(1, "INGRESOS DEL MES", "$" + String.format("%,.2f", ingresosDelMes), "", "Mes actual", true, "bg-emerald-100", "text-emerald-600", "💰", false),
                crearKpi(2, "EGRESOS DEL MES", "-$" + String.format("%,.2f", egresosDelMes), "", "Salidas registradas", false, "bg-red-100", "text-red-500", "📉", false),
                crearKpi(3, "PACIENTES TOTALES", String.valueOf(totalPacientes), "", "Registrados en BD", true, "bg-blue-100", "text-blue-600", "👥", false),
                crearKpi(4, "PENDIENTES DE LAB.", String.valueOf(pendientesLab), "", pendientesLab > 0 ? "Requiere atención" : "Al día", false, "bg-slate-100", "text-slate-500", "🔬", pendientesLab > 0)
        );
        respuesta.put("kpis", kpis);

        // Gráfica de Barras (Doctores)
        List<Map.Entry<String, BigDecimal>> topDoctores = ingresosPorDoctor.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(4).collect(Collectors.toList());
        List<String> labelsBarras = new ArrayList<>();
        List<BigDecimal> dataBarras = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> entry : topDoctores) {
            labelsBarras.add(entry.getKey().length() > 15 ? entry.getKey().substring(0, 15) + "..." : entry.getKey());
            dataBarras.add(entry.getValue());
        }
        Map<String, Object> barras = new HashMap<>();
        barras.put("labels", labelsBarras);
        barras.put("data", dataBarras);
        respuesta.put("graficaBarras", barras);

        //Gráfica Dona 1 (Tratamientos)
        List<Map.Entry<String, Integer>> topTratamientos = conteoTratamientos.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(4).collect(Collectors.toList());
        int totalTopTx = topTratamientos.stream().mapToInt(Map.Entry::getValue).sum();
        String[] coloresDona = { "#10b981", "#3b82f6", "#f59e0b", "#ec4899" };
        List<Map<String, Object>> dona = new ArrayList<>();
        int colorIndex = 0;
        for (Map.Entry<String, Integer> entry : topTratamientos) {
            int porcentaje = totalTopTx > 0 ? (int) Math.round((entry.getValue() * 100.0) / totalTopTx) : 0;
            dona.add(crearItemDona(entry.getKey(), porcentaje, coloresDona[colorIndex % coloresDona.length]));
            colorIndex++;
        }
        if (dona.isEmpty()) dona.add(crearItemDona("Sin tratamientos", 100, "#e2e8f0"));
        respuesta.put("graficaDona", dona);

        //Gráfica Dona 2 (Egresos)
        List<Map.Entry<String, BigDecimal>> topEgresos = egresosPorCategoria.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .collect(Collectors.toList());
        BigDecimal totalGastosTop = topEgresos.stream().map(Map.Entry::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
        String[] coloresEgresos = { "#ef4444", "#f97316", "#8b5cf6", "#64748b" };
        List<Map<String, Object>> donaEgresos = new ArrayList<>();
        int colorIndexEgreso = 0;
        for (Map.Entry<String, BigDecimal> entry : topEgresos) {
            int porcentaje = totalGastosTop.compareTo(BigDecimal.ZERO) > 0 ? 
                    entry.getValue().multiply(new BigDecimal(100)).divide(totalGastosTop, java.math.RoundingMode.HALF_UP).intValue() : 0;
            donaEgresos.add(crearItemDona(entry.getKey(), porcentaje, coloresEgresos[colorIndexEgreso % coloresEgresos.length]));
            colorIndexEgreso++;
        }
        if (donaEgresos.isEmpty()) donaEgresos.add(crearItemDona("Sin egresos", 100, "#e2e8f0"));
        respuesta.put("graficaEgresos", donaEgresos);

        // Actividad Reciente...
        List<Map<String, Object>> feedActividad = new ArrayList<>();
        pacienteRepository.findAll().stream()
                .sorted(Comparator.comparing(com.clinica.model.entity.Paciente::getId).reversed())
                .limit(3)
                .forEach(p -> {
                    String fechaStr = p.getFechaRegistro() != null ? p.getFechaRegistro().toLocalDate().toString() : "Recientemente";
                    feedActividad.add(crearItemActividad("success", "Nuevo paciente", p.getNombre() + " se unió a la clínica.", fechaStr, "bg-emerald-100", "text-emerald-600", "➕"));
                });
        visitaRepository.findAll().stream()
                .sorted(Comparator.comparing(Visita::getId).reversed())
                .limit(3)
                .forEach(v -> {
                    String fechaVisitaStr = v.getFecha() != null ? v.getFecha().toString() : "Recientemente";
                    String motivoStr = v.getMotivo() != null ? v.getMotivo() : "Atención General";
                    String abonoStr = v.getTotalAbonado() != null ? v.getTotalAbonado().toString() : "0.00";
                    feedActividad.add(crearItemActividad("info", "Visita finalizada", "Motivo: " + motivoStr + " - Abono: $" + abonoStr, fechaVisitaStr, "bg-blue-100", "text-blue-600", "✔️"));
                });
        respuesta.put("actividadReciente", feedActividad.stream().limit(5).collect(Collectors.toList()));
        respuesta.put("totalTratamientos", tratamientosRealizadosTotales);

        return ResponseEntity.ok(respuesta);
    }

    private Map<String, Object> crearItemActividad(String tipo, String titulo, String desc, String tiempo, String bg,
            String color, String icon) {
        Map<String, Object> act = new HashMap<>();
        act.put("tipo", tipo);
        act.put("titulo", titulo);
        act.put("desc", desc);
        act.put("tiempo", tiempo);
        act.put("bg", bg);
        act.put("color", color);
        act.put("icon", icon);
        return act;
    }

    private Map<String, Object> crearKpi(int id, String titulo, String valor, String tendencia, String textoTendencia,
            boolean positivo, String bg, String color, String icono, boolean alerta) {
        Map<String, Object> kpi = new HashMap<>();
        kpi.put("id", id);
        kpi.put("titulo", titulo);
        kpi.put("valor", valor);
        kpi.put("tendencia", tendencia);
        kpi.put("textoTendencia", textoTendencia);
        kpi.put("positivo", positivo);
        kpi.put("bgIcono", bg);
        kpi.put("colorIcono", color);
        kpi.put("icono", icono);
        kpi.put("alerta", alerta);
        return kpi;
    }

    private Map<String, Object> crearItemDona(String label, int percent, String color) {
        Map<String, Object> item = new HashMap<>();
        item.put("label", label);
        item.put("percent", percent);
        item.put("color", color);
        return item;
    }
}