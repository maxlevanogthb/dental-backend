package com.clinica.service.impl;

import com.clinica.model.entity.Paciente;
import com.clinica.repository.PacienteRepository;
import com.clinica.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;



/**
 * Implementación de la lógica de negocio para la gestión de pacientes, 
 * encargada de intermediar entre el controlador y el repositorio.
 */
@Service
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;

    @Autowired
    public PacienteServiceImpl(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public Page<Paciente> obtenerPaginadosPorTipo(String tipoRegistro, int page, int size, String sortBy, boolean asc) {
        Sort sort = asc ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return pacienteRepository.findByTipoRegistro(tipoRegistro, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paciente> listarTodos() {
        return pacienteRepository.findAll();
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public Paciente guardar(Paciente paciente) {
        // La persistencia de los campos Map (JSONB) es gestionada automáticamente por Hibernate
        return pacienteRepository.save(paciente);
    }

    @Override
    @Transactional(readOnly = true)
    public Paciente buscarPorId(Long id) {
        return pacienteRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        pacienteRepository.deleteById(id);
    }
}