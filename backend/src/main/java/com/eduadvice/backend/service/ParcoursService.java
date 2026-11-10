package com.eduadvice.backend.service;

import com.eduadvice.backend.dto.ParcoursResponseDTO;
import com.eduadvice.backend.entity.CareerOpportunity;
import com.eduadvice.backend.entity.Parcours;
import com.eduadvice.backend.exception.RessourceNotFoundException;
import com.eduadvice.backend.repository.ParcoursRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ParcoursService {

    @Autowired
    ParcoursRepository parcoursRepository;

    private ParcoursResponseDTO toDTO(Parcours parcours) {
        ParcoursResponseDTO dto = new ParcoursResponseDTO(
                parcours.getId(),
                parcours.getName(),
                parcours.getLevel(),
                parcours.getDiploma(),
                parcours.getMention(),
                parcours.getEstablishment(),
                parcours.getDescription(),
                parcours.getCareerOpportunities()
                        .stream()
                        .map(CareerOpportunity::getId)
                        .collect(Collectors.toSet())
                );

        return dto;
    }

    public List<ParcoursResponseDTO> getAllParcours(){
        return parcoursRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public ParcoursResponseDTO getParcoursById(Long id){

        Optional<Parcours> parcours = parcoursRepository.findById(id);

        if(parcours.isEmpty()) {
            throw new RessourceNotFoundException(" Le parcours avec l'identifiant " + id + " n'existe pas.");
        }

        return toDTO(parcours.get());
    }
}
