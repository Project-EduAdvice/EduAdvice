package com.eduadvice.backend.dto;

import com.eduadvice.backend.entity.TeachingUnitCategory;
import java.math.BigDecimal;

public record TeachingUnitResponseDTO(
        Long id,
        Long semesterId,
        String code,
        String name,
        TeachingUnitCategory category,
        BigDecimal ects
) {}