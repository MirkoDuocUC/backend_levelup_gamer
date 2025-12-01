package com.levelup.levelupgamer.service;

import com.levelup.levelupgamer.dto.CrearResenaRequest;
import com.levelup.levelupgamer.entity.Producto;
import com.levelup.levelupgamer.entity.Resena;
import com.levelup.levelupgamer.entity.Usuario;
import com.levelup.levelupgamer.repository.OrdenItemRepository;

import com.levelup.levelupgamer.repository.ProductoRepository;
import com.levelup.levelupgamer.repository.ResenaRepository;
import com.levelup.levelupgamer.repository.UsuarioRepository;
import com.levelup.levelupgamer.util.EstadoResena;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResenaService {

    private final ResenaRepository resenaRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final OrdenItemRepository ordenItemRepository;

    @Transactional
    public Resena crearResena(String email, CrearResenaRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Verificar si el usuario ha comprado el producto para marcar como verificado
        boolean verificado = ordenItemRepository.existsByOrdenUsuarioIdAndProductoId(usuario.getId(), producto.getId());
        
        // Nota: La verificación real requeriría una consulta más compleja o relaciones bidireccionales.
        // Por simplicidad en este paso, lo dejaremos como false o implementaremos una consulta simple si es crítico.
        
        Resena resena = Resena.builder()
                .usuario(usuario)
                .producto(producto)
                .calificacion(request.getCalificacion())
                .titulo(request.getTitulo())
                .comentario(request.getComentario())
                .verificado(verificado) // Idealmente true si compró
                .estado(EstadoResena.pendiente)
                .likes(0)
                .build();

        return resenaRepository.save(resena);
    }

    public List<Resena> obtenerResenasProducto(Integer productoId) {
        return resenaRepository.findByProductoIdAndEstado(productoId, EstadoResena.aprobado);
    }

    public List<Resena> obtenerPendientes() {
        return resenaRepository.findByEstadoOrderByCreatedAtDesc(EstadoResena.pendiente);
    }

    @Transactional
    public Resena moderarResena(Integer id, EstadoResena estado) {
        Resena resena = resenaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));
        
        resena.setEstado(estado);
        return resenaRepository.save(resena);
    }
}
