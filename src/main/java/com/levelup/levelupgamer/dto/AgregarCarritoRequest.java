package com.levelup.levelupgamer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgregarCarritoRequest {
    private Integer productoId;
    private Integer cantidad;
}
