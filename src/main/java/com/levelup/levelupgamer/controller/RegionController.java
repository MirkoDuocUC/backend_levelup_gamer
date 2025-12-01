package com.levelup.levelupgamer.controller;

import com.levelup.levelupgamer.entity.Region;
import com.levelup.levelupgamer.repository.RegionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/regiones")
@RequiredArgsConstructor
@Tag(name = "Ubicación", description = "Endpoints para regiones y comunas de Chile")
public class RegionController {

    private final RegionRepository regionRepository;

    @GetMapping
    @Operation(summary = "Listar todas las regiones de Chile")
    public ResponseEntity<List<Region>> listar() {
        return ResponseEntity.ok(regionRepository.findAll());
    }
}
