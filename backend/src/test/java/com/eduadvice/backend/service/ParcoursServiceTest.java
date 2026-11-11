package com.eduadvice.backend.service;

import com.eduadvice.backend.dto.CareerOpportunityDTO;
import com.eduadvice.backend.dto.ParcoursDTO;
import com.eduadvice.backend.entity.CareerOpportunity;
import com.eduadvice.backend.entity.Parcours;
import com.eduadvice.backend.exception.RessourceNotFoundException;
import com.eduadvice.backend.repository.ParcoursRepository;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParcoursServiceTest {
    @Mock
    private ParcoursRepository parcoursRepository;

    @InjectMocks
    private ParcoursService parcoursService;

   private Parcours parcours1, parcours2;
    private CareerOpportunity career1, career2;

    @BeforeEach
    void setUp() {
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

        career1 = new CareerOpportunity(
                "Data Scientist",
                "Data",
                "Conception de modèle statistiques et d'algorithmes de Machine Learning.",
                "Python, Machine Learning, Statistics"
        );

        career2 = new CareerOpportunity(
                "Data Engineer",
                "Data",
                "Conception de pipelines de données.",
                "Python, SQL, ETL"
        );

        parcours1.setCareerOpportunities(Set.of(career1,career2));
    }

    @Test
    void getAllParcours_shouldReturnParcours_whenExists() {
        when(parcoursRepository.findAll()).thenReturn(List.of(parcours1, parcours2));

        List<ParcoursDTO> result = parcoursService.getAllParcours();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).extracting(ParcoursDTO::name).containsExactly("EI2D","P2S");

        verify(parcoursRepository).findAll();
    }

    @Test
    void getAllParcours_shouldReturnEmptyList_whenNoParcoursExists() {
        when(parcoursRepository.findAll()).thenReturn(List.of());

        List<ParcoursDTO> result = parcoursService.getAllParcours();

        assertThat(result).isEmpty();

        verify(parcoursRepository).findAll();
    }

    @Test
    void getParcoursById_shouldReturnParcours_whenExists() {
        when(parcoursRepository.findById(1L)).thenReturn(Optional.of(parcours1));

        ParcoursDTO result = parcoursService.getParcoursById(1L);

        assertThat(result).isNotNull();
        assertThat(result).extracting(ParcoursDTO::name).isEqualTo("EI2D");
        assertThat(result).extracting(ParcoursDTO::level).isEqualTo("Master 2");

        verify(parcoursRepository).findById(1L);
    }

    @Test
    void getParcoursById_shouldThrowException_whenNotFound() {
        when(parcoursRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> parcoursService.getParcoursById(99L)).isInstanceOf(RessourceNotFoundException.class);
        verify(parcoursRepository).findById(99L);
    }

    @Test
    void getCareerOpportunities_shouldReturnCareerOpportunities_whenExists() {
        when(parcoursRepository.findById(1L)).thenReturn(Optional.of(parcours1));

        List<CareerOpportunityDTO> result = parcoursService.getCareerOpportunities(1L);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(CareerOpportunityDTO::title).containsExactly("Data Scientist", "Data Engineer");
        assertThat(result.get(0)).extracting(CareerOpportunityDTO::sector).isEqualTo("Data");
        assertThat(result.get(1)).extracting(CareerOpportunityDTO::sector).isEqualTo("Data");
        assertThat(result.get(0)).extracting(CareerOpportunityDTO::required_skills).isEqualTo("Python, Machine Learning, Statistics");

        verify(parcoursRepository).findById(1L);
    }

    @Test
    void getCareerOpportunities_shouldReturnEmptyList_whenNoCareerOpportunitiesExist() {
        when(parcoursRepository.findById(1L)).thenReturn(Optional.of(parcours2));

        List<CareerOpportunityDTO> result = parcoursService.getCareerOpportunities(1L);

        assertThat(result).isEmpty();

        verify(parcoursRepository).findById(1L);
    }

    @Test
    void getCareerOpportunities_shouldThrowException_whenParcoursNotFound() {

        when(parcoursRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> parcoursService.getCareerOpportunities(99L)).isInstanceOf(RessourceNotFoundException.class);

        verify(parcoursRepository).findById(99L);
    }
}