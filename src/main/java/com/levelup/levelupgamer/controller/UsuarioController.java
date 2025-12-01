package com.levelup.levelupgamer.controller;

import com.levelup.levelupgamer.entity.Usuario;
import com.levelup.levelupgamer.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Endpoints para gestión de usuarios (ADMIN)")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    @GetMapping
    @Operation(summary = "Listar todos los usuarios (ADMIN)")
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID (ADMIN)")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Integer id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/perfil")
    @Operation(summary = "Ver perfil propio (Autenticado)")
    public ResponseEntity<Usuario> verPerfil(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/perfil")
    @Operation(summary = "Actualizar perfil propio (Autenticado)")
    public ResponseEntity<Usuario> actualizarPerfil(
            @AuthenticationPrincipal Usuario usuario,
            @RequestBody Usuario datos) {
        
        // Solo actualizar campos permitidos
        usuario.setNombre(datos.getNombre());
        usuario.setApellido(datos.getApellido());
        usuario.setTelefono(datos.getTelefono());
        usuario.setDireccion(datos.getDireccion());
        usuario.setComuna(datos.getComuna());
        usuario.setRegion(datos.getRegion());
        usuario.setNewsletter(datos.getNewsletter());
        usuario.setNotificaciones(datos.getNotificaciones());
        usuario.setTema(datos.getTema());
        
        return ResponseEntity.ok(usuarioRepository.save(usuario));
    }
}
