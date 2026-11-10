package com.eduadvice.backend.controller;

import com.eduadvice.backend.dto.ParcoursResponseDTO;
import com.eduadvice.backend.entity.Parcours;
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
    public ResponseEntity<List<ParcoursResponseDTO>> getAllParcours(){
        return ResponseEntity.ok(parcoursService.getAllParcours());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParcoursResponseDTO> getParcours(@PathVariable Long id){
        return ResponseEntity.ok(parcoursService.getParcoursById(id));
    }
}
