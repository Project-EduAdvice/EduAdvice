
package com.eduadvice.backend.repository;

import com.eduadvice.backend.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    List<Subject> findByTeachingUnitId(Long teachingUnitId);
    
}
