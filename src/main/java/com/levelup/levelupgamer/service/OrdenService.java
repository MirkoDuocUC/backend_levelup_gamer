package com.levelup.levelupgamer.service;

import com.levelup.levelupgamer.dto.CrearOrdenRequest;
import com.levelup.levelupgamer.entity.*;
import com.levelup.levelupgamer.repository.*;
import com.levelup.levelupgamer.util.EstadoCarrito;
import com.levelup.levelupgamer.util.EstadoOrden;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdenService {

    private final OrdenRepository ordenRepository;
    private final OrdenItemRepository ordenItemRepository;
    private final CarritoRepository carritoRepository;
    private final CarritoItemRepository carritoItemRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    @Transactional
    public Orden crearOrden(String email, CrearOrdenRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Carrito carrito = carritoRepository.findByUsuarioIdAndEstado(usuario.getId(), EstadoCarrito.activo)
                .orElseThrow(() -> new RuntimeException("No hay carrito activo"));

        List<CarritoItem> items = carritoItemRepository.findByCarritoId(carrito.getId());
        
        if (items.isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        // Calcular totales
        BigDecimal subtotal = BigDecimal.ZERO;
        for (CarritoItem item : items) {
            subtotal = subtotal.add(item.getPrecioUnitario().multiply(BigDecimal.valueOf(item.getCantidad())));
            
            // Validar y descontar stock
            Producto producto = item.getProducto();
            if (producto.getStock() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
            }
            producto.setStock(producto.getStock() - item.getCantidad());
            producto.setVentasTotal(producto.getVentasTotal() + item.getCantidad());
            productoRepository.save(producto);
        }

        // Calcular descuento
        BigDecimal descuento = subtotal.multiply(usuario.getDescuentoDuoc().divide(BigDecimal.valueOf(100)));
        BigDecimal total = subtotal.subtract(descuento);

        // Generar número de orden
        long count = ordenRepository.count() + 1;
        String numeroOrden = String.format("ORD-%d-%04d", LocalDateTime.now().getYear(), count);

        // Crear Orden
        Orden orden = Orden.builder()
                .numeroOrden(numeroOrden)
                .usuario(usuario)
                .subtotal(subtotal)
                .descuento(descuento)
                .total(total)
                .estado(EstadoOrden.pendiente)
                .metodoPago(request.getMetodoPago())
                .direccionEnvio(request.getDireccionEnvio())
                .comunaEnvio(request.getComunaEnvio())
                .regionEnvio(request.getRegionEnvio())
                .telefonoContacto(request.getTelefonoContacto())
                .notas(request.getNotas())
                .build();
        
        orden = ordenRepository.save(orden);

        // Crear Items de Orden
        for (CarritoItem item : items) {
            OrdenItem ordenItem = OrdenItem.builder()
                    .orden(orden)
                    .producto(item.getProducto())
                    .productoNombre(item.getProducto().getNombre())
                    .productoCodigo(item.getProducto().getCodigo())
                    .cantidad(item.getCantidad())
                    .precioUnitario(item.getPrecioUnitario())
                    .subtotal(item.getPrecioUnitario().multiply(BigDecimal.valueOf(item.getCantidad())))
                    .build();
            ordenItemRepository.save(ordenItem);
        }

        // Actualizar estado del carrito
        carrito.setEstado(EstadoCarrito.convertido);
        carritoRepository.save(carrito);

        // Crear nuevo carrito vacío
        Carrito nuevoCarrito = Carrito.builder()
                .usuario(usuario)
                .estado(EstadoCarrito.activo)
                .build();
        carritoRepository.save(nuevoCarrito);

        return orden;
    }

    public List<Orden> obtenerMisOrdenes(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return ordenRepository.findByUsuarioIdOrderByCreatedAtDesc(usuario.getId());
    }

    public List<Orden> obtenerTodasLasOrdenes() {
        return ordenRepository.findAllByOrderByCreatedAtDesc();
    }
    
    public Orden obtenerOrdenPorId(Integer id) {
        return ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
    }
    
    @Transactional
    public Orden actualizarEstado(Integer id, EstadoOrden nuevoEstado) {
        Orden orden = obtenerOrdenPorId(id);
        orden.setEstado(nuevoEstado);
        
        // Si se cancela, devolver stock
        if (nuevoEstado == EstadoOrden.cancelado) {
            List<OrdenItem> items = ordenItemRepository.findByOrdenId(id);
            for (OrdenItem item : items) {
                Producto producto = item.getProducto();
                producto.setStock(producto.getStock() + item.getCantidad());
                producto.setVentasTotal(producto.getVentasTotal() - item.getCantidad());
                productoRepository.save(producto);
            }
        }
        
        return ordenRepository.save(orden);
    }
}
