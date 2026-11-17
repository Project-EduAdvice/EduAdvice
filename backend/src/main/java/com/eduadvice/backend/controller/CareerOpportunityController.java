package com.eduadvice.backend.controller;

import com.eduadvice.backend.dto.CareerOpportunityDTO;
import com.eduadvice.backend.dto.ParcoursDTO;
import com.eduadvice.backend.service.CareerOpportunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/api/career-opportunities", "/api/career-opportunities/"})
public class CareerOpportunityController {
    @Autowired
    CareerOpportunityService careerOpportunityService;

    @GetMapping
    public ResponseEntity<List<CareerOpportunityDTO>> getAllCareerOpportunities() {
        return ResponseEntity.ok(careerOpportunityService.getAllCareerOpportunities());
    }

    @GetMapping({"/{id}", "/{id}/"})
    public ResponseEntity<CareerOpportunityDTO> getCareerOpportunityById(@PathVariable Long id) {
        return ResponseEntity.ok(careerOpportunityService.getCareerOpportunityById(id));
    }

    @GetMapping({"/{id}/parcours", "/{id}/parcours/"})
    public List<ParcoursDTO> getParcoursList(@PathVariable Long id){
        return careerOpportunityService.getParcoursList(id);
    }
}
