package com.clinica.service;

import com.clinica.model.entity.Paciente;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PacienteService {
    
    List<Paciente> listarTodos();
    Paciente buscarPorId(Long id);
    Paciente guardar(Paciente paciente);
    void eliminar(Long id);

    Page<Paciente> obtenerPaginadosPorTipo(String tipoRegistro, int page, int size, String sortBy, boolean asc);
}