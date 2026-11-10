
package com.eduadvice.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(
    name = "teaching_units",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_teaching_unit_code",
            columnNames = {"semester_id", "code"}
        )
    }
)
public class TeachingUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    @Column(length = 50)
    private String code;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TeachingUnitCategory category;

    @Column(nullable = false, precision = 4, scale = 1)
    private BigDecimal ects;

    public TeachingUnit() {
    }

    public TeachingUnit(
            Semester semester,
            String code,
            String name,
            TeachingUnitCategory category,
            BigDecimal ects
    ) {
        this.semester = semester;
        this.code = code;
        this.name = name;
        this.category = category;
        this.ects = ects;
    }

    public Long getId() {
        return id;
    }

    public Semester getSemester() {
        return semester;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public TeachingUnitCategory getCategory() {
        return category;
    }

    public BigDecimal getEcts() {
        return ects;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(TeachingUnitCategory category) {
        this.category = category;
    }

    public void setEcts(BigDecimal ects) {
        this.ects = ects;
    }
}
