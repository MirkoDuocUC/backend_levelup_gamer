package com.levelup.levelupgamer.entity;

import com.levelup.levelupgamer.util.EstadoOrden;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ordenes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @NotNull
    @Size(max = 50)
    @Column(name = "numero_orden", unique = true, nullable = false)
    private String numeroOrden;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @NotNull
    private Usuario usuario;

    @NotNull
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal subtotal;

    @Column(precision = 10, scale = 2)
    private BigDecimal descuento = BigDecimal.ZERO;

    @NotNull
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoOrden estado = EstadoOrden.pendiente;

    @Size(max = 50)
    @Column(name = "metodo_pago")
    private String metodoPago;

    @NotNull
    @Column(name = "direccion_envio", columnDefinition = "TEXT", nullable = false)
    private String direccionEnvio;

    @Size(max = 100)
    @Column(name = "comuna_envio")
    private String comunaEnvio;

    @Size(max = 100)
    @Column(name = "region_envio")
    private String regionEnvio;

    @Size(max = 20)
    @Column(name = "telefono_contacto")
    private String telefonoContacto;

    @Column(columnDefinition = "TEXT")
    private String notas;

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;

    @Column(name = "fecha_entrega")
    private LocalDateTime fechaEntrega;

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
