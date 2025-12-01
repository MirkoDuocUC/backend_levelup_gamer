package com.levelup.levelupgamer.service;

import com.levelup.levelupgamer.dto.AgregarCarritoRequest;
import com.levelup.levelupgamer.entity.Carrito;
import com.levelup.levelupgamer.entity.CarritoItem;
import com.levelup.levelupgamer.entity.Producto;
import com.levelup.levelupgamer.entity.Usuario;
import com.levelup.levelupgamer.repository.CarritoItemRepository;
import com.levelup.levelupgamer.repository.CarritoRepository;
import com.levelup.levelupgamer.repository.ProductoRepository;
import com.levelup.levelupgamer.repository.UsuarioRepository;
import com.levelup.levelupgamer.util.EstadoCarrito;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final CarritoItemRepository carritoItemRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Carrito obtenerCarritoActivo(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return carritoRepository.findByUsuarioIdAndEstado(usuario.getId(), EstadoCarrito.activo)
                .orElseGet(() -> {
                    Carrito nuevoCarrito = Carrito.builder()
                            .usuario(usuario)
                            .estado(EstadoCarrito.activo)
                            .build();
                    return carritoRepository.save(nuevoCarrito);
                });
    }

    @Transactional
    public CarritoItem agregarProducto(String email, AgregarCarritoRequest request) {
        Carrito carrito = obtenerCarritoActivo(email);
        
        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (!producto.getActivo()) {
            throw new RuntimeException("El producto no está disponible");
        }

        if (producto.getStock() < request.getCantidad()) {
            throw new RuntimeException("Stock insuficiente. Disponible: " + producto.getStock());
        }

        Optional<CarritoItem> itemExistente = carritoItemRepository.findByCarritoIdAndProductoId(carrito.getId(), producto.getId());

        if (itemExistente.isPresent()) {
            CarritoItem item = itemExistente.get();
            int nuevaCantidad = item.getCantidad() + request.getCantidad();
            
            if (producto.getStock() < nuevaCantidad) {
                throw new RuntimeException("Stock insuficiente para la cantidad total solicitada");
            }
            
            item.setCantidad(nuevaCantidad);
            // Actualizamos el precio al actual por si cambió
            item.setPrecioUnitario(producto.getPrecio());
            return carritoItemRepository.save(item);
        } else {
            CarritoItem newItem = CarritoItem.builder()
                    .carrito(carrito)
                    .producto(producto)
                    .cantidad(request.getCantidad())
                    .precioUnitario(producto.getPrecio())
                    .build();
            return carritoItemRepository.save(newItem);
        }
    }

    @Transactional
    public void eliminarProducto(String email, Integer itemId) {
        Carrito carrito = obtenerCarritoActivo(email);
        CarritoItem item = carritoItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));

        if (!item.getCarrito().getId().equals(carrito.getId())) {
            throw new RuntimeException("No tienes permiso para eliminar este item");
        }

        carritoItemRepository.delete(item);
    }

    @Transactional
    public void vaciarCarrito(String email) {
        Carrito carrito = obtenerCarritoActivo(email);
        carritoItemRepository.deleteByCarritoId(carrito.getId());
    }
    
    public List<CarritoItem> obtenerItems(String email) {
        Carrito carrito = obtenerCarritoActivo(email);
        return carritoItemRepository.findByCarritoId(carrito.getId());
    }
    
    public com.levelup.levelupgamer.dto.CarritoResponseDTO obtenerCarritoCompleto(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Carrito carrito = obtenerCarritoActivo(email);
        List<CarritoItem> items = carritoItemRepository.findByCarritoId(carrito.getId());
        
        // Construir DTOs de items
        List<com.levelup.levelupgamer.dto.CarritoItemDTO> itemDTOs = items.stream()
                .map(item -> {
                    // DTO de producto simplificado
                    com.levelup.levelupgamer.dto.ProductoSimpleDTO productoDTO = com.levelup.levelupgamer.dto.ProductoSimpleDTO.builder()
                            .id(item.getProducto().getId())
                            .codigo(item.getProducto().getCodigo())
                            .nombre(item.getProducto().getNombre())
                            .descripcion(item.getProducto().getDescripcion())
                            .precio(item.getProducto().getPrecio())
                            .stock(item.getProducto().getStock())
                            .imagenUrl(item.getProducto().getImagenUrl())
                            .activo(item.getProducto().getActivo())
                            .categoriaNombre(item.getProducto().getCategoria().getNombre())
                            .build();
                    
                    // DTO de item
                    return com.levelup.levelupgamer.dto.CarritoItemDTO.builder()
                            .id(item.getId())
                            .producto(productoDTO)
                            .cantidad(item.getCantidad())
                            .precioUnitario(item.getPrecioUnitario())
                            .subtotal(item.getPrecioUnitario().multiply(java.math.BigDecimal.valueOf(item.getCantidad())))
                            .build();
                })
                .toList();
        
        // Calcular totales
        java.math.BigDecimal subtotal = items.stream()
                .map(item -> item.getPrecioUnitario().multiply(java.math.BigDecimal.valueOf(item.getCantidad())))
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        
        java.math.BigDecimal descuento = usuario.getDescuentoDuoc() != null 
                ? subtotal.multiply(usuario.getDescuentoDuoc()).divide(java.math.BigDecimal.valueOf(100))
                : java.math.BigDecimal.ZERO;
        
        java.math.BigDecimal total = subtotal.subtract(descuento);
        
        return com.levelup.levelupgamer.dto.CarritoResponseDTO.builder()
                .id(carrito.getId())
                .estado(carrito.getEstado().name())
                .items(itemDTOs)
                .subtotal(subtotal)
                .descuento(descuento)
                .total(total)
                .cantidadItems(items.size())
                .build();
    }
}
