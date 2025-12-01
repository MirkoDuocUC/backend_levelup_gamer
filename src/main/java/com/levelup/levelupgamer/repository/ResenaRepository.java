package com.levelup.levelupgamer.repository;

import com.levelup.levelupgamer.entity.Resena;
import com.levelup.levelupgamer.util.EstadoResena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Integer> {
    List<Resena> findByProductoIdAndEstado(Integer productoId, EstadoResena estado);
    List<Resena> findByEstadoOrderByCreatedAtDesc(EstadoResena estado);
    List<Resena> findByUsuarioId(Integer usuarioId);
}
