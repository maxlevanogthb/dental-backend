package com.clinica.service;

import com.clinica.model.entity.EspecialistaExterno;
import com.clinica.repository.EspecialistaExternoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EspecialistaExternoService {

    @Autowired
    private EspecialistaExternoRepository repository;
    
    public List<EspecialistaExterno> listarActivos() {
        return repository.findAll().stream()
                .filter(EspecialistaExterno::getActivo)
                .collect(Collectors.toList());
    }

    public EspecialistaExterno buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public EspecialistaExterno guardar(EspecialistaExterno especialista) {
        return repository.save(especialista);
    }

    public void darDeBaja(Long id) {
        EspecialistaExterno especialista = buscarPorId(id);
        if (especialista != null) {
            especialista.setActivo(false);
            repository.save(especialista);
        }
    }
}