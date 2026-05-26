package com.clinica.service;

import com.clinica.model.entity.Laboratorio;
import com.clinica.repository.LaboratorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LaboratorioService {

    @Autowired
    private LaboratorioRepository repository;

    public List<Laboratorio> listarActivos() {
        return repository.findAll().stream()
                .filter(Laboratorio::getActivo)
                .collect(Collectors.toList());
    }

    public Laboratorio buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Laboratorio guardar(Laboratorio laboratorio) {
        return repository.save(laboratorio);
    }
    
    public void darDeBaja(Long id) {
        Laboratorio laboratorio = buscarPorId(id);
        if (laboratorio != null) {
            laboratorio.setActivo(false);
            repository.save(laboratorio);
        }
    }
}