package com.eduadvice.backend.dto;

import java.time.LocalDateTime;

public record ErrorResponseDTO(int status,
                               String error,
                               String message,
                               String path,
                               LocalDateTime timestamp) {
}
