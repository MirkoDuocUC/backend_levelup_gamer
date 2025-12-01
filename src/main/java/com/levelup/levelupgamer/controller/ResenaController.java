package com.levelup.levelupgamer.controller;

import com.levelup.levelupgamer.dto.CrearResenaRequest;
import com.levelup.levelupgamer.entity.Resena;
import com.levelup.levelupgamer.service.ResenaService;
import com.levelup.levelupgamer.util.EstadoResena;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resenas")
@RequiredArgsConstructor
@Tag(name = "Reseñas", description = "Endpoints para gestión de reseñas de productos")
public class ResenaController {

    private final ResenaService resenaService;

    @PostMapping("/crear")
    @Operation(summary = "Crear reseña de producto")
    public ResponseEntity<Resena> crearResena(
            Authentication authentication,
            @Valid @RequestBody CrearResenaRequest request) {
        return ResponseEntity.ok(resenaService.crearResena(authentication.getName(), request));
    }

    @GetMapping("/producto/{productoId}")
    @Operation(summary = "Ver reseñas aprobadas de un producto")
    public ResponseEntity<List<Resena>> verResenasProducto(@PathVariable Integer productoId) {
        return ResponseEntity.ok(resenaService.obtenerResenasProducto(productoId));
    }

    @GetMapping("/pendientes")
    @Operation(summary = "Ver reseñas pendientes de moderación (ADMIN)")
    public ResponseEntity<List<Resena>> verPendientes() {
        return ResponseEntity.ok(resenaService.obtenerPendientes());
    }

    @PutMapping("/moderar/{id}")
    @Operation(summary = "Aprobar o rechazar reseña (ADMIN)")
    public ResponseEntity<Resena> moderarResena(
            @PathVariable Integer id,
            @RequestParam EstadoResena estado) {
        return ResponseEntity.ok(resenaService.moderarResena(id, estado));
    }
}
