package com.eduadvice.backend.service;

import com.eduadvice.backend.entity.Semester;
import com.eduadvice.backend.repository.SemesterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SemesterService {

    private final SemesterRepository semesterRepository;

    public SemesterService(SemesterRepository semesterRepository) {
        this.semesterRepository = semesterRepository;
    }

    public List<Semester> getAllSemesters() {
        return semesterRepository.findAll();
    }

    public List<Semester> getSemestersByParcours(Long parcoursId) {
        return semesterRepository.findByParcoursId(parcoursId);
    }

    public Semester getSemesterById(Long id) {
        return semesterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Semester not found"));
    }
}