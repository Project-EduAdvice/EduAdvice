
package com.eduadvice.backend.service;

import com.eduadvice.backend.dto.TeachingUnitResponseDTO;
import com.eduadvice.backend.entity.TeachingUnit;
import com.eduadvice.backend.exception.RessourceNotFoundException;
import com.eduadvice.backend.repository.TeachingUnitRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeachingUnitService {

    private final TeachingUnitRepository teachingUnitRepository;

    public TeachingUnitService(TeachingUnitRepository teachingUnitRepository) {
        this.teachingUnitRepository = teachingUnitRepository;
    }

    public List<TeachingUnitResponseDTO> getAllTeachingUnits() {
        return teachingUnitRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public TeachingUnitResponseDTO getTeachingUnitById(Long id) {
        TeachingUnit teachingUnit = teachingUnitRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException(
                        "Teaching unit not found with id: " + id
                ));

        return convertToDTO(teachingUnit);
    }

    public List<TeachingUnitResponseDTO> getTeachingUnitsBySemester(
            Long semesterId
    ) {
        return teachingUnitRepository.findBySemesterId(semesterId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    private TeachingUnitResponseDTO convertToDTO(TeachingUnit teachingUnit) {
        return new TeachingUnitResponseDTO(
                teachingUnit.getId(),
                teachingUnit.getSemester().getId(),
                teachingUnit.getName(),
                teachingUnit.getCategory(),
                teachingUnit.getEcts()
        );
    }
}
