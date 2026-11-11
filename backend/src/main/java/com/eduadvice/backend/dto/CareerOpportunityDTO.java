package com.eduadvice.backend.dto;

public record CareerOpportunityDTO(Long id,
                                   String title,
                                   String sector,
                                   String description,
                                   String required_skills) {
}
