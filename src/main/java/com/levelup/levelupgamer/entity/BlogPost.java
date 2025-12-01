package com.levelup.levelupgamer.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "blog_posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BlogPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @NotNull
    @Size(max = 255)
    @Column(nullable = false)
    private String titulo;

    @NotNull
    @Size(max = 255)
    @Column(unique = true, nullable = false)
    private String slug;

    @NotNull
    @Column(columnDefinition = "TEXT", nullable = false)
    private String contenido;

    @Column(columnDefinition = "TEXT")
    private String resumen;

    @Size(max = 500)
    @Column(name = "imagen_destacada")
    private String imagenDestacada;

    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false)
    @NotNull
    private Usuario autor;

    @Size(max = 100)
    private String categoria;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSON")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<String> tags;

    private Boolean publicado = false;

    private Integer vistas = 0;

    @Column(name = "fecha_publicacion")
    private LocalDateTime fechaPublicacion;

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
