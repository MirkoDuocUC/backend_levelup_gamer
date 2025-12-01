package com.levelup.levelupgamer.entity;

import com.levelup.levelupgamer.util.EstadoUsuario;
import com.levelup.levelupgamer.util.RolUsuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @Email
    @NotNull
    @Size(max = 255)
    @Column(unique = true, nullable = false)
    private String email;

    @NotNull
    @Size(max = 255)
    @Column(name = "password_hash", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private String passwordHash;

    @NotNull
    @Size(max = 100)
    @Column(nullable = false)
    private String nombre;

    @NotNull
    @Size(max = 100)
    @Column(nullable = false)
    private String apellido;

    @Size(max = 20)
    private String telefono;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    private Integer edad;

    @Column(columnDefinition = "TEXT")
    private String direccion;

    @ManyToOne
    @JoinColumn(name = "comuna_id")
    private Comuna comuna;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RolUsuario rol = RolUsuario.cliente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoUsuario estado = EstadoUsuario.activo;

    @Column(name = "descuento_duoc", precision = 5, scale = 2)
    private BigDecimal descuentoDuoc = BigDecimal.ZERO;

    private Boolean newsletter = false;

    private Boolean notificaciones = true;

    @Size(max = 20)
    private String tema = "dark";

    @Column(name = "email_verificado")
    private Boolean emailVerificado = false;

    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "ultima_conexion")
    private LocalDateTime ultimaConexion;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ========== UserDetails Implementation ==========

    @Override
    @com.fasterxml.jackson.annotation.JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    @Override
    @com.fasterxml.jackson.annotation.JsonIgnore
    public String getPassword() {
        return passwordHash;
    }

    @Override
    @com.fasterxml.jackson.annotation.JsonIgnore
    public String getUsername() {
        return email;
    }

    @Override
    @com.fasterxml.jackson.annotation.JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @com.fasterxml.jackson.annotation.JsonIgnore
    public boolean isAccountNonLocked() {
        return estado != EstadoUsuario.suspendido;
    }

    @Override
    @com.fasterxml.jackson.annotation.JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @com.fasterxml.jackson.annotation.JsonIgnore
    public boolean isEnabled() {
        return estado == EstadoUsuario.activo && emailVerificado;
    }
}
