
package com.eduadvice.backend.service;

import com.eduadvice.backend.dto.LearningResourceRequestDTO;
import com.eduadvice.backend.dto.LearningResourceResponseDTO;
import com.eduadvice.backend.entity.LearningResource;
import com.eduadvice.backend.entity.Subject;
import com.eduadvice.backend.exception.RessourceNotFoundException;
import com.eduadvice.backend.repository.LearningResourceRepository;
import com.eduadvice.backend.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.eduadvice.backend.exception.DuplicateResourceException;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class LearningResourceService {

    private final LearningResourceRepository learningResourceRepository;
    private final SubjectRepository subjectRepository;
    private final UrlValidator urlValidator;

    public LearningResourceService(
            LearningResourceRepository learningResourceRepository,
            SubjectRepository subjectRepository,
            UrlValidator urlValidator) {
        this.learningResourceRepository = learningResourceRepository;
        this.subjectRepository = subjectRepository;
        this.urlValidator = urlValidator;
    }

    public List<LearningResourceResponseDTO> getResourcesBySubject(Long subjectId) {

        if (!subjectRepository.existsById(subjectId)) {
            throw new RessourceNotFoundException(
                    "Subject not found with id: " + subjectId
            );
        }

        return learningResourceRepository.findBySubjectId(subjectId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional
    public LearningResourceResponseDTO createResource(
            Long subjectId,
            LearningResourceRequestDTO request) {

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RessourceNotFoundException(
                        "Subject not found with id: " + subjectId
                ));

        urlValidator.validate(request.url());

        if (learningResourceRepository.existsBySubjectIdAndUrl(
                subjectId, request.url().trim())) {
            throw new DuplicateResourceException(
                    "This URL already exists for this subject"
            );
        }

        LearningResource resource = new LearningResource(
                subject,
                request.title(),
                request.url().trim(),
                request.description(),
                request.resourceType()
        );

        LearningResource savedResource =
                learningResourceRepository.save(resource);

        return convertToDTO(savedResource);
    }

    private LearningResourceResponseDTO convertToDTO(
            LearningResource resource) {

        return new LearningResourceResponseDTO(
                resource.getId(),
                resource.getSubject().getId(),
                resource.getTitle(),
                resource.getUrl(),
                resource.getDescription(),
                resource.getResourceType()
        );
    }
}
