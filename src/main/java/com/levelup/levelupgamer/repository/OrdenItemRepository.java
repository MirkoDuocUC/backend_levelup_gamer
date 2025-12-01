package com.levelup.levelupgamer.repository;

import com.levelup.levelupgamer.entity.OrdenItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenItemRepository extends JpaRepository<OrdenItem, Integer> {
    List<OrdenItem> findByOrdenId(Integer ordenId);
    boolean existsByOrdenUsuarioIdAndProductoId(Integer usuarioId, Integer productoId);
}
