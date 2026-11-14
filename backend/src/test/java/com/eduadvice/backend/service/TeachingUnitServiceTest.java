package com.eduadvice.backend.service;

import com.eduadvice.backend.dto.SemesterDTO;
import com.eduadvice.backend.dto.SubjectResponseDTO;
import com.eduadvice.backend.dto.TeachingUnitResponseDTO;
import com.eduadvice.backend.entity.*;
import com.eduadvice.backend.exception.RessourceNotFoundException;
import com.eduadvice.backend.repository.TeachingUnitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeachingUnitServiceTest {

    @Mock
    private TeachingUnitRepository teachingUnitRepository;

    @InjectMocks
    private TeachingUnitService teachingUnitService;

    private Parcours parcours1;
    private TeachingUnit teachingUnit1, teachingUnit2;
    private Semester semester1;
    private Subject subject1, subject2;

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

        semester1 = new Semester(3,parcours1);

        teachingUnit1 = new TeachingUnit(
                semester1,
                "Majeure I : Apprentissage Artificiel et Science des Données",
                TeachingUnitCategory.MAJOR_1,
                new BigDecimal("12")
        );

        teachingUnit2 = new TeachingUnit(
                semester1,
                "Majeure II : Ingénierie des Données et Informatique Décisionnelle",
                TeachingUnitCategory.MAJOR_2,
                new BigDecimal("12")
        );

        subject1 = new Subject(
                teachingUnit1,
                "Apprentissage statistique",
                "Étude des méthodes statistiques permettant aux systèmes informatiques d'apprendre à partir de données.",
                "Apprentissage supervisé, régression, classification, estimation, généralisation et évaluation des modèles.",
                new BigDecimal("3")
        );

        subject2 = new Subject(
                teachingUnit1,
                "Deep Learning",
                "Étude des réseaux de neurones profonds utilisés pour apprendre des représentations complexes à partir de données.",
                "Réseaux de neurones, rétropropagation, fonctions d'activation, optimisation, réseaux convolutifs et architectures profondes.",
                new BigDecimal("3")
        );

        semester1.setParcours(parcours1);
        semester1.setTeachingUnits(List.of(teachingUnit1,teachingUnit2));
        teachingUnit1.setSemester(semester1);
        teachingUnit2.setSemester(semester1);
        teachingUnit1.setSubjects(List.of(subject1,subject2));
        teachingUnit2.setSubjects(List.of());
    }

    @Test
    void getAllTeachingUnits_shouldReturnTeachingUnits_whenExists() {
        when(teachingUnitRepository.findAll())
                .thenReturn(List.of(teachingUnit1,teachingUnit2));

        List<TeachingUnitResponseDTO> result = teachingUnitService.getAllTeachingUnits();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertEquals("Majeure I : Apprentissage Artificiel et Science des Données", result.get(0).name());
        assertEquals("Majeure II : Ingénierie des Données et Informatique Décisionnelle", result.get(1).name());

        verify(teachingUnitRepository).findAll();
    }

    @Test
    void getAllTeachingUnits_shouldReturnEmptyList_whenNoTeachingUnitsExist() {
        when(teachingUnitRepository.findAll()).thenReturn(List.of());

        List<TeachingUnitResponseDTO> result = teachingUnitService.getAllTeachingUnits();

        assertTrue(result.isEmpty());

        verify(teachingUnitRepository).findAll();
    }

    @Test
    void getTeachingUnitById_shouldReturnTeachingUnit_whenExists() {
        when(teachingUnitRepository.findById(1L)).thenReturn(Optional.of(teachingUnit1));

        TeachingUnitResponseDTO result = teachingUnitService.getTeachingUnitById(1L);
        assertThat(result).isNotNull();
        assertThat(result).extracting(TeachingUnitResponseDTO::name).isEqualTo("Majeure I : Apprentissage Artificiel et Science des Données");

        verify(teachingUnitRepository).findById(1L);
    }

    @Test
    void getTeachingUnitById_shouldThrowException_whenNotFound() {
        when(teachingUnitRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> teachingUnitService.getTeachingUnitById(99L)).isInstanceOf(RessourceNotFoundException.class);

        verify(teachingUnitRepository).findById(99L);
    }

    @Test
    void getSemester_shouldReturnSemester_whenExists() {
        when(teachingUnitRepository.findById(1L)).thenReturn(Optional.of(teachingUnit1));

        SemesterDTO result = teachingUnitService.getSemester(1L);

        assertThat(result).isNotNull();
        assertThat(result).extracting(SemesterDTO::number).isEqualTo(3);

        verify(teachingUnitRepository).findById(1L);
    }

    @Test
    void getSemester_shouldThrowException_whenTeachingUnitNotFound() {
        when(teachingUnitRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> teachingUnitService.getSemester(99L)).isInstanceOf(RessourceNotFoundException.class);

        verify(teachingUnitRepository).findById(99L);
    }

    @Test
    void getSubjects_shouldReturnSubjects_whenExists() {
        when(teachingUnitRepository.findById(1L)).thenReturn(Optional.of(teachingUnit1));

        List<SubjectResponseDTO> result = teachingUnitService.getSubjects(1L);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(SubjectResponseDTO::name).containsExactly("Apprentissage statistique","Deep Learning");

        verify(teachingUnitRepository).findById(1L);
    }

    @Test
    void getSubjects_shouldReturnEmptyList_whenNoSubjectsExists() {
        when(teachingUnitRepository.findById(2L)).thenReturn(Optional.of(teachingUnit2));

        List<SubjectResponseDTO> result = teachingUnitService.getSubjects(2L);

        assertThat(result).isEmpty();

        verify(teachingUnitRepository).findById(2L);
    }

    @Test
    void getSubjects_shouldThrowException_whenTeachingUnitNotFound() {
        when(teachingUnitRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> teachingUnitService.getSubjects(99L)).isInstanceOf(RessourceNotFoundException.class);

        verify(teachingUnitRepository).findById(99L);
    }
}