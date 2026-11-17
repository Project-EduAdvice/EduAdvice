
package com.eduadvice.backend.controller;

import com.eduadvice.backend.dto.LearningResourceRequestDTO;
import com.eduadvice.backend.dto.LearningResourceResponseDTO;
import com.eduadvice.backend.dto.SubjectResponseDTO;
import com.eduadvice.backend.service.LearningResourceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/learning-resources","/api/learning-resources/"})
public class LearningResourceController {

    private final LearningResourceService learningResourceService;

    public LearningResourceController(
            LearningResourceService learningResourceService) {
        this.learningResourceService = learningResourceService;
    }

    @GetMapping
    public List<LearningResourceResponseDTO> getAllResources(){
        return learningResourceService.getAllResources();
    }

    @GetMapping({"/{id}","/{id}/"})
    public LearningResourceResponseDTO getResourceById(@PathVariable Long id){
        return learningResourceService.getResourceById(id);
    }

    @GetMapping({"/{id}/subject","{id}/subject/"})
    public ResponseEntity<SubjectResponseDTO> getSubject(@PathVariable Long id){
        return ResponseEntity.ok(learningResourceService.getSubject(id));
    }

    // Create a learning resource for a subject
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LearningResourceResponseDTO createResource(
            @RequestBody LearningResourceRequestDTO request) {

        return learningResourceService.createResource(request);
    }
}
