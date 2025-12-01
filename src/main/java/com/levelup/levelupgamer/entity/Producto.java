package com.levelup.levelupgamer.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @NotNull
    @Size(max = 50)
    @Column(unique = true, nullable = false)
    private String codigo;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    @NotNull
    private Categoria categoria;

    @NotNull
    @Size(max = 255)
    @Column(nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @NotNull
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal precio;

    private Integer stock = 0;

    @Column(name = "stock_minimo")
    private Integer stockMinimo = 10;

    @Size(max = 500)
    @Column(name = "imagen_url")
    private String imagenUrl;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "imagenes_adicionales", columnDefinition = "JSON")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Map<String, Object> imagenesAdicionales;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "especificaciones", columnDefinition = "JSON")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Map<String, Object> especificaciones;

    private Boolean activo = true;

    private Boolean destacado = false;

    private Integer vistas = 0;

    @Column(name = "ventas_total")
    private Integer ventasTotal = 0;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
