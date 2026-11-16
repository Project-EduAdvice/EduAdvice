
package com.eduadvice.backend.controller;

import com.eduadvice.backend.dto.LearningResourceRequestDTO;
import com.eduadvice.backend.dto.LearningResourceResponseDTO;
import com.eduadvice.backend.service.LearningResourceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects/{subjectId}/learning-resources")
public class LearningResourceController {

    private final LearningResourceService learningResourceService;

    public LearningResourceController(
            LearningResourceService learningResourceService) {
        this.learningResourceService = learningResourceService;
    }

    // Get all learning resources belonging to a subject
    @GetMapping
    public List<LearningResourceResponseDTO> getResourcesBySubject(
            @PathVariable Long subjectId) {

        return learningResourceService.getResourcesBySubject(subjectId);
    }

    // Create a learning resource for a subject
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LearningResourceResponseDTO createResource(
            @PathVariable Long subjectId,
            @RequestBody LearningResourceRequestDTO request) {

        return learningResourceService.createResource(subjectId, request);
    }
}
