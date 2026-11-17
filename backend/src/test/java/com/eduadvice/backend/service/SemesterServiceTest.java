package com.eduadvice.backend.service;

import com.eduadvice.backend.dto.ParcoursDTO;
import com.eduadvice.backend.dto.SemesterDTO;
import com.eduadvice.backend.dto.TeachingUnitResponseDTO;
import com.eduadvice.backend.entity.Parcours;
import com.eduadvice.backend.entity.Semester;
import com.eduadvice.backend.entity.TeachingUnit;
import com.eduadvice.backend.entity.TeachingUnitCategory;
import com.eduadvice.backend.exception.RessourceNotFoundException;
import com.eduadvice.backend.repository.SemesterRepository;
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
class SemesterServiceTest {

    @Mock
    private SemesterRepository semesterRepository;

    @InjectMocks
    private SemesterService semesterService;

    private Semester semester1,semester2;
    private Parcours parcours1;
    private TeachingUnit teachingUnit1, teachingUnit2;


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

        semester1 = new Semester(3, parcours1);
        semester2 = new Semester(4,parcours1);

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

        semester1.setParcours(parcours1);
        semester2.setParcours(parcours1);
        teachingUnit1.setSemester(semester1);
        teachingUnit2.setSemester(semester1);
        semester1.setTeachingUnits(List.of(teachingUnit1,teachingUnit2));
    }

    @Test
    void getAllSemesters_shouldReturnSemesters_whenExists() {
        when(semesterRepository.findAll()).thenReturn(List.of(semester1,semester2));

        List<SemesterDTO> result = semesterService.getAllSemesters();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).extracting(SemesterDTO::number).containsExactly(3,4);

        verify(semesterRepository).findAll();
    }

    @Test
    void getAllSemesters_shouldReturnEmptyList_whenNoSemestersExists() {
        when(semesterRepository.findAll()).thenReturn(List.of());

        List<SemesterDTO> result = semesterService.getAllSemesters();

        assertThat(result).isEmpty();

        verify(semesterRepository).findAll();
    }

    @Test
    void getSemesterById_shouldReturnSemester_whenExists() {
        when(semesterRepository.findById(1L)).thenReturn(Optional.of(semester1));

        SemesterDTO result = semesterService.getSemesterById(1L);

        assertThat(result).isNotNull();
        assertThat(result).extracting(SemesterDTO::number).isEqualTo(3);
        assertEquals("EI2D", result.parcours().name());

        verify(semesterRepository).findById(1L);
    }

    @Test
    void getSemesterById_ShouldThrownException_WhenNotFound() {
        when(semesterRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> semesterService.getSemesterById(99L)).isInstanceOf(RessourceNotFoundException.class);

        verify(semesterRepository).findById(99L);
    }

    @Test
    void getParcours_shouldReturnParcours_whenExists() {
        when(semesterRepository.findById(2L)).thenReturn(Optional.of(semester2));

        ParcoursDTO result = semesterService.getParcours(2L);

        assertThat(result).isNotNull();
        assertThat(result).extracting(ParcoursDTO::name).isEqualTo("EI2D");

        verify(semesterRepository).findById(2L);
    }

    @Test
    void getParcours_shouldThrowException_whenSemestersNotFound() {
        when(semesterRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> semesterService.getParcours(99L)).isInstanceOf(RessourceNotFoundException.class);

        verify(semesterRepository).findById(99L);
    }

    @Test
    void getTeachingUnits_shouldReturnTeachingUnits_whenExists() {
        when(semesterRepository.findById(1L)).thenReturn(Optional.of(semester1));

        List<TeachingUnitResponseDTO> result = semesterService.getTeachingUnits(1L);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(TeachingUnitResponseDTO::name).containsExactly("Majeure I : Apprentissage Artificiel et Science des Données","Majeure II : Ingénierie des Données et Informatique Décisionnelle");

        verify(semesterRepository).findById(1L);

    }

    @Test
    void getTeachingUnits_shouldReturnEmptyList_whenNoTeachingUnitsExists() {
        when(semesterRepository.findById(2L)).thenReturn(Optional.of(semester2));

        List<TeachingUnitResponseDTO> result = semesterService.getTeachingUnits(2L);

        assertThat(result).isEmpty();

        verify(semesterRepository).findById(2L);
    }

    @Test
    void getTeachingUnits_shouldThrowException_whenSemestersNotFound() {
        when(semesterRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> semesterService.getTeachingUnits(99L)).isInstanceOf(RessourceNotFoundException.class);

        verify(semesterRepository).findById(99L);
    }
}