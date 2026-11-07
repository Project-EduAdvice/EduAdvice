package com.eduadvice.backend.controller;

import com.eduadvice.backend.entity.Parcours;
import com.eduadvice.backend.service.ParcoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/parcours")
public class ParcoursController {
    @Autowired
    ParcoursService parcoursService;

    @GetMapping
    public List<Parcours> getAllParcours(){
        return parcoursService.getAllParcours();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Parcours> getParcours(@PathVariable Long id){
        Optional<Parcours> parcours = parcoursService.getParcoursById(id);

        if(parcours.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(parcours.get());
    }
}
