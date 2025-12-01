package com.levelup.levelupgamer.controller;

import com.levelup.levelupgamer.entity.Producto;
import com.levelup.levelupgamer.repository.ProductoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Endpoints para gestión de productos")
public class ProductoController {

    private final ProductoRepository productoRepository;

    @GetMapping
    @Operation(summary = "Listar todos los productos activos")
    public ResponseEntity<List<Producto>> listar() {
        return ResponseEntity.ok(productoRepository.findByActivoTrue());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Integer id) {
        return productoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/destacados")
    @Operation(summary = "Listar productos destacados")
    public ResponseEntity<List<Producto>> destacados() {
        return ResponseEntity.ok(productoRepository.findByDestacadoTrueAndActivoTrue());
    }

    @GetMapping("/categoria/{categoriaId}")
    @Operation(summary = "Listar productos por categoría")
    public ResponseEntity<List<Producto>> porCategoria(@PathVariable Integer categoriaId) {
        return ResponseEntity.ok(productoRepository.findByCategoriaId(categoriaId));
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar productos por nombre o descripción")
    public ResponseEntity<List<Producto>> buscar(@RequestParam String q) {
        return ResponseEntity.ok(productoRepository.buscarProductos(q));
    }

    @PostMapping("/crear")
    @Operation(summary = "Crear nuevo producto (ADMIN)")
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        return ResponseEntity.ok(productoRepository.save(producto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto (ADMIN)")
    public ResponseEntity<Producto> actualizar(@PathVariable Integer id, @RequestBody Producto producto) {
        return productoRepository.findById(id)
                .map(p -> {
                    producto.setId(id);
                    return ResponseEntity.ok(productoRepository.save(producto));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto (ADMIN)")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return productoRepository.findById(id)
                .map(p -> {
                    productoRepository.delete(p);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
