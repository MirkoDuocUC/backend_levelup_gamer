package com.levelup.levelupgamer.repository;

import com.levelup.levelupgamer.entity.Carrito;
import com.levelup.levelupgamer.util.EstadoCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Integer> {
    Optional<Carrito> findByUsuarioIdAndEstado(Integer usuarioId, EstadoCarrito estado);
}
