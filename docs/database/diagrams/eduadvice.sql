-- ==========================================
-- 1. REPRESENTATION FORMEL DU SCHEMA DE LA DATABASE
-- ==========================================

-- ==========================================
-- 1. PARCOURS
-- ==========================================

CREATE TABLE parcours (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    level VARCHAR(100) NOT NULL,
    diploma VARCHAR(100) NOT NULL,
    mention VARCHAR(150),
    establishment VARCHAR(255) NOT NULL,
    description TEXT
);


-- ==========================================
-- 2. SEMESTERS
-- ==========================================

CREATE TABLE semesters (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    parcours_id BIGINT NOT NULL,
    number INT NOT NULL,

    CONSTRAINT fk_semesters_parcours
       FOREIGN KEY (parcours_id)
           REFERENCES parcours(id)
           ON DELETE CASCADE,

    CONSTRAINT uq_semester_number
       UNIQUE (parcours_id, number),

    CONSTRAINT chk_semester_number
       CHECK (number > 0)
);


-- ==========================================
-- 3. TEACHING UNITS (UE)
-- ==========================================

CREATE TABLE teaching_units (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    semester_id BIGINT NOT NULL,
    code VARCHAR(50),
    name VARCHAR(255) NOT NULL,
    category VARCHAR(20) NOT NULL,
    ects DECIMAL(4,1) NOT NULL,

    CONSTRAINT fk_teaching_units_semester
        FOREIGN KEY (semester_id)
            REFERENCES semesters(id)
            ON DELETE CASCADE,

    CONSTRAINT uq_teaching_unit_code
        UNIQUE (semester_id, code),

    CONSTRAINT chk_teaching_unit_category
        CHECK (category IN (
                            'MAJOR_1',
                            'MAJOR_2',
                            'MINOR',
                            'TRANSVERSAL'
            )),

    CONSTRAINT chk_teaching_unit_ects
        CHECK (ects > 0)
);


-- ==========================================
-- 4. SUBJECTS (CONTENUS PÉDAGOGIQUES)
-- ==========================================

CREATE TABLE subjects (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    teaching_unit_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    objectives TEXT,
    content TEXT,

    CONSTRAINT fk_subjects_teaching_unit
      FOREIGN KEY (teaching_unit_id)
          REFERENCES teaching_units(id)
          ON DELETE CASCADE,

    CONSTRAINT uq_subject_name
      UNIQUE (teaching_unit_id, name)
);


-- ==========================================
-- 5. LEARNING RESOURCES
-- ==========================================

CREATE TABLE learning_resources (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    subject_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    url TEXT NOT NULL,
    description TEXT,
    resource_type VARCHAR(20) NOT NULL,

    CONSTRAINT fk_resources_subject
        FOREIGN KEY (subject_id)
            REFERENCES subjects(id)
            ON DELETE CASCADE,

    CONSTRAINT uq_resource_url
        UNIQUE (subject_id, url),

    CONSTRAINT chk_resource_type
        CHECK (resource_type IN (
                                 'DOCUMENT',
                                 'VIDEO',
                                 'WEBSITE',
                                 'EXERCISE',
                                 'TOOL',
                                 'OTHER'
            ))
);


-- ==========================================
-- 6. CAREER OPPORTUNITIES
-- ==========================================

CREATE TABLE career_opportunities (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    parcours_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    sector VARCHAR(150),
    description TEXT,
    required_skills TEXT,

    CONSTRAINT fk_careers_parcours
      FOREIGN KEY (parcours_id)
          REFERENCES parcours(id)
          ON DELETE CASCADE,

    CONSTRAINT uq_career_title
      UNIQUE (parcours_id, title)
);