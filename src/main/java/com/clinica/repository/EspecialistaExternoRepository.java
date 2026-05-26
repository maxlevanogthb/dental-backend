// EspecialistaExternoRepository.java
package com.clinica.repository;
import com.clinica.model.entity.EspecialistaExterno;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspecialistaExternoRepository extends JpaRepository<EspecialistaExterno, Long> {

    Optional<EspecialistaExterno> findByCorreo(String correo);
}