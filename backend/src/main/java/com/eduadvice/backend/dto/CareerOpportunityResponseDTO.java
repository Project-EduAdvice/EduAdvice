package com.eduadvice.backend.dto;

import java.util.Set;

public record CareerOpportunityResponseDTO(Long id,
                                           String title,
                                           String sector,
                                           String description,
                                           String required_skills,
                                           Set<Long> parcoursIds) {
}
