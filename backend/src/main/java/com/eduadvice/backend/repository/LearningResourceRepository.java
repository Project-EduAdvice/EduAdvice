
package com.eduadvice.backend.repository;

import com.eduadvice.backend.entity.LearningResource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LearningResourceRepository
        extends JpaRepository<LearningResource, Long> {

    List<LearningResource> findBySubjectId(Long subjectId);

    boolean existsBySubjectIdAndUrl(Long subjectId, String url);
}
