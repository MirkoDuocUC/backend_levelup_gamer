package com.levelup.levelupgamer.controller;

import com.levelup.levelupgamer.dto.CrearOrdenRequest;
import com.levelup.levelupgamer.entity.Orden;
import com.levelup.levelupgamer.service.OrdenService;
import com.levelup.levelupgamer.util.EstadoOrden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
@RequiredArgsConstructor
@Tag(name = "Órdenes", description = "Endpoints para gestión de órdenes de compra")
public class OrdenController {

    private final OrdenService ordenService;

    @PostMapping("/crear")
    @Operation(summary = "Crear orden desde el carrito activo")
    public ResponseEntity<Orden> crearOrden(
            Authentication authentication,
            @Valid @RequestBody CrearOrdenRequest request) {
        return ResponseEntity.ok(ordenService.crearOrden(authentication.getName(), request));
    }

    @GetMapping("/mis-ordenes")
    @Operation(summary = "Ver mis órdenes")
    public ResponseEntity<List<Orden>> misOrdenes(Authentication authentication) {
        return ResponseEntity.ok(ordenService.obtenerMisOrdenes(authentication.getName()));
    }

    @GetMapping
    @Operation(summary = "Ver todas las órdenes (ADMIN)")
    public ResponseEntity<List<Orden>> todasLasOrdenes() {
        return ResponseEntity.ok(ordenService.obtenerTodasLasOrdenes());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener orden por ID")
    public ResponseEntity<Orden> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(ordenService.obtenerOrdenPorId(id));
    }

    @PutMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado de orden (ADMIN)")
    public ResponseEntity<Orden> actualizarEstado(
            @PathVariable Integer id,
            @RequestParam EstadoOrden estado) {
        return ResponseEntity.ok(ordenService.actualizarEstado(id, estado));
    }
}
