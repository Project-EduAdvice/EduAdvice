package com.eduadvice.backend.controller;

import com.eduadvice.backend.dto.ParcoursDTO;
import com.eduadvice.backend.dto.SemesterDTO;
import com.eduadvice.backend.dto.TeachingUnitResponseDTO;
import com.eduadvice.backend.entity.TeachingUnitCategory;
import com.eduadvice.backend.exception.RessourceNotFoundException;
import com.eduadvice.backend.service.SemesterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SemesterController.class)
class SemesterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SemesterService semesterService;

    private SemesterDTO semester1,semester2;
    private ParcoursDTO parcours1;
    private TeachingUnitResponseDTO teachingUnit1, teachingUnit2;

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

        semester1 = new SemesterDTO(
                1L,
                3,
                parcours1
        );

        semester2 = new SemesterDTO(
                2L,
                4,
                parcours1
        );

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
    }

    @Test
    void getAllSemesters_shouldReturn200AndSemestersList() throws Exception {
        when(semesterService.getAllSemesters()).thenReturn(List.of(semester1,semester2));

        mockMvc.perform(get("/api/semesters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].number").value(3))
                .andExpect(jsonPath("$[1].number").value(4))
                .andDo(print());

        verify(semesterService).getAllSemesters();
    }

    @Test
    void getAllSemesters_shouldReturnEmptyList_whenNoSemestersExist() throws Exception {

        when(semesterService.getAllSemesters()).thenReturn(List.of());

        mockMvc.perform(get("/api/semesters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
                .andDo(print());

        verify(semesterService).getAllSemesters();
    }

    @Test
    void ShouldReturn404_whenPathDoesNotExist() throws Exception {

        mockMvc.perform(get("/api/semes"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void getSemesterById_shouldReturnSemesterById_whenExists() throws Exception {
        when(semesterService.getSemesterById(1L)).thenReturn(semester1);

        mockMvc.perform(get("/api/semesters/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(3))
                .andExpect(jsonPath("$.parcours.id").value(1))
                .andExpect(jsonPath("$.parcours.name").value("EI2D"))
                .andDo(print());

        verify(semesterService).getSemesterById(1L);
    }


    @Test
    void getSemesterById_shouldReturn404_whenNotExists() throws Exception {
        when(semesterService.getSemesterById(99L)).thenThrow(new RessourceNotFoundException("Semester not found"));

        mockMvc.perform(get("/api/semesters/99"))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(semesterService).getSemesterById(99L);
    }

    @Test
    void getParcours_shouldReturnParcours() throws Exception {
        when(semesterService.getParcours(1L)).thenReturn(parcours1);

        mockMvc.perform(get("/api/semesters/1/parcours"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("EI2D"))
                .andDo(print());

        verify(semesterService).getParcours(1L);

    }


    @Test
    void getTeachingUnits_shouldReturnTeachingUnitsList() throws Exception {
        when(semesterService.getTeachingUnits(1L))
                .thenReturn(List.of(teachingUnit1,teachingUnit2));

        mockMvc.perform(get("/api/semesters/1/teaching-units"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].semesterId").value(1))
                .andExpect(jsonPath("$[0].name").value("Majeure I : Apprentissage Artificiel et Science des Données"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].semesterId").value(1))
                .andExpect(jsonPath("$[1].name").value("Majeure II : Ingénierie des Données et Informatique Décisionnelle"))
                .andDo(print());

        verify(semesterService).getTeachingUnits(1L);
    }

    @Test
    void getTeachingUnits_shouldReturnEmptyTeachingUnitsList_whenNoTeachingUnitsExist() throws Exception {
        when(semesterService.getTeachingUnits(2L)).thenReturn(List.of());

        mockMvc.perform(get("/api/semesters/2/teaching-units"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
                .andDo(print());

        verify(semesterService).getTeachingUnits(2L);
    }
}