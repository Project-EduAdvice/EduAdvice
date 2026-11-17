package com.eduadvice.backend.controller;

import com.eduadvice.backend.dto.ParcoursDTO;
import com.eduadvice.backend.dto.SemesterDTO;
import com.eduadvice.backend.dto.SubjectResponseDTO;
import com.eduadvice.backend.dto.TeachingUnitResponseDTO;
import com.eduadvice.backend.entity.*;
import com.eduadvice.backend.exception.RessourceNotFoundException;
import com.eduadvice.backend.service.TeachingUnitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TeachingUnitController.class)
class TeachingUnitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TeachingUnitService teachingUnitService;

    private ParcoursDTO parcours1;
    private TeachingUnitResponseDTO teachingUnit1, teachingUnit2;
    private SemesterDTO semester1;
    private SubjectResponseDTO subject1, subject2;

    @BeforeEach
    void setUp() {
        parcours1 = new ParcoursDTO(
                1L,
                "EI2D",
                "Master 2",
                "Master Informatique",
                "Informatique",
                "Université Sorbonne Paris Nord",
                "Exploration Informatique des Données et Décisionnel."
        );

        semester1 = new SemesterDTO(1L,3,parcours1);

        teachingUnit1 = new TeachingUnitResponseDTO(
                1L,
                semester1.id(),
                "Majeure I : Apprentissage Artificiel et Science des Données",
                TeachingUnitCategory.MAJOR_1,
                new BigDecimal("12")
        );

        teachingUnit2 = new TeachingUnitResponseDTO(
                2L,
                semester1.id(),
                "Majeure II : Ingénierie des Données et Informatique Décisionnelle",
                TeachingUnitCategory.MAJOR_2,
                new BigDecimal("12")
        );

        subject1 = new SubjectResponseDTO(
                1L,
                teachingUnit1.id(),
                "Apprentissage statistique",
                "Étude des méthodes statistiques permettant aux systèmes informatiques d'apprendre à partir de données.",
                "Apprentissage supervisé, régression, classification, estimation, généralisation et évaluation des modèles.",
                new BigDecimal("3")
        );

        subject2 = new SubjectResponseDTO(
                2L,
                teachingUnit1.id(),
                "Deep Learning",
                "Étude des réseaux de neurones profonds utilisés pour apprendre des représentations complexes à partir de données.",
                "Réseaux de neurones, rétropropagation, fonctions d'activation, optimisation, réseaux convolutifs et architectures profondes.",
                new BigDecimal("3")
        );
    }

    @Test
    void getAllTeachingUnits_shouldReturn200AndTeachingUnits() throws Exception {
        when(teachingUnitService.getAllTeachingUnits()).thenReturn(List.of(teachingUnit1,teachingUnit2));

        mockMvc.perform(get("/api/teaching-units"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].semesterId").value(1))
                .andExpect(jsonPath("$[0].name").value("Majeure I : Apprentissage Artificiel et Science des Données"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Majeure II : Ingénierie des Données et Informatique Décisionnelle"))
                .andDo(print());

        verify(teachingUnitService).getAllTeachingUnits();
    }

    @Test
    void getAllTeachingUnits_shouldReturnEmptyList_whenNoTeachingUnitsExist() throws Exception {
        when(teachingUnitService.getAllTeachingUnits()).thenReturn(List.of());

        mockMvc.perform(get("/api/teaching-units"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
                .andDo(print());

        verify(teachingUnitService).getAllTeachingUnits();
    }

    @Test
    void ShouldReturn404_whenPathDoesNotExist() throws Exception {

        mockMvc.perform(get("/api/teaching"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void getTeachingUnitById_shouldReturnTeachingUnit_whenExists() throws Exception {
        when(teachingUnitService.getTeachingUnitById(1L)).thenReturn(teachingUnit1);

        mockMvc.perform(get("/api/teaching-units/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.semesterId").value(1))
                .andExpect(jsonPath("$.name").value("Majeure I : Apprentissage Artificiel et Science des Données"))
                .andExpect(jsonPath("$.category").value("MAJOR_1"))
                .andExpect(jsonPath("$.ects").value(new BigDecimal(12)))
                .andDo(print());

        verify(teachingUnitService).getTeachingUnitById(1L);
    }

    @Test
    void getTeachingUnitById_shouldReturn404_whenNotExists() throws Exception {
        when(teachingUnitService.getTeachingUnitById(99L)).thenThrow(new RessourceNotFoundException("Teaching Units not found"));

        mockMvc.perform(get("/api/teaching-units/99"))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(teachingUnitService).getTeachingUnitById(99L);
    }

    @Test
    void getSemester_shouldReturnSemester() throws Exception {
        when(teachingUnitService.getSemester(1L)).thenReturn(semester1);

        mockMvc.perform(get("/api/teaching-units/1/semester"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.number").value(3))
                .andExpect(jsonPath("$.parcours.id").value(1))
                .andExpect(jsonPath("$.parcours.name").value("EI2D"))
                .andExpect(jsonPath("$.parcours.level").value("Master 2"))
                .andDo(print());

        verify(teachingUnitService).getSemester(1L);
    }


    @Test
    void getSubjectsByTeachingUnit_shouldReturnSubjects() throws Exception {
        when(teachingUnitService.getSubjects(1L))
                .thenReturn(List.of(subject1, subject2));

        mockMvc.perform(get("/api/teaching-units/1/subjects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].teachingUnitId").value(1))
                .andExpect(jsonPath("$[0].name").value("Apprentissage statistique"))
                .andExpect(jsonPath("$[0].ects").value(new BigDecimal(3)))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Deep Learning"))
                .andDo(print());

        verify(teachingUnitService).getSubjects(1L);
    }

    @Test
    void getSubjectsByTeachingUnit_shouldReturnEmptyList_whenNoSubjectsExist() throws Exception {
        when(teachingUnitService.getSubjects(2L)).thenReturn(List.of());

        mockMvc.perform(get("/api/teaching-units/2/subjects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
                .andDo(print());

        verify(teachingUnitService).getSubjects(2L);
    }

}