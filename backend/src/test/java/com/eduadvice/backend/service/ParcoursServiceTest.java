package com.eduadvice.backend.service;

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
    }

    @Test
    void getAllParcours_shouldReturnParcours_whenExists() {
        when(parcoursRepository.findAll()).thenReturn(List.of(parcours1, parcours2));

        List<Parcours> result = parcoursService.getAllParcours();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(parcours1,parcours2);
        verify(parcoursRepository).findAll();
    }

    @Test
    void getAllParcours_shouldReturnEmptyList_whenNoParcoursExists() {
        when(parcoursRepository.findAll()).thenReturn(List.of());

        List<Parcours> result = parcoursService.getAllParcours();

        assertThat(result).isEmpty();
        verify(parcoursRepository).findAll();
    }

    @Test
    void getParcoursById_shouldReturnParcours_whenExists() {
        when(parcoursRepository.findById(1L)).thenReturn(Optional.of(parcours1));

        Parcours result = parcoursService.getParcoursById(1L);


        assertThat(result).isSameAs(parcours1);
        verify(parcoursRepository).findById(1L);
    }

    @Test
    void getParcoursById_shouldThrowException_whenNotFound() {
        when(parcoursRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> parcoursService.getParcoursById(99L)).isInstanceOf(RessourceNotFoundException.class);
        verify(parcoursRepository).findById(99L);
    }
}