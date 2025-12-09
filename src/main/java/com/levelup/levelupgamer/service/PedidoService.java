package com.levelup.levelupgamer.service;

import com.levelup.levelupgamer.entity.*;
import com.levelup.levelupgamer.repository.*;
import com.levelup.levelupgamer.util.EstadoCarrito;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final CarritoRepository carritoRepository;
    private final CarritoItemRepository carritoItemRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    @Transactional
    public Pedido crearPedido(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Carrito carrito = carritoRepository.findByUsuarioIdAndEstado(usuario.getId(), EstadoCarrito.activo)
                .orElseThrow(() -> new RuntimeException("No hay carrito activo"));

        List<CarritoItem> items = carritoItemRepository.findByCarritoId(carrito.getId());

        if (items.isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        // Calcular total y validar stock
        BigDecimal total = BigDecimal.ZERO;
        for (CarritoItem item : items) {
            total = total.add(item.getPrecioUnitario().multiply(BigDecimal.valueOf(item.getCantidad())));

            Producto producto = item.getProducto();
            if (producto.getStock() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
            }
            // Descontar stock (opcional, pero recomendado en la guía)
            producto.setStock(producto.getStock() - item.getCantidad());
            producto.setVentasTotal(producto.getVentasTotal() + item.getCantidad());
            productoRepository.save(producto);
        }

        // Crear Pedido
        Pedido pedido = Pedido.builder()
                .usuario(usuario)
                .total(total)
                .estado("COMPLETADO") // Según guía, respuesta esperada dice COMPLETADO
                .build();
        
        pedido = pedidoRepository.save(pedido);

        // Crear Detalles
        for (CarritoItem item : items) {
            DetallePedido detalle = DetallePedido.builder()
                    .pedido(pedido)
                    .producto(item.getProducto())
                    .cantidad(item.getCantidad())
                    .precioUnitario(item.getPrecioUnitario())
                    .build();
            detallePedidoRepository.save(detalle);
        }

        // Vaciar carrito (eliminar items)
        carritoItemRepository.deleteAll(items);
        
        // Opcional: Marcar carrito como convertido o dejarlo activo pero vacío?
        // La guía dice "Vaciar el carrito (eliminar items de la tabla de carrito)".
        // OrdenService marca como convertido y crea uno nuevo.
        // Seguiré la guía literal: "Vaciar el carrito".
        
        return pedido;
    }
}
