package com.eduadvice.backend.dto;

import com.eduadvice.backend.entity.ResourceType;

public record LearningResourceRequestDTO(
        String title,
        String url,
        String description,
        ResourceType resourceType
) {
}