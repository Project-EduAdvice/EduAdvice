
package com.eduadvice.backend.dto;

import java.math.BigDecimal;

public record SubjectResponseDTO(
        Long id,
        Long teachingUnitId,
        String name,
        String description,
        String content,
        BigDecimal ects
) {}
