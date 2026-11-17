package com.eduadvice.backend.controller;

import com.eduadvice.backend.dto.CareerOpportunityDTO;
import com.eduadvice.backend.dto.ParcoursDTO;
import com.eduadvice.backend.dto.SemesterDTO;
import com.eduadvice.backend.service.ParcoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/api/parcours", "/api/parcours/"})
public class ParcoursController {
    @Autowired
    ParcoursService parcoursService;

    @GetMapping
    public ResponseEntity<List<ParcoursDTO>> getAllParcours(){
        return ResponseEntity.ok(parcoursService.getAllParcours());
    }

    @GetMapping({"/{id}","/{id}/"})
    public ResponseEntity<ParcoursDTO> getParcours(@PathVariable Long id){
        return ResponseEntity.ok(parcoursService.getParcoursById(id));
    }

    @GetMapping({"/{id}/career-opportunities","/{id}/career-opportunities/"})
    public List<CareerOpportunityDTO> getCareerOpportunities(@PathVariable Long id) {
        return parcoursService.getCareerOpportunities(id);
    }

    @GetMapping({"/{id}/semesters", "/{id}/semesters/"})
    public List<SemesterDTO> getSemesters(@PathVariable Long id){
        return parcoursService.getSemesters(id);
    }
}
