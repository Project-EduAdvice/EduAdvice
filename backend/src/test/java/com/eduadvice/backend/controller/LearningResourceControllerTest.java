
package com.eduadvice.backend.controller;

import com.eduadvice.backend.dto.LearningResourceRequestDTO;
import com.eduadvice.backend.dto.LearningResourceResponseDTO;
import com.eduadvice.backend.dto.SubjectResponseDTO;
import com.eduadvice.backend.entity.ResourceType;
import com.eduadvice.backend.exception.RessourceNotFoundException;
import com.eduadvice.backend.service.LearningResourceService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(LearningResourceController.class)
class LearningResourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LearningResourceService learningResourceService;

    private LearningResourceResponseDTO resourceResponse;
    private LearningResourceRequestDTO resourceRequest;
    private SubjectResponseDTO subjectResponse;

    @BeforeEach
    void setUp() {

        resourceResponse = new LearningResourceResponseDTO(
                1L,
                1L,
                "Java documentation",
                "https://docs.oracle.com/javase/",
                "Documentation",
                ResourceType.DOCUMENT
        );

        resourceRequest = new LearningResourceRequestDTO(
                1L,
                "Java documentation",
                "https://docs.oracle.com/javase/",
                "Documentation",
                ResourceType.DOCUMENT
        );

        subjectResponse = new SubjectResponseDTO(
                1L,
                1L,
                "Java",
                "Programming language",
                "POO",
                new BigDecimal("3")
        );
    }

    // GET ALL RESOURCES

    @Test
    void getAllResources_shouldReturn200() throws Exception {

        when(learningResourceService.getAllResources())
                .thenReturn(List.of(resourceResponse));

        mockMvc.perform(get("/api/learning-resources"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Java documentation"));

        verify(learningResourceService).getAllResources();
    }

    @Test
    void getAllResources_shouldReturnEmptyList_whenNoResourcesExist()
            throws Exception {

        when(learningResourceService.getAllResources())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/learning-resources"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(learningResourceService).getAllResources();
    }

    // GET RESOURCE BY ID

    @Test
    void getResourceById_shouldReturn200_whenResourceExists()
            throws Exception {

        when(learningResourceService.getResourceById(1L))
                .thenReturn(resourceResponse);

        mockMvc.perform(get("/api/learning-resources/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Java documentation"));

        verify(learningResourceService).getResourceById(1L);
    }

    @Test
    void getResourceById_shouldReturn404_whenResourceNotFound()
            throws Exception {

        when(learningResourceService.getResourceById(99L))
                .thenThrow(new RessourceNotFoundException(
                        "Learning resource not found"
                ));

        mockMvc.perform(get("/api/learning-resources/99"))
                .andExpect(status().isNotFound());

        verify(learningResourceService).getResourceById(99L);
    }

    // GET SUBJECT OF A RESOURCE

    @Test
    void getSubject_shouldReturn200_whenSubjectExists()
            throws Exception {

        when(learningResourceService.getSubject(1L))
                .thenReturn(subjectResponse);

        mockMvc.perform(get("/api/learning-resources/1/subject"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Java"));

        verify(learningResourceService).getSubject(1L);
    }

    @Test
    void getSubject_shouldReturn404_whenResourceNotFound()
            throws Exception {

        when(learningResourceService.getSubject(99L))
                .thenThrow(new RessourceNotFoundException(
                        "Learning resource not found"
                ));

        mockMvc.perform(get("/api/learning-resources/99/subject"))
                .andExpect(status().isNotFound());

        verify(learningResourceService).getSubject(99L);
    }

    // CREATE RESOURCE

    @Test
    void createResource_shouldReturn201_whenResourceIsCreated()
            throws Exception {

        when(learningResourceService.createResource(any(
                LearningResourceRequestDTO.class
        ))).thenReturn(resourceResponse);

        mockMvc.perform(post("/api/learning-resources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resourceRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Java documentation"));

        verify(learningResourceService).createResource(any(
                LearningResourceRequestDTO.class
        ));
    }

    @Test
    void createResource_shouldReturn404_whenSubjectNotFound()
            throws Exception {

        when(learningResourceService.createResource(any(
                LearningResourceRequestDTO.class
        ))).thenThrow(new RessourceNotFoundException(
                "Subject not found"
        ));

        mockMvc.perform(post("/api/learning-resources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resourceRequest)))
                .andExpect(status().isNotFound());

        verify(learningResourceService).createResource(any(
                LearningResourceRequestDTO.class
        ));
    }
}

