package com.levelup.levelupgamer.repository;

import com.levelup.levelupgamer.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    Optional<Producto> findByCodigo(String codigo);
    List<Producto> findByActivoTrue();
    List<Producto> findByCategoriaId(Integer categoriaId);
    List<Producto> findByDestacadoTrueAndActivoTrue();
    
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND " +
           "(LOWER(p.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Producto> buscarProductos(String searchTerm);
}
