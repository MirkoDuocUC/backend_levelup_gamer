package com.levelup.levelupgamer.controller;

import com.levelup.levelupgamer.entity.Comuna;
import com.levelup.levelupgamer.repository.ComunaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comunas")
@RequiredArgsConstructor
@Tag(name = "Ubicación", description = "Endpoints para regiones y comunas de Chile")
public class ComunaController {

    private final ComunaRepository comunaRepository;

    @GetMapping
    @Operation(summary = "Listar todas las comunas")
    public ResponseEntity<List<Comuna>> listar() {
        return ResponseEntity.ok(comunaRepository.findAll());
    }

    @GetMapping("/region/{regionId}")
    @Operation(summary = "Listar comunas por región")
    public ResponseEntity<List<Comuna>> porRegion(@PathVariable Integer regionId) {
        return ResponseEntity.ok(comunaRepository.findByRegionId(regionId));
    }
}
