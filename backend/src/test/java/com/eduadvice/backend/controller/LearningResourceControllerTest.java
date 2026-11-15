
package com.eduadvice.backend.controller;

import com.eduadvice.backend.dto.LearningResourceRequestDTO;
import com.eduadvice.backend.dto.LearningResourceResponseDTO;
import com.eduadvice.backend.entity.ResourceType;
import com.eduadvice.backend.exception.DuplicateResourceException;
import com.eduadvice.backend.exception.RessourceNotFoundException;
import com.eduadvice.backend.service.LearningResourceService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LearningResourceController.class)
class LearningResourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LearningResourceService learningResourceService;

    private LearningResourceResponseDTO resource1;
    private LearningResourceResponseDTO resource2;

    @BeforeEach
    void setUp() {
        resource1 = new LearningResourceResponseDTO(
                1L,
                10L,
                "Java Documentation",
                "https://docs.oracle.com/en/java/",
                "Official Java documentation",
                ResourceType.DOCUMENT
        );

        resource2 = new LearningResourceResponseDTO(
                2L,
                10L,
                "Spring Boot Tutorial",
                "https://spring.io/guides",
                "Spring Boot learning guides",
                ResourceType.WEBSITE
        );
    }

    @Test
    void getResources_shouldReturn200AndResourceList() throws Exception {

        when(learningResourceService.getResourcesBySubject(10L))
                .thenReturn(List.of(resource1, resource2));

        mockMvc.perform(get("/api/subjects/10/learning-resources"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Java Documentation"))
                .andExpect(jsonPath("$[1].title").value("Spring Boot Tutorial"))
                .andDo(print());

        verify(learningResourceService).getResourcesBySubject(10L);
    }

    @Test
    void getResources_shouldReturnEmptyList_whenNoResourcesExist() throws Exception {

        when(learningResourceService.getResourcesBySubject(10L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/subjects/10/learning-resources"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
                .andDo(print());

        verify(learningResourceService).getResourcesBySubject(10L);
    }

    @Test
    void getResources_shouldReturn404_whenSubjectDoesNotExist() throws Exception {

        when(learningResourceService.getResourcesBySubject(99L))
                .thenThrow(new RessourceNotFoundException("Subject not found"));

        mockMvc.perform(get("/api/subjects/99/learning-resources"))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(learningResourceService).getResourcesBySubject(99L);
    }

    @Test
    void createResource_shouldReturn201_whenResourceIsCreated() throws Exception {

        when(learningResourceService.createResource(
                eq(10L), any(LearningResourceRequestDTO.class)))
                .thenReturn(resource1);

        String requestBody = """
                {
                    "title": "Java Documentation",
                    "url": "https://docs.oracle.com/en/java/",
                    "description": "Official Java documentation",
                    "resourceType": "DOCUMENT"
                }
                """;

        mockMvc.perform(post("/api/subjects/10/learning-resources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.subjectId").value(10))
                .andExpect(jsonPath("$.title").value("Java Documentation"))
                .andExpect(jsonPath("$.resourceType").value("DOCUMENT"))
                .andDo(print());

        verify(learningResourceService).createResource(
                eq(10L), any(LearningResourceRequestDTO.class));
    }

    @Test
    void createResource_shouldReturn404_whenSubjectDoesNotExist() throws Exception {

        when(learningResourceService.createResource(
                eq(99L), any(LearningResourceRequestDTO.class)))
                .thenThrow(new RessourceNotFoundException("Subject not found"));

        String requestBody = """
                {
                    "title": "Java Documentation",
                    "url": "https://docs.oracle.com/en/java/",
                    "description": "Official Java documentation",
                    "resourceType": "DOCUMENT"
                }
                """;

        mockMvc.perform(post("/api/subjects/99/learning-resources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(learningResourceService).createResource(
                eq(99L), any(LearningResourceRequestDTO.class));
    }

    @Test
    void createResource_shouldReturn409_whenResourceAlreadyExists() throws Exception {

        when(learningResourceService.createResource(
                eq(10L), any(LearningResourceRequestDTO.class)))
                .thenThrow(new DuplicateResourceException("Resource already exists"));

        String requestBody = """
                {
                    "title": "Java Documentation",
                    "url": "https://docs.oracle.com/en/java/",
                    "description": "Official Java documentation",
                    "resourceType": "DOCUMENT"
                }
                """;

        mockMvc.perform(post("/api/subjects/10/learning-resources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andDo(print());

        verify(learningResourceService).createResource(
                eq(10L), any(LearningResourceRequestDTO.class));
    }
}
