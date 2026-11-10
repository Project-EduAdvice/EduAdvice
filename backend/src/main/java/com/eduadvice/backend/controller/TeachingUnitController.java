package com.eduadvice.backend.controller;

import com.eduadvice.backend.dto.TeachingUnitResponseDTO;
import com.eduadvice.backend.service.TeachingUnitService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teaching-units")
public class TeachingUnitController {

    private final TeachingUnitService teachingUnitService;

    public TeachingUnitController(TeachingUnitService teachingUnitService) {
        this.teachingUnitService = teachingUnitService;
    }

    @GetMapping
    public List<TeachingUnitResponseDTO> getAllTeachingUnits() {
        return teachingUnitService.getAllTeachingUnits();
    }

    @GetMapping("/{id}")
    public TeachingUnitResponseDTO getTeachingUnitById(@PathVariable Long id) {
        return teachingUnitService.getTeachingUnitById(id);
    }

    @GetMapping("/semester/{semesterId}")
    public List<TeachingUnitResponseDTO> getTeachingUnitsBySemester(
            @PathVariable Long semesterId) {
        return teachingUnitService.getTeachingUnitsBySemester(semesterId);
    }
}