package com.eduadvice.backend.controller;

import com.eduadvice.backend.dto.SemesterDTO;
import com.eduadvice.backend.dto.ParcoursDTO;
import com.eduadvice.backend.entity.Semester;
import com.eduadvice.backend.service.SemesterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/semesters", "/api/semesters/"})
public class SemesterController {

    private final SemesterService semesterService;

    public SemesterController(SemesterService semesterService) {
        this.semesterService = semesterService;
    }

    @GetMapping
    public ResponseEntity<List<SemesterDTO>> getAllSemesters() {
        return ResponseEntity.ok(semesterService.getAllSemesters());
    }

    @GetMapping({"/{id}", "/{id}/"})
    public ResponseEntity<SemesterDTO> getSemesterById(@PathVariable Long id) {
        return ResponseEntity.ok(semesterService.getSemesterById(id));
    }

    @GetMapping({"/{id}/parcours", "/{id}/parcours/"})
    public ResponseEntity<ParcoursDTO> getParcours(@PathVariable Long id){
        return ResponseEntity.ok(semesterService.getParcours(id));
    }

    @GetMapping("/parcours/{parcoursId}")
    public List<Semester> getSemestersByParcours(@PathVariable Long parcoursId) {
        return semesterService.getSemestersByParcours(parcoursId);
    }
}