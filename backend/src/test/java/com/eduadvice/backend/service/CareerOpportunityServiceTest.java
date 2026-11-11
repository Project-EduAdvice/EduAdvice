package com.eduadvice.backend.service;

import com.eduadvice.backend.dto.CareerOpportunityDTO;
import com.eduadvice.backend.dto.ParcoursDTO;
import com.eduadvice.backend.entity.CareerOpportunity;
import com.eduadvice.backend.entity.Parcours;
import com.eduadvice.backend.exception.RessourceNotFoundException;
import com.eduadvice.backend.repository.CareerOpportunityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CareerOpportunityServiceTest {
    @Mock
    private CareerOpportunityRepository careerRepository;

    @InjectMocks
    private CareerOpportunityService careerService;

    private CareerOpportunity career1, career2;
    private Parcours parcours1, parcours2;

    @BeforeEach
    void setUp(){
        career1 = new CareerOpportunity(
                "Data Engineer",
                "Data Engineering",
                "Designs and maintains data pipelines.",
                "Python, SQL, ETL"
        );

        career2 = new CareerOpportunity(
                "Lawyer",
                "Legal",
                "Provides legal advice and represents clients in court.",
                "Litigation, Contract Law, Negotiation"
        );

        parcours1 = new Parcours(
                "EI2D",
                "Master 2",
                "Master Informatique",
                "Informatique",
                "Université Sorbonne Paris Nord",
                "Exploration Informatique des Données et Décisionnel."
        );

        parcours2 = new Parcours(
                "P2S",
                "Master 2",
                "Master Informatique",
                "Informatique",
                "Université Sorbonne Paris Nord",
                "Programmation, Sûreté et Sécurité."
        );

        career1.setParcours(Set.of(parcours1, parcours2));
    }

    @Test
    void getAllCareer_shouldReturnCareer_whenExists(){
        when(careerRepository.findAll()).thenReturn(List.of(career1,career2));

        List<CareerOpportunityDTO> result = careerService.getAllCareerOpportunities();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).extracting(CareerOpportunityDTO::title).containsExactly("Data Engineer","Lawyer");

        verify(careerRepository).findAll();
    }

    @Test
    void getAllCareer_shouldReturnEmptyList_whenNoCareerExists() {
        when(careerRepository.findAll()).thenReturn(List.of());

        List<CareerOpportunityDTO> result = careerService.getAllCareerOpportunities();

        assertThat(result).isEmpty();

        verify(careerRepository).findAll();
    }

    @Test
    void getCareerById_ShouldReturnCareer_whenExists(){
        when(careerRepository.findById(1L)).thenReturn(Optional.of(career1));

        CareerOpportunityDTO result = careerService.getCareerOpportunityById(1L);

        assertThat(result).isNotNull();
        assertThat(result).extracting(CareerOpportunityDTO::title).isEqualTo("Data Engineer");
        assertThat(result).extracting(CareerOpportunityDTO::sector).isEqualTo("Data Engineering");

        verify(careerRepository).findById(1L);
    }

    @Test
    void getCareerById_ShouldThrownException_WhenNotFound(){
        when(careerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> careerService.getCareerOpportunityById(99L)).isInstanceOf(RessourceNotFoundException.class);

        verify(careerRepository).findById(99L);
    }

    @Test
    void getParcoursList_shouldReturnParcoursList_whenExists(){
        when(careerRepository.findById(1L)).thenReturn(Optional.of(career1));

        List<ParcoursDTO> result = careerService.getParcoursList(1L);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(ParcoursDTO::name).containsExactly("EI2D","P2S");
        assertThat(result).extracting(ParcoursDTO::level).containsExactly("Master 2", "Master 2");

        verify(careerRepository).findById(1L);
    }

    @Test
    void getParcoursList_shouldReturnEmptyList_whenNoParcoursExist(){
        when(careerRepository.findById(2L)).thenReturn(Optional.of(career2));

        List<ParcoursDTO> result = careerService.getParcoursList(2L);

        assertThat(result).isEmpty();

        verify(careerRepository).findById(2L);
    }

    @Test
    void getParcoursList_shouldThrowException_whenCareerNotFound(){
        when(careerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> careerService.getParcoursList(99L)).isInstanceOf(RessourceNotFoundException.class);

        verify(careerRepository).findById(99L);
    }
}