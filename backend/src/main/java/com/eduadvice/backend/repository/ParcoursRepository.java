package com.eduadvice.backend.repository;

import com.eduadvice.backend.entity.Parcours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParcoursRepository extends JpaRepository<Parcours, Long> {

}
