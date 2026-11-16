
package com.eduadvice.backend.service;

import com.eduadvice.backend.dto.LearningResourceRequestDTO;
import com.eduadvice.backend.dto.LearningResourceResponseDTO;
import com.eduadvice.backend.entity.LearningResource;
import com.eduadvice.backend.entity.ResourceType;
import com.eduadvice.backend.entity.Subject;
import com.eduadvice.backend.exception.DuplicateResourceException;
import com.eduadvice.backend.exception.RessourceNotFoundException;
import com.eduadvice.backend.repository.LearningResourceRepository;
import com.eduadvice.backend.repository.SubjectRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

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

    private LearningResourceService learningResourceService;

    @BeforeEach
    void setUp() {
        learningResourceService = new LearningResourceService(
                learningResourceRepository,
                subjectRepository,
                urlValidator
        );
    }

    // ---------------------------------------
    // GET RESOURCES TESTS
    // ---------------------------------------

    @Test
    void shouldReturnResourcesForExistingSubject() {

        Long subjectId = 1L;

        Subject subject = mock(Subject.class);
        when(subject.getId()).thenReturn(subjectId);

        LearningResource resource = new LearningResource(
                subject,
                "Java Documentation",
                "https://docs.oracle.com",
                "Official Java documentation",
                ResourceType.WEBSITE
        );
        resource.setId(10L);

        when(subjectRepository.existsById(subjectId)).thenReturn(true);
        when(learningResourceRepository.findBySubjectId(subjectId))
                .thenReturn(List.of(resource));

        List<LearningResourceResponseDTO> result =
                learningResourceService.getResourcesBySubject(subjectId);

        assertEquals(1, result.size());
        assertEquals(10L, result.get(0).id());
        assertEquals(subjectId, result.get(0).subjectId());
        assertEquals("Java Documentation", result.get(0).title());
        assertEquals("https://docs.oracle.com", result.get(0).url());
        assertEquals(ResourceType.WEBSITE, result.get(0).resourceType());

        verify(learningResourceRepository).findBySubjectId(subjectId);
    }

    @Test
    void shouldReturnEmptyListWhenSubjectHasNoResources() {

        Long subjectId = 1L;

        when(subjectRepository.existsById(subjectId)).thenReturn(true);
        when(learningResourceRepository.findBySubjectId(subjectId))
                .thenReturn(List.of());

        List<LearningResourceResponseDTO> result =
                learningResourceService.getResourcesBySubject(subjectId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenSubjectDoesNotExistForGet() {

        Long subjectId = 999L;

        when(subjectRepository.existsById(subjectId)).thenReturn(false);

        assertThrows(
                RessourceNotFoundException.class,
                () -> learningResourceService.getResourcesBySubject(subjectId)
        );

        verify(learningResourceRepository, never()).findBySubjectId(any());
    }

    // ---------------------------------------
    // CREATE RESOURCE TESTS
    // ---------------------------------------

    @Test
    void shouldCreateLearningResourceSuccessfully() {

        Long subjectId = 1L;

        Subject subject = mock(Subject.class);
        when(subject.getId()).thenReturn(subjectId);

        LearningResourceRequestDTO request =
                new LearningResourceRequestDTO(
                        "Java Documentation",
                        "https://docs.oracle.com",
                        "Official Java documentation",
                        ResourceType.WEBSITE
                );

        when(subjectRepository.findById(subjectId))
                .thenReturn(Optional.of(subject));

        when(learningResourceRepository.existsBySubjectIdAndUrl(
                subjectId, request.url()
        )).thenReturn(false);

        when(learningResourceRepository.save(any(LearningResource.class)))
                .thenAnswer(invocation -> {
                    LearningResource saved = invocation.getArgument(0);
                    saved.setId(10L);
                    return saved;
                });

        LearningResourceResponseDTO result =
                learningResourceService.createResource(subjectId, request);

        assertNotNull(result);
        assertEquals(10L, result.id());
        assertEquals(subjectId, result.subjectId());
        assertEquals("Java Documentation", result.title());
        assertEquals("https://docs.oracle.com", result.url());
        assertEquals(ResourceType.WEBSITE, result.resourceType());

        verify(urlValidator).validate(request.url());
        verify(learningResourceRepository).save(any(LearningResource.class));
    }

    @Test
    void shouldThrowExceptionWhenSubjectDoesNotExistForCreate() {

        Long subjectId = 999L;

        LearningResourceRequestDTO request =
                new LearningResourceRequestDTO(
                        "Java Documentation",
                        "https://docs.oracle.com",
                        "Official Java documentation",
                        ResourceType.WEBSITE
                );

        when(subjectRepository.findById(subjectId))
                .thenReturn(Optional.empty());

        assertThrows(
                RessourceNotFoundException.class,
                () -> learningResourceService.createResource(subjectId, request)
        );

        verify(urlValidator, never()).validate(any());
        verify(learningResourceRepository, never()).save(any());
    }

    @Test
    void shouldRejectInvalidUrl() {

        Long subjectId = 1L;

        Subject subject = mock(Subject.class);

        LearningResourceRequestDTO request =
                new LearningResourceRequestDTO(
                        "Bad resource",
                        "ftp://example.com",
                        "Invalid URL test",
                        ResourceType.WEBSITE
                );

        when(subjectRepository.findById(subjectId))
                .thenReturn(Optional.of(subject));

        doThrow(new IllegalArgumentException(
                "Only HTTP and HTTPS URLs are allowed"
        )).when(urlValidator).validate(request.url());

        assertThrows(
                IllegalArgumentException.class,
                () -> learningResourceService.createResource(subjectId, request)
        );

        verify(learningResourceRepository, never()).save(any());
    }

    @Test
    void shouldRejectDuplicateUrlForSameSubject() {

        Long subjectId = 1L;

        Subject subject = mock(Subject.class);

        LearningResourceRequestDTO request =
                new LearningResourceRequestDTO(
                        "Java Documentation",
                        "https://docs.oracle.com",
                        "Duplicate test",
                        ResourceType.WEBSITE
                );

        when(subjectRepository.findById(subjectId))
                .thenReturn(Optional.of(subject));

        when(learningResourceRepository.existsBySubjectIdAndUrl(
                subjectId, request.url()
        )).thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> learningResourceService.createResource(subjectId, request)
        );

        verify(urlValidator).validate(request.url());
        verify(learningResourceRepository, never()).save(any());
    }

    @Test
    void shouldTrimWhitespaceFromUrlBeforeSaving() {

        Long subjectId = 1L;

        Subject subject = mock(Subject.class);
        when(subject.getId()).thenReturn(subjectId);

        LearningResourceRequestDTO request =
                new LearningResourceRequestDTO(
                        "Java Documentation",
                        "  https://docs.oracle.com  ",
                        "Whitespace test",
                        ResourceType.WEBSITE
                );

        when(subjectRepository.findById(subjectId))
                .thenReturn(Optional.of(subject));

        when(learningResourceRepository.existsBySubjectIdAndUrl(
                subjectId, "https://docs.oracle.com"
        )).thenReturn(false);

        when(learningResourceRepository.save(any(LearningResource.class)))
                .thenAnswer(invocation -> {
                    LearningResource saved = invocation.getArgument(0);
                    saved.setId(11L);
                    return saved;
                });

        LearningResourceResponseDTO result =
                learningResourceService.createResource(subjectId, request);

        assertEquals("https://docs.oracle.com", result.url());

        verify(urlValidator).validate(request.url());
        verify(learningResourceRepository).existsBySubjectIdAndUrl(
                subjectId, "https://docs.oracle.com"
        );
    }
}
