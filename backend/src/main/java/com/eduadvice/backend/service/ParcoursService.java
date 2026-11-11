package com.eduadvice.backend.service;

import com.eduadvice.backend.entity.Parcours;
import com.eduadvice.backend.exception.RessourceNotFoundException;
import com.eduadvice.backend.repository.ParcoursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParcoursService {

    @Autowired
    ParcoursRepository parcoursRepository;

    public List<Parcours> getAllParcours(){
        return parcoursRepository.findAll();
    }

    public Parcours getParcoursById(Long id){

        Optional<Parcours> parcours = parcoursRepository.findById(id);

        if(parcours.isEmpty()) {
            throw new RessourceNotFoundException(" Le parcours avec l'identifiant " + id + " n'existe pas.");
        }

        return parcours.get();
    }
}
