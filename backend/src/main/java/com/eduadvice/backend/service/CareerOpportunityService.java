package com.eduadvice.backend.service;

import com.eduadvice.backend.dto.CareerOpportunityDTO;
import com.eduadvice.backend.entity.CareerOpportunity;
import com.eduadvice.backend.exception.RessourceNotFoundException;
import com.eduadvice.backend.repository.CareerOpportunityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CareerOpportunityService {

    @Autowired
    private CareerOpportunityRepository careerOpportunityRepository;

    private CareerOpportunityDTO toDTO(CareerOpportunity opportunity){
        CareerOpportunityDTO dto = new CareerOpportunityDTO(
                opportunity.getId(),
                opportunity.getTitle(),
                opportunity.getSector(),
                opportunity.getDescription(),
                opportunity.getRequired_skills()
                );

        return dto;
    }

    public List<CareerOpportunityDTO> getAllCareerOpportunities(){
        return careerOpportunityRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public CareerOpportunityDTO getCareerOpportunityById(Long id) {
        Optional<CareerOpportunity> careerOpportunity = careerOpportunityRepository.findById(id);

        if(careerOpportunity.isEmpty()){
            throw new RessourceNotFoundException("L'opportunité de carrière avec l'identifiant " + id + " n'existe pas.");
        }

        return toDTO(careerOpportunity.get());
    }
}
