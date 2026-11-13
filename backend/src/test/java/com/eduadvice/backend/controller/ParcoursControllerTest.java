package com.eduadvice.backend.controller;

import com.eduadvice.backend.dto.CareerOpportunityDTO;
import com.eduadvice.backend.dto.ParcoursDTO;
import com.eduadvice.backend.dto.SemesterDTO;
import com.eduadvice.backend.exception.RessourceNotFoundException;
import com.eduadvice.backend.service.ParcoursService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ParcoursController.class)
class ParcoursControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ParcoursService parcoursService;

    private ParcoursDTO parcours1, parcours2;
    private CareerOpportunityDTO career1;
    private SemesterDTO semester1;

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

        parcours2 = new ParcoursDTO(
                2L,
                "P2S",
                "Master 2",
                "Master Informatique",
                "Informatique",
                "Université Sorbonne Paris Nord",
                "Programmation, Sûreté et Sécurité."
        );

        career1 = new CareerOpportunityDTO(
                1L,
                "Data Scientist",
                "Data",
                "Conception de modèle statistiques et d'algorithmes de Machine Learning.",
                "Python, Machine Learning, Statistics"
        );

        semester1 = new SemesterDTO(
                3,
                parcours1
        );
    }

    @Test
    void getAllParcours_ShouldReturn200AndParcoursList() throws Exception {
        when(parcoursService.getAllParcours()).thenReturn(List.of(parcours1,parcours2));

        ResultActions response = mockMvc.perform(get("/api/parcours"));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("EI2D"))
                .andExpect(jsonPath("$[1].name").value("P2S"))
                .andDo(print());

        verify(parcoursService).getAllParcours();
    }

    @Test
    void getAllParcours_shouldReturn404_whenPathDoesNotExist() throws Exception {

        mockMvc.perform(get("/api/parco"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void getParcoursById_shouldReturnParcours_whenExists() throws Exception {
        when(parcoursService.getParcoursById(1L))
                .thenReturn(parcours1);

        mockMvc.perform(get("/api/parcours/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("EI2D"))
                .andExpect(jsonPath("$.level").value("Master 2"))
                .andExpect(jsonPath("$.diploma").value("Master Informatique"))
                .andDo(print());

        verify(parcoursService).getParcoursById(1L);
    }

    @Test
    void getParcoursById_shouldReturn404_whenNotExists() throws Exception {
        when(parcoursService.getParcoursById(99L)).thenThrow(new RessourceNotFoundException("Parcours not found"));

        mockMvc.perform(get("/api/parcours/99"))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(parcoursService).getParcoursById(99L);
    }

    @Test
    void getCareerOpportunities_shouldReturnCareerOpportunities() throws Exception {
        when(parcoursService.getCareerOpportunities(1L))
                .thenReturn(List.of(career1));

        mockMvc.perform(get("/api/parcours/1/career-opportunities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Data Scientist"))
                .andExpect(jsonPath("$[0].sector").value("Data"))
                .andDo(print());

        verify(parcoursService).getCareerOpportunities(1L);
    }

    @Test
    void getCareerOpportunities_shouldReturnEmptyList_whenNoOpportunitiesExist() throws Exception {
        when(parcoursService.getCareerOpportunities(2L)).thenReturn(List.of());

        mockMvc.perform(get("/api/parcours/2/career-opportunities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
                .andDo(print());

        verify(parcoursService).getCareerOpportunities(2L);
    }

    @Test
    void getSemesters_shouldReturnSemesters() throws Exception {
        when(parcoursService.getSemesters(1L)).thenReturn(List.of(semester1));

        mockMvc.perform(get("/api/parcours/1/semesters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].number").value("3"))
                .andExpect(jsonPath("$[0].parcours.name").value("EI2D"))
                .andDo(print());

        verify(parcoursService).getSemesters(1L);
    }

    @Test
    void getSemesters_shouldReturnEmptyList_whenNoSemestersExist() throws Exception {
        when(parcoursService.getSemesters(2L)).thenReturn(List.of());

        mockMvc.perform(get("/api/parcours/2/semesters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
                .andDo(print());

        verify(parcoursService).getSemesters(2L);
    }
}