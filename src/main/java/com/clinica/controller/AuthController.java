package com.clinica.controller;

import com.clinica.model.entity.Usuario;
import com.clinica.repository.UsuarioRepository;
import com.clinica.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");

            //Validamos las credenciales
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            //Buscamos al usuario en la BD para sacar sus datos reales
            Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow();

            //Generamos el Gafete (Token)
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtil.generateToken(userDetails, usuario.getRol().name(), usuario.getId());

            //Empaquetamos la respuesta para Vue
            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("username", usuario.getUsername());
            response.put("nombre", usuario.getNombre());
            response.put("rol", usuario.getRol().name());
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Credenciales incorrectas o usuario inactivo");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }
}