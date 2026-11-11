package com.eduadvice.backend.controller;

import com.eduadvice.backend.dto.CareerOpportunityDTO;
import com.eduadvice.backend.dto.ParcoursDTO;
import com.eduadvice.backend.exception.RessourceNotFoundException;
import com.eduadvice.backend.service.CareerOpportunityService;
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

@WebMvcTest(CareerOpportunityController.class)
class CareerOpportunityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CareerOpportunityService careerService;

    private CareerOpportunityDTO career1, career2;
    private ParcoursDTO parcours1;

    @BeforeEach
    void setUp() {
        career1 = new CareerOpportunityDTO(
                1L,
                "Software Engineer",
                "Software Development",
                "Designs, develops and maintains software applications.",
                "Java, OOP, Spring Boot"
        );

        career2 = new CareerOpportunityDTO(
                2L,
                "Data Scientist",
                "Data",
                "Develops statistical models and machine learning algorithms to extract insights and make predictions.",
                "Python, Machine Learning, Statistics"
        );

        parcours1 = new ParcoursDTO(
                2L,
                "P2S",
                "Master 2",
                "Master Informatique",
                "Informatique",
                "Université Sorbonne Paris Nord",
                "Programmation, Sûreté et Sécurité."
        );
    }

    @Test
    void getAllCareer_shouldReturn200AndCareerList() throws Exception {
        when(careerService.getAllCareerOpportunities()).thenReturn(List.of(career1,career2));

        ResultActions response = mockMvc.perform(get("/api/career-opportunities"));
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Software Engineer"))
                .andExpect(jsonPath("$[1].title").value("Data Scientist"))
                .andDo(print());

        verify(careerService).getAllCareerOpportunities();
    }

    @Test
    void getAllCareer_shouldReturn404_whenPathDoesNotExist() throws Exception {

        mockMvc.perform(get("/api/career-"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void getCareerById_shouldReturnCareer_whenExists() throws Exception {
        when(careerService.getCareerOpportunityById(1L)).thenReturn(career1);

        ResultActions response = mockMvc.perform(get("/api/career-opportunities/1"));
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Software Engineer"))
                .andExpect(jsonPath("$.sector").value("Software Development"))
                .andDo(print());

        verify(careerService).getCareerOpportunityById(1L);
    }

    @Test
    void getCareerById_shouldReturn404_whenNotExists() throws Exception {
        when(careerService.getCareerOpportunityById(99L))
                .thenThrow(new RessourceNotFoundException("Career opportunity not found"));

        mockMvc.perform(get("/api/career-opportunities/99"))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(careerService).getCareerOpportunityById(99L);
    }

    @Test
    void getParcoursList_shouldReturnParcoursList() throws Exception {
        when(careerService.getParcoursList(1L)).thenReturn(List.of(parcours1));

        mockMvc.perform(get("/api/career-opportunities/1/parcours"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("P2S"))
                .andExpect(jsonPath("$[0].level").value("Master 2"))
                .andDo(print());

        verify(careerService).getParcoursList(1L);
    }

    @Test
    void getParcoursList_shouldReturnEmptyList_whenNoParcoursExist() throws Exception {
        when(careerService.getParcoursList(2L)).thenReturn(List.of());

        mockMvc.perform(get("/api/career-opportunities/2/parcours"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
                .andDo(print());

        verify(careerService).getParcoursList(2L);
    }
}