package com.eduadvice.backend.service;

import com.eduadvice.backend.dto.CareerOpportunityResponseDTO;
import com.eduadvice.backend.entity.CareerOpportunity;
import com.eduadvice.backend.entity.Parcours;
import com.eduadvice.backend.exception.RessourceNotFoundException;
import com.eduadvice.backend.repository.CareerOpportunityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CareerOpportunityService {

    @Autowired
    private CareerOpportunityRepository careerOpportunityRepository;

    private CareerOpportunityResponseDTO toDTO(CareerOpportunity opportunity){
        CareerOpportunityResponseDTO dto = new CareerOpportunityResponseDTO(
                opportunity.getId(),
                opportunity.getTitle(),
                opportunity.getSector(),
                opportunity.getDescription(),
                opportunity.getRequired_skills(),
                opportunity.getParcours()
                        .stream()
                        .map(Parcours::getId)
                        .collect(Collectors.toSet())
        );

        return dto;
    }

    public List<CareerOpportunityResponseDTO> getAllCareerOpportunities(){
        return careerOpportunityRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public CareerOpportunityResponseDTO getCareerOpportunityById(Long id) {
        Optional<CareerOpportunity> careerOpportunity = careerOpportunityRepository.findById(id);

        if(careerOpportunity.isEmpty()){
            throw new RessourceNotFoundException("L'opportunité de carrière avec l'identifiant " + id + " n'existe pas.");
        }

        return toDTO(careerOpportunity.get());
    }
}
