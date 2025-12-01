package com.levelup.levelupgamer.repository;

import com.levelup.levelupgamer.entity.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarritoItemRepository extends JpaRepository<CarritoItem, Integer> {
    List<CarritoItem> findByCarritoId(Integer carritoId);
    Optional<CarritoItem> findByCarritoIdAndProductoId(Integer carritoId, Integer productoId);
    void deleteByCarritoId(Integer carritoId);
}
