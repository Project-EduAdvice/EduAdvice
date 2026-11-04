CREATE TABLE "establishments" (
  "id" integer PRIMARY KEY,
  "name" varchar(150) NOT NULL,
  "city" varchar(100),
  "website" varchar(255)
);

CREATE TABLE "diplomas" (
  "id" integer PRIMARY KEY,
  "establishment_id" integer NOT NULL,
  "name" varchar(150) NOT NULL,
  "level" varchar(50)
);

CREATE TABLE "mentions" (
  "id" integer PRIMARY KEY,
  "diploma_id" integer NOT NULL,
  "name" varchar(150) NOT NULL
);

CREATE TABLE "parcours" (
  "id" integer PRIMARY KEY,
  "mention_id" integer NOT NULL,
  "name" varchar(200) NOT NULL,
  "description" text
);

CREATE TABLE "academic_levels" (
  "id" integer PRIMARY KEY,
  "parcours_id" integer NOT NULL,
  "name" varchar(10) NOT NULL
);

CREATE TABLE "semesters" (
  "id" integer PRIMARY KEY,
  "academic_level_id" integer NOT NULL,
  "number" integer NOT NULL,
  "name" varchar(50)
);

CREATE TABLE "teaching_units" (
  "id" integer PRIMARY KEY,
  "semester_id" integer NOT NULL,
  "code" varchar(30),
  "name" varchar(200) NOT NULL,
  "description" text,
  "ects" decimal(4,1)
);

CREATE TABLE "subjects" (
  "id" integer PRIMARY KEY,
  "teaching_unit_id" integer NOT NULL,
  "code" varchar(30),
  "name" varchar(200) NOT NULL,
  "description" text
);

CREATE TABLE "learning_resources" (
  "id" integer PRIMARY KEY,
  "title" varchar(200) NOT NULL,
  "url" varchar(500) NOT NULL,
  "description" text,
  "resource_type" varchar(50)
);

CREATE TABLE "subject_resources" (
  "subject_id" integer NOT NULL,
  "resource_id" integer NOT NULL,
  PRIMARY KEY ("subject_id", "resource_id")
);

CREATE TABLE "career_opportunities" (
  "id" integer PRIMARY KEY,
  "title" varchar(200) NOT NULL,
  "description" text,
  "sector" varchar(150)
);

CREATE TABLE "parcours_careers" (
  "parcours_id" integer NOT NULL,
  "career_opportunity_id" integer NOT NULL,
  PRIMARY KEY ("parcours_id", "career_opportunity_id")
);

CREATE TABLE "skills" (
  "id" integer PRIMARY KEY,
  "name" varchar(150) UNIQUE NOT NULL,
  "description" text
);

CREATE TABLE "career_skills" (
  "career_opportunity_id" integer NOT NULL,
  "skill_id" integer NOT NULL,
  PRIMARY KEY ("career_opportunity_id", "skill_id")
);

CREATE UNIQUE INDEX ON "academic_levels" ("parcours_id", "name");

CREATE UNIQUE INDEX ON "semesters" ("academic_level_id", "number");

CREATE UNIQUE INDEX ON "teaching_units" ("semester_id", "code");

CREATE UNIQUE INDEX ON "subjects" ("teaching_unit_id", "code");

COMMENT ON COLUMN "academic_levels"."name" IS 'M1 or M2';

COMMENT ON COLUMN "teaching_units"."ects" IS 'ECTS allocated to the UE';

ALTER TABLE "diplomas" ADD FOREIGN KEY ("establishment_id") REFERENCES "establishments" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "mentions" ADD FOREIGN KEY ("diploma_id") REFERENCES "diplomas" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "parcours" ADD FOREIGN KEY ("mention_id") REFERENCES "mentions" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "academic_levels" ADD FOREIGN KEY ("parcours_id") REFERENCES "parcours" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "semesters" ADD FOREIGN KEY ("academic_level_id") REFERENCES "academic_levels" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "teaching_units" ADD FOREIGN KEY ("semester_id") REFERENCES "semesters" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "subjects" ADD FOREIGN KEY ("teaching_unit_id") REFERENCES "teaching_units" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "subject_resources" ADD FOREIGN KEY ("subject_id") REFERENCES "subjects" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "subject_resources" ADD FOREIGN KEY ("resource_id") REFERENCES "learning_resources" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "parcours_careers" ADD FOREIGN KEY ("parcours_id") REFERENCES "parcours" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "parcours_careers" ADD FOREIGN KEY ("career_opportunity_id") REFERENCES "career_opportunities" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "career_skills" ADD FOREIGN KEY ("career_opportunity_id") REFERENCES "career_opportunities" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "career_skills" ADD FOREIGN KEY ("skill_id") REFERENCES "skills" ("id") DEFERRABLE INITIALLY IMMEDIATE;
