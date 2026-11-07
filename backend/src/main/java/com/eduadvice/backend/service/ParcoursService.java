package com.eduadvice.backend.service;

import com.eduadvice.backend.entity.Parcours;
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

    public Optional<Parcours> getParcoursById(Long id){
        return parcoursRepository.findById(id);
    }
}
