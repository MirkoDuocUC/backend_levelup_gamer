package com.levelup.levelupgamer.controller;

import com.levelup.levelupgamer.entity.Contacto;
import com.levelup.levelupgamer.service.ContactoService;
import com.levelup.levelupgamer.util.EstadoContacto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacto")
@RequiredArgsConstructor
@Tag(name = "Contacto", description = "Endpoints para formulario de contacto")
public class ContactoController {

    private final ContactoService contactoService;

    @PostMapping("/enviar")
    @Operation(summary = "Enviar mensaje de contacto (Público)")
    public ResponseEntity<Contacto> enviar(@Valid @RequestBody Contacto contacto) {
        return ResponseEntity.ok(contactoService.enviarMensaje(contacto));
    }

    @GetMapping("/admin/mensajes")
    @Operation(summary = "Ver todos los mensajes (ADMIN)")
    public ResponseEntity<List<Contacto>> verTodos() {
        return ResponseEntity.ok(contactoService.obtenerTodos());
    }

    @PutMapping("/admin/responder/{id}")
    @Operation(summary = "Responder mensaje (ADMIN)")
    public ResponseEntity<Contacto> responder(
            @PathVariable Integer id,
            @RequestBody String respuesta) {
        return ResponseEntity.ok(contactoService.responderMensaje(id, respuesta));
    }
}
