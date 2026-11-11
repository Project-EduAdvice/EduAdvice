package com.eduadvice.backend.service;

import com.eduadvice.backend.dto.CareerOpportunityDTO;
import com.eduadvice.backend.entity.CareerOpportunity;
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

    @BeforeEach
    void setUp(){
        career1 = new CareerOpportunity(
                "Software Engineer",
                "Software Development",
                "Designs, develops and maintains software applications.",
                "Java, OOP, Spring Boot"
        );

        career2 = new CareerOpportunity(
                "Data Scientist",
                "Data",
                "Develops statistical models and machine learning algorithms to extract insights and make predictions.",
                "Python, Machine Learning, Statistics"
        );
    }

    @Test
    void getAllCareer_shouldReturnCareer_whenExists(){
        when(careerRepository.findAll()).thenReturn(List.of(career1,career2));

        List<CareerOpportunityDTO> result = careerService.getAllCareerOpportunities();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).extracting(CareerOpportunityDTO::title).containsExactly("Software Engineer","Data Scientist");

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
        assertThat(result).extracting(CareerOpportunityDTO::title).isEqualTo("Software Engineer");
        assertThat(result).extracting(CareerOpportunityDTO::sector).isEqualTo("Software Development");

        verify(careerRepository).findById(1L);
    }

    @Test
    void getCareerById_ShouldThrownException_WhenNotFound(){
        when(careerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> careerService.getCareerOpportunityById(99L)).isInstanceOf(RessourceNotFoundException.class);

        verify(careerRepository).findById(99L);
    }
}