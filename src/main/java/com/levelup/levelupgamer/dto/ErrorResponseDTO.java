package com.levelup.levelupgamer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponseDTO {
    private Integer status;
    private String error;
    private String mensaje;
    private String path;
    private LocalDateTime timestamp;

    public static ErrorResponseDTO of(HttpStatus status, String mensaje, String path) {
        return ErrorResponseDTO.builder()
                .status(status.value())
                .error(status.getReasonPhrase())
                .mensaje(mensaje)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
