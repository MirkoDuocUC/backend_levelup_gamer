package com.levelup.levelupgamer.controller;

import com.levelup.levelupgamer.dto.AgregarCarritoRequest;
import com.levelup.levelupgamer.entity.Carrito;
import com.levelup.levelupgamer.entity.CarritoItem;
import com.levelup.levelupgamer.service.CarritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
@Tag(name = "Carrito de Compras", description = "Endpoints para gestionar el carrito")
public class CarritoController {

    private final CarritoService carritoService;

    @GetMapping
    @Operation(summary = "Obtener carrito activo con items y totales")
    public ResponseEntity<com.levelup.levelupgamer.dto.CarritoResponseDTO> obtenerCarrito(Authentication authentication) {
        return ResponseEntity.ok(carritoService.obtenerCarritoCompleto(authentication.getName()));
    }

    @GetMapping("/items")
    @Operation(summary = "Obtener items del carrito")
    public ResponseEntity<List<CarritoItem>> obtenerItems(Authentication authentication) {
        return ResponseEntity.ok(carritoService.obtenerItems(authentication.getName()));
    }

    @PostMapping("/agregar")
    @Operation(summary = "Agregar producto al carrito")
    public ResponseEntity<CarritoItem> agregarProducto(
            Authentication authentication,
            @RequestBody AgregarCarritoRequest request) {
        return ResponseEntity.ok(carritoService.agregarProducto(authentication.getName(), request));
    }

    @DeleteMapping("/items/{itemId}")
    @Operation(summary = "Eliminar item del carrito")
    public ResponseEntity<Void> eliminarItem(
            Authentication authentication,
            @PathVariable Integer itemId) {
        carritoService.eliminarProducto(authentication.getName(), itemId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/vaciar")
    @Operation(summary = "Vaciar carrito")
    public ResponseEntity<Void> vaciarCarrito(Authentication authentication) {
        carritoService.vaciarCarrito(authentication.getName());
        return ResponseEntity.ok().build();
    }
}
