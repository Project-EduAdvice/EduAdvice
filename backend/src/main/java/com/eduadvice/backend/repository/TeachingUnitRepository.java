
package com.eduadvice.backend.repository;

import com.eduadvice.backend.entity.TeachingUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeachingUnitRepository extends JpaRepository<TeachingUnit, Long> {

    List<TeachingUnit> findBySemesterId(Long semesterId);
}
