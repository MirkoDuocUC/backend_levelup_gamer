package com.levelup.levelupgamer.repository;

import com.levelup.levelupgamer.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    List<Categoria> findByActivoTrueOrderByOrdenAsc();
    List<Categoria> findAllByOrderByOrdenAsc();
}
