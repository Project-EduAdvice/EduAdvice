
package com.eduadvice.backend.service;

import com.eduadvice.backend.dto.LearningResourceRequestDTO;
import com.eduadvice.backend.dto.LearningResourceResponseDTO;
import com.eduadvice.backend.dto.SubjectResponseDTO;
import com.eduadvice.backend.entity.*;
import com.eduadvice.backend.exception.DuplicateResourceException;
import com.eduadvice.backend.exception.RessourceNotFoundException;
import com.eduadvice.backend.repository.LearningResourceRepository;
import com.eduadvice.backend.repository.SubjectRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.util.InvalidUrlException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LearningResourceServiceTest {

    @Mock
    private LearningResourceRepository learningResourceRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private UrlValidator urlValidator;

    @InjectMocks
    private LearningResourceService learningResourceService;

    private Parcours parcours;
    private Semester semester;
    private TeachingUnit teachingUnit;
    private Subject subject;
    private LearningResource learningResource,learningResource2;

    private LearningResourceRequestDTO request;

    @BeforeEach
    void setUp() {
        // Parcours
        parcours = new Parcours(
                "EI2D",
                "Master 2",
                "Master Informatique",
                "Informatique",
                "Université Sorbonne Paris Nord",
                "Exploration Informatique des Données et Décisionnel."
        );

        // Semester
        semester = new Semester(3, parcours);

        // TeachingUnit
        teachingUnit = new TeachingUnit(
                semester,
                "Majeure I : Apprentissage Artificiel et Science des Données",
                TeachingUnitCategory.MAJOR_1,
                new BigDecimal("12")
        );

        // Subjects
        subject = new Subject(
                teachingUnit,
                "Apprentissage statistique",
                "Étude des méthodes statistiques permettant aux systèmes informatiques d'apprendre à partir de données.",
                "Apprentissage supervisé, régression, classification, estimation, généralisation et évaluation des modèles.",
                new BigDecimal("3")
        );

        learningResource = new LearningResource(
                subject,
                "Statistical Rethinking",
                "https://civil.colorado.edu/~balajir/CVEN6833/bayes-resources/RM-StatRethink-Bayes.pdf",
                "Book on statistics",
                ResourceType.DOCUMENT
        );

        learningResource2 = new LearningResource(
                subject,
                "The Elements of Statistical Learning",
                "https://www.sas.upenn.edu/~fdiebold/NoHesitations/BookAdvanced.pdf",
                "La « bible » de l'apprentissage statistique",
                ResourceType.DOCUMENT
        );

        request = new LearningResourceRequestDTO(
                1L,
                "Java Documentation",
                "https://docs.oracle.com/en/java/",
                "Official Java documentation",
                ResourceType.DOCUMENT
        );

        parcours.setSemesters(List.of(semester));
        semester.setParcours(parcours);

        semester.setTeachingUnits(List.of(teachingUnit));
        teachingUnit.setSemester(semester);

        teachingUnit.setSubjects(List.of(subject));
        subject.setTeachingUnit(teachingUnit);

        subject.setLearningResourceList(List.of(learningResource,learningResource2));
    }

    @Test
    void getAllResources_shouldReturnAllResources() {
        when(learningResourceRepository.findAll())
                .thenReturn(List.of(learningResource,learningResource2));

        List<LearningResourceResponseDTO> result = learningResourceService.getAllResources();


        assertThat(result).hasSize(2);
        assertThat(result.get(0).title()).isEqualTo("Statistical Rethinking");
        assertThat(result.get(0).url()).isEqualTo("https://civil.colorado.edu/~balajir/CVEN6833/bayes-resources/RM-StatRethink-Bayes.pdf");
        assertThat(result.get(1).title()).isEqualTo("The Elements of Statistical Learning");
        assertThat(result.get(1).resourceType()).isEqualTo(ResourceType.DOCUMENT);

        verify(learningResourceRepository).findAll();
    }

    @Test
    void getAllResources_shouldReturnEmptyList_whenNoResourcesExist() {
        when(learningResourceRepository.findAll()).thenReturn(List.of());

        List<LearningResourceResponseDTO> result = learningResourceService.getAllResources();

        assertTrue(result.isEmpty());

        verify(learningResourceRepository).findAll();
    }


    @Test
    void getResourceById_shouldReturnResource_whenExists() {
        when(learningResourceRepository.findById(1L)).thenReturn(Optional.of(learningResource));

        LearningResourceResponseDTO result = learningResourceService.getResourceById(1L);

        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo("Statistical Rethinking");
        assertThat(result.url()).isEqualTo("https://civil.colorado.edu/~balajir/CVEN6833/bayes-resources/RM-StatRethink-Bayes.pdf");
        assertThat(result.resourceType()).isEqualTo(ResourceType.DOCUMENT);

        verify(learningResourceRepository).findById(1L);
    }

    @Test
    void getResourceById_shouldThrowException_whenNotExists() {
        when(learningResourceRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> learningResourceService.getResourceById(99L)).isInstanceOf(RessourceNotFoundException.class);

        verify(learningResourceRepository).findById(99L);
    }

    @Test
    void getSubject_shouldReturnSubject_whenResourceExists() {
        when(learningResourceRepository.findById(1L)).thenReturn(Optional.of(learningResource));

        SubjectResponseDTO result = learningResourceService.getSubject(1L);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo(subject.getName());
        assertThat(result.content()).isEqualTo(subject.getContent());
        assertThat(result.description()).isEqualTo(subject.getDescription());

        verify(learningResourceRepository).findById(1L);
    }

    @Test
    void getSubject_shouldThrowException_whenResourceNotExists() {
        when(learningResourceRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> learningResourceService.getResourceById(99L)).isInstanceOf(RessourceNotFoundException.class);

        verify(learningResourceRepository).findById(99L);
    }

    @Test
    void createResource_shouldReturnResourceDTO_whenSuccessful() {

        when(subjectRepository.findById(1L))
                .thenReturn(Optional.of(subject));

        when(learningResourceRepository
                .existsBySubjectIdAndUrl(1L, request.url()))
                .thenReturn(false);

        when(learningResourceRepository.save(any(LearningResource.class)))
                .thenAnswer(invocation -> {
                    LearningResource resource = invocation.getArgument(0);
                    resource.setId(10L);
                    return resource;
                });

        LearningResourceResponseDTO response =
                learningResourceService.createResource(request);

        assertNotNull(response);
        assertEquals(10L, response.id());
        assertEquals("Java Documentation", response.title());
        assertEquals(request.url(), response.url());
        assertEquals("Official Java documentation", response.description());
        assertEquals(ResourceType.DOCUMENT, response.resourceType());

        verify(subjectRepository).findById(1L);
        verify(urlValidator).validate(request.url());
        verify(learningResourceRepository)
                .existsBySubjectIdAndUrl(1L, request.url());
        verify(learningResourceRepository).save(any(LearningResource.class));
    }

    @Test
    void createResource_shouldThrowException_whenSubjectNotFound() {
        when(subjectRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> learningResourceService.createResource(request)).isInstanceOf(RessourceNotFoundException.class);

        verify(subjectRepository).findById(1L);

        verifyNoInteractions(urlValidator, learningResourceRepository);
    }

    @Test
    void createResource_shouldThrowException_whenUrlAlreadyExists() {
        when(subjectRepository.findById(1L))
                .thenReturn(Optional.of(subject));

        when(learningResourceRepository
                .existsBySubjectIdAndUrl(1L, request.url()))
                .thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> learningResourceService.createResource(request)
        );

        verify(urlValidator).validate(request.url());

        verify(learningResourceRepository)
                .existsBySubjectIdAndUrl(1L, request.url());

        verify(learningResourceRepository, never())
                .save(any(LearningResource.class));
    }

    @Test
    void createResource_shouldThrowException_whenUrlIsInvalid() {
        when(subjectRepository.findById(1L))
                .thenReturn(Optional.of(subject));

        doThrow(new InvalidUrlException("Invalid URL"))
                .when(urlValidator).validate(request.url());

        assertThrows(
                InvalidUrlException.class,
                () -> learningResourceService.createResource(request)
        );

        verify(learningResourceRepository, never())
                .existsBySubjectIdAndUrl(anyLong(), anyString());

        verify(learningResourceRepository, never())
                .save(any(LearningResource.class));
    }
}

