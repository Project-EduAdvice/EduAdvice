package com.eduadvice.backend.controller;

import com.eduadvice.backend.entity.Parcours;
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
                .andDo(print());

        verify(parcoursService).getParcoursById(1L);
    }

    @Test
    void getParcoursById_shouldReturn404_whenNotExists() throws Exception {

        when(parcoursService.getParcoursById(99L))
                .thenThrow(new RessourceNotFoundException("Parcours not found"));

        mockMvc.perform(get("/api/parcours/99"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}