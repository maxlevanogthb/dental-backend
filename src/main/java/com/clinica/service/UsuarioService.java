package com.clinica.service;

import com.clinica.model.entity.Usuario;
import com.clinica.model.entity.EspecialistaExterno;
import com.clinica.repository.UsuarioRepository;
import com.clinica.repository.EspecialistaExternoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EspecialistaExternoRepository especialistaExternoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario crear(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setActivo(true);
        
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        if (usuario.getRol() != null && usuario.getRol().name().equals("DOCTOR")) {
            EspecialistaExterno nuevoDoctor = new EspecialistaExterno();
            nuevoDoctor.setNombre(usuario.getNombre());
            nuevoDoctor.setEspecialidad(usuario.getEspecialidad() != null ? usuario.getEspecialidad() : "General");
            nuevoDoctor.setTelefono(usuario.getTelefono());
            nuevoDoctor.setCorreo(usuario.getUsername()); 
            nuevoDoctor.setTipoEspecialista("TRATANTE"); 
            nuevoDoctor.setActivo(true);
            
            especialistaExternoRepository.save(nuevoDoctor);
        }

        return usuarioGuardado;
    }

    public Usuario actualizar(Long id, Usuario datosActualizados) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String correoAnterior = usuarioExistente.getUsername();

        usuarioExistente.setNombre(datosActualizados.getNombre());
        usuarioExistente.setUsername(datosActualizados.getUsername());
        usuarioExistente.setRol(datosActualizados.getRol());

        if (datosActualizados.getPassword() != null && !datosActualizados.getPassword().trim().isEmpty()) {
            usuarioExistente.setPassword(passwordEncoder.encode(datosActualizados.getPassword()));
        }

        Usuario usuarioGuardado = usuarioRepository.save(usuarioExistente);

        if (usuarioGuardado.getRol().name().equals("DOCTOR")) {
            
            Optional<EspecialistaExterno> espOpt = especialistaExternoRepository.findByCorreo(correoAnterior);
            
            if (espOpt.isPresent()) {
                EspecialistaExterno esp = espOpt.get();
                esp.setNombre(datosActualizados.getNombre());
                esp.setCorreo(datosActualizados.getUsername());
                
                if (datosActualizados.getEspecialidad() != null && !datosActualizados.getEspecialidad().isEmpty()) {
                    esp.setEspecialidad(datosActualizados.getEspecialidad());
                }
                if (datosActualizados.getTelefono() != null && !datosActualizados.getTelefono().isEmpty()) {
                    esp.setTelefono(datosActualizados.getTelefono());
                }
                especialistaExternoRepository.save(esp);
            } else {
                EspecialistaExterno nuevoDoctor = new EspecialistaExterno();
                nuevoDoctor.setNombre(datosActualizados.getNombre());
                nuevoDoctor.setEspecialidad(datosActualizados.getEspecialidad() != null ? datosActualizados.getEspecialidad() : "General");
                nuevoDoctor.setTelefono(datosActualizados.getTelefono());
                nuevoDoctor.setCorreo(datosActualizados.getUsername());
                nuevoDoctor.setTipoEspecialista("TRATANTE");
                nuevoDoctor.setActivo(true);
                
                especialistaExternoRepository.save(nuevoDoctor);
            }
        }

        return usuarioGuardado;
    }

    public void cambiarEstado(Long id, boolean activo) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setActivo(activo);
        usuarioRepository.save(usuario);
    }
}