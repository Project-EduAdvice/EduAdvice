package com.eduadvice.backend.dto;

import com.eduadvice.backend.entity.ResourceType;

public record LearningResourceRequestDTO(
        Long subjectId,
        String title,
        String url,
        String description,
        ResourceType resourceType
) {
}