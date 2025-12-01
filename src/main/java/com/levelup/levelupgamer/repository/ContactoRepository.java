package com.levelup.levelupgamer.repository;

import com.levelup.levelupgamer.entity.Contacto;
import com.levelup.levelupgamer.util.EstadoContacto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactoRepository extends JpaRepository<Contacto, Integer> {
    List<Contacto> findByEstadoOrderByCreatedAtDesc(EstadoContacto estado);
    List<Contacto> findAllByOrderByCreatedAtDesc();
}
