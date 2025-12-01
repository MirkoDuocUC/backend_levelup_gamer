package com.levelup.levelupgamer.service;

import com.levelup.levelupgamer.dto.AuthRequest;
import com.levelup.levelupgamer.dto.AuthResponse;
import com.levelup.levelupgamer.dto.RegistroRequest;
import com.levelup.levelupgamer.entity.Usuario;
import com.levelup.levelupgamer.repository.ComunaRepository;
import com.levelup.levelupgamer.repository.RegionRepository;
import com.levelup.levelupgamer.repository.UsuarioRepository;
import com.levelup.levelupgamer.security.JwtUtil;
import com.levelup.levelupgamer.util.EstadoUsuario;
import com.levelup.levelupgamer.util.RolUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final ComunaRepository comunaRepository;
    private final RegionRepository regionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse login(AuthRequest request) {
        // Autenticar con Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Obtener usuario
        Usuario usuario = (Usuario) authentication.getPrincipal();

        // Generar token JWT
        String token = jwtUtil.generarToken(usuario.getEmail(), usuario.getRol().name());

        return AuthResponse.builder()
                .token(token)
                .tipo("Bearer")
                .email(usuario.getEmail())
                .rol(usuario.getRol().name())
                .nombre(usuario.getNombre() + " " + usuario.getApellido())
                .id(usuario.getId())
                .mensaje("Login exitoso")
                .build();
    }

    @Transactional
    public AuthResponse registro(RegistroRequest request) {
        // Validar email único
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // Validar edad mínima (18 años)
        int edad = Period.between(request.getFechaNacimiento(), LocalDate.now()).getYears();
        if (edad < 18) {
            throw new IllegalArgumentException("Debes tener al menos 18 años para registrarte");
        }

        // Determinar descuento DUOC
        BigDecimal descuentoDuoc = request.getEmail().toLowerCase().contains("@duocuc.cl")
                ? BigDecimal.valueOf(20.00)
                : BigDecimal.ZERO;

        // Crear usuario
        Usuario usuario = Usuario.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .telefono(request.getTelefono())
                .fechaNacimiento(request.getFechaNacimiento())
                .edad(edad)
                .direccion(request.getDireccion())
                .rol(RolUsuario.cliente)
                .estado(EstadoUsuario.activo)
                .descuentoDuoc(descuentoDuoc)
                .newsletter(request.getNewsletter())
                .emailVerificado(true) // Por simplificación, en producción enviar email
                .build();

        // Asignar comuna y región si existen
        if (request.getComunaId() != null) {
            comunaRepository.findById(request.getComunaId())
                    .ifPresent(usuario::setComuna);
        }
        if (request.getRegionId() != null) {
            regionRepository.findById(request.getRegionId())
                    .ifPresent(usuario::setRegion);
        }

        usuarioRepository.save(usuario);

        // Generar token JWT para auto-login
        String token = jwtUtil.generarToken(usuario.getEmail(), usuario.getRol().name());

        return AuthResponse.builder()
                .token(token)
                .tipo("Bearer")
                .email(usuario.getEmail())
                .rol(usuario.getRol().name())
                .nombre(usuario.getNombre() + " " + usuario.getApellido())
                .id(usuario.getId())
                .mensaje(descuentoDuoc.compareTo(BigDecimal.ZERO) > 0
                        ? "Registro exitoso. ¡Tienes 20% de descuento DUOC!"
                        : "Registro exitoso")
                .build();
    }
}
