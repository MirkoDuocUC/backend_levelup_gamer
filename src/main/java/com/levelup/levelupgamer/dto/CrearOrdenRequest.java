package com.levelup.levelupgamer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearOrdenRequest {
    @NotBlank
    private String direccionEnvio;
    
    private String comunaEnvio;
    
    private String regionEnvio;
    
    private String telefonoContacto;
    
    private String metodoPago;
    
    private String notas;
}
