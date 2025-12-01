package com.levelup.levelupgamer.repository;

import com.levelup.levelupgamer.entity.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Integer> {
    Optional<Orden> findByNumeroOrden(String numeroOrden);
    List<Orden> findByUsuarioIdOrderByCreatedAtDesc(Integer usuarioId);
    List<Orden> findAllByOrderByCreatedAtDesc();
}
