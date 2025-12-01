package com.levelup.levelupgamer.controller;

import com.levelup.levelupgamer.entity.Categoria;
import com.levelup.levelupgamer.repository.CategoriaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
@Tag(name = "Categorías", description = "Endpoints para gestión de categorías")
public class CategoriaController {

    private final CategoriaRepository categoriaRepository;

    @GetMapping
    @Operation(summary = "Listar todas las categorías activas")
    public ResponseEntity<List<Categoria>> listar() {
        return ResponseEntity.ok(categoriaRepository.findByActivoTrueOrderByOrdenAsc());
    }

    @GetMapping("/todas")
    @Operation(summary = "Listar todas las categorías (ADMIN)")
    public ResponseEntity<List<Categoria>> todas() {
        return ResponseEntity.ok(categoriaRepository.findAllByOrderByOrdenAsc());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoría por ID")
    public ResponseEntity<Categoria> obtenerPorId(@PathVariable Integer id) {
        return categoriaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear nueva categoría (ADMIN)")
    public ResponseEntity<Categoria> crear(@RequestBody Categoria categoria) {
        return ResponseEntity.ok(categoriaRepository.save(categoria));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar categoría (ADMIN)")
    public ResponseEntity<Categoria> actualizar(@PathVariable Integer id, @RequestBody Categoria categoria) {
        return categoriaRepository.findById(id)
                .map(c -> {
                    categoria.setId(id);
                    return ResponseEntity.ok(categoriaRepository.save(categoria));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar categoría (ADMIN)")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return categoriaRepository.findById(id)
                .map(c -> {
                    categoriaRepository.delete(c);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
