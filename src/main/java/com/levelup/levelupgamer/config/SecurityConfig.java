package com.levelup.levelupgamer.config;

import com.levelup.levelupgamer.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos
                        .requestMatchers("/auth/**", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
                        
                        // Endpoints solo para admins
                        .requestMatchers("/api/admin/**").hasRole("admin")
                        .requestMatchers("/api/usuarios/**").hasRole("admin")
                        .requestMatchers("/api/categorias/crear", "/api/categorias/*/editar", "/api/categorias/*/eliminar").hasRole("admin")
                        .requestMatchers("/api/productos/crear", "/api/productos/*/editar", "/api/productos/*/eliminar").hasRole("admin")
                        .requestMatchers("/api/resenas/moderar/**").hasRole("admin")
                        .requestMatchers("/api/blog/crear", "/api/blog/*/editar", "/api/blog/*/eliminar").hasRole("admin")
                        .requestMatchers("/api/contacto/admin/**").hasRole("admin")
                        
                        // Endpoints para clientes autenticados
                        .requestMatchers("/api/carrito/**").hasAnyRole("cliente", "admin")
                        .requestMatchers("/api/ordenes/crear").hasRole("cliente")
                        .requestMatchers("/api/ordenes/mis-ordenes").hasAnyRole("cliente", "admin")
                        .requestMatchers("/api/resenas/crear").hasRole("cliente")
                        .requestMatchers("/api/perfil/**").authenticated()
                        
                        // Endpoints públicos de lectura
                        .requestMatchers("/api/productos/**", "/api/categorias/**").permitAll()
                        .requestMatchers("/api/blog/**").permitAll()
                        .requestMatchers("/api/regiones/**", "/api/comunas/**").permitAll()
                        .requestMatchers("/api/contacto/enviar").permitAll()
                        
                        // Cualquier otra petición requiere autenticación
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // IMPORTANTE: Ajusta los orígenes según tu frontend
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:4200", "http://localhost:5173"));
        
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
