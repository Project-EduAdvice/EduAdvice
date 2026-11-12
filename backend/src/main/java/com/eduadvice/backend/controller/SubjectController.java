
package com.eduadvice.backend.controller;

import com.eduadvice.backend.dto.SubjectResponseDTO;
import com.eduadvice.backend.service.SubjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public List<SubjectResponseDTO> getAllSubjects() {
        return subjectService.getAllSubjects();
    }

    @GetMapping("/{id}")
    public SubjectResponseDTO getSubjectById(@PathVariable Long id) {
        return subjectService.getSubjectById(id);
    }

    @GetMapping("/teaching-unit/{teachingUnitId}")
    public List<SubjectResponseDTO> getSubjectsByTeachingUnit(
            @PathVariable Long teachingUnitId
    ) {
        return subjectService.getSubjectsByTeachingUnit(teachingUnitId);
    }
}
