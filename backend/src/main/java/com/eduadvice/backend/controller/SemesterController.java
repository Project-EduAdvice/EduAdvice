package com.eduadvice.backend.controller;

import com.eduadvice.backend.entity.Semester;
import com.eduadvice.backend.service.SemesterService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/semesters")
public class SemesterController {

    private final SemesterService semesterService;

    public SemesterController(SemesterService semesterService) {
        this.semesterService = semesterService;
    }

    @GetMapping
    public List<Semester> getAllSemesters() {
        return semesterService.getAllSemesters();
    }

    @GetMapping("/{id}")
    public Semester getSemesterById(@PathVariable Long id) {
        return semesterService.getSemesterById(id);
    }

    @GetMapping("/parcours/{parcoursId}")
    public List<Semester> getSemestersByParcours(@PathVariable Long parcoursId) {
        return semesterService.getSemestersByParcours(parcoursId);
    }
}