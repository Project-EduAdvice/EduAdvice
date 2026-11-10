package com.eduadvice.backend.dto;

import java.util.Set;

public record ParcoursResponseDTO(Long id,
                                  String name,
                                  String level,
                                  String diploma,
                                  String mention,
                                  String establishment,
                                  String description,
                                  Set<Long> careerOpportunityIds) {
}
