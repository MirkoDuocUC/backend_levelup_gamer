package com.levelup.levelupgamer.controller;

import com.levelup.levelupgamer.entity.BlogPost;
import com.levelup.levelupgamer.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blog")
@RequiredArgsConstructor
@Tag(name = "Blog", description = "Endpoints para gestión del blog")
public class BlogController {

    private final BlogService blogService;

    @GetMapping
    @Operation(summary = "Listar posts publicados")
    public ResponseEntity<List<BlogPost>> listarPublicados() {
        return ResponseEntity.ok(blogService.obtenerPostsPublicados());
    }

    @GetMapping("/{slug}")
    @Operation(summary = "Obtener post por slug")
    public ResponseEntity<BlogPost> obtenerPorSlug(@PathVariable String slug) {
        return ResponseEntity.ok(blogService.obtenerPorSlug(slug));
    }

    @PostMapping("/crear")
    @Operation(summary = "Crear nuevo post (ADMIN)")
    public ResponseEntity<BlogPost> crear(
            Authentication authentication,
            @RequestBody BlogPost post) {
        return ResponseEntity.ok(blogService.crearPost(authentication.getName(), post));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar post (ADMIN)")
    public ResponseEntity<BlogPost> actualizar(
            @PathVariable Integer id,
            @RequestBody BlogPost post) {
        return ResponseEntity.ok(blogService.actualizarPost(id, post));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar post (ADMIN)")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        blogService.eliminarPost(id);
        return ResponseEntity.ok().build();
    }
}
