package com.levelup.levelupgamer.controller;

import com.levelup.levelupgamer.entity.Pedido;
import com.levelup.levelupgamer.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Endpoints para gestión de pedidos simples")
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    @Operation(summary = "Procesar compra del carrito actual")
    public ResponseEntity<Pedido> crearPedido(Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pedidoService.crearPedido(authentication.getName()));
    }
}
