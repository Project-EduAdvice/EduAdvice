-- =====================================================================
-- V2 — Aligne le schéma sur les entités JPA mergées après V1.
--
-- Écarts détectés (Hibernate ddl-auto=validate + comparaison de schémas) :
--   1. Débouchés : relation 1-N (parcours_id) devenue N-N (table de jointure),
--      et table renommée career_opportunities -> career_opportunity.
--   2. subjects : nouvelle colonne ects.
--   3. teaching_units : nouvelles catégories PROFESSIONALIZATION et OPTIONAL.
--   4. teaching_units : ects devient facultatif (certaines UE n'en ont pas).
--
-- Migration NON destructive : les liens parcours <-> débouchés existants
-- sont recopiés dans la table de jointure avant de supprimer l'ancienne colonne.
-- =====================================================================

-- ---------------------------------------------------------------------
-- 1. Débouchés : 1-N -> N-N
-- ---------------------------------------------------------------------
ALTER TABLE career_opportunities RENAME TO career_opportunity;

CREATE TABLE parcours_career_opportunities (
    parcours_id             BIGINT NOT NULL,
    career_opportunities_id BIGINT NOT NULL,

    CONSTRAINT pk_parcours_career_opportunities
        PRIMARY KEY (parcours_id, career_opportunities_id),

    CONSTRAINT fk_pco_parcours
        FOREIGN KEY (parcours_id)
            REFERENCES parcours(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_pco_career_opportunity
        FOREIGN KEY (career_opportunities_id)
            REFERENCES career_opportunity(id)
            ON DELETE CASCADE
);

-- On conserve les liens existants avant de supprimer l'ancienne colonne.
INSERT INTO parcours_career_opportunities (parcours_id, career_opportunities_id)
SELECT parcours_id, id
FROM career_opportunity;

ALTER TABLE career_opportunity
    DROP CONSTRAINT uq_career_title,
    DROP CONSTRAINT fk_careers_parcours,
    DROP COLUMN parcours_id;

-- ---------------------------------------------------------------------
-- 2. subjects : ajout des ECTS
-- ---------------------------------------------------------------------
ALTER TABLE subjects
    ADD COLUMN ects DECIMAL(4,1);

-- ---------------------------------------------------------------------
-- 3. teaching_units : nouvelles catégories
-- ---------------------------------------------------------------------
ALTER TABLE teaching_units
    DROP CONSTRAINT chk_teaching_unit_category;

ALTER TABLE teaching_units
    ADD CONSTRAINT chk_teaching_unit_category
        CHECK (category IN (
                            'MAJOR_1',
                            'MAJOR_2',
                            'MINOR',
                            'TRANSVERSAL',
                            'PROFESSIONALIZATION',
                            'OPTIONAL'
            ));

-- ---------------------------------------------------------------------
-- 4. teaching_units : ECTS facultatifs
--    (la contrainte chk_teaching_unit_ects "ects > 0" reste valable :
--     un CHECK est ignoré quand la valeur est NULL)
-- ---------------------------------------------------------------------
ALTER TABLE teaching_units
    ALTER COLUMN ects DROP NOT NULL;
