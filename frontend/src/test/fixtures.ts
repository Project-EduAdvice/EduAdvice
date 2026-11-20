import { AxiosError, type InternalAxiosRequestConfig } from "axios";
import type {
  CareerOpportunity,
  LearningResource,
  Parcours,
  Semester,
  SemesterWithUnits,
  Subject,
  TeachingUnit,
} from "../services/types";

export const p2s: Parcours = {
  id: 2,
  name: "P2S",
  level: "Master 2",
  diploma: "Master",
  mention: "Informatique",
  establishment: "Université Sorbonne Paris Nord",
  description: "Parcours Programmation, Sûreté et Sécurité.",
};

export const ei2d: Parcours = {
  id: 1,
  name: "EI2D",
  level: "Master 2",
  diploma: "Master",
  mention: "Informatique",
  establishment: "Université Sorbonne Paris Nord",
  description: "Parcours Ingénierie de l'Intelligence et des Données.",
};

export const unitMajor1: TeachingUnit = {
  id: 1,
  semesterId: 10,
  name: "Majeure I : Programmation",
  category: "MAJOR_1",
  ects: 12,
};

export const unitMajor2: TeachingUnit = {
  id: 2,
  semesterId: 10,
  name: "Majeure II : Spécification, Sûreté et Sécurité",
  category: "MAJOR_2",
  ects: 12,
};

export const unitSpecific: TeachingUnit = {
  id: 3,
  semesterId: 10,
  name: "UE spécifique parcours EUR",
  category: "TRANSVERSAL",
  ects: null,
};

export const unitMinor: TeachingUnit = {
  id: 4,
  semesterId: 11,
  name: "Mineures",
  category: "MINOR",
  ects: 6,
};

export const unitProfessional: TeachingUnit = {
  id: 5,
  semesterId: 11,
  name: "Professionnalisation",
  category: "PROFESSIONALIZATION",
  ects: 24,
};

export const semester3: Semester = { id: 10, number: 3, parcours: p2s };

export const curriculum: SemesterWithUnits[] = [
  { id: 10, number: 3, teachingUnits: [unitMajor1, unitMajor2, unitSpecific] },
  { id: 11, number: 4, teachingUnits: [unitMinor, unitProfessional] },
];

export const subjectProofs: Subject = {
  id: 20,
  teachingUnitId: 1,
  name: "Programmes et preuves",
  description: "Étude des méthodes formelles.",
  content: "Logique, assertions, invariants et preuves de programmes.",
  ects: 3,
};

export const subjectBare: Subject = {
  id: 21,
  teachingUnitId: 1,
  name: "Projet",
  description: null,
  content: null,
  ects: null,
};

export const resources: LearningResource[] = [
  {
    id: 1,
    subjectId: 20,
    title: "Documentation Coq",
    url: "https://coq.inria.fr/doc",
    description: "Référence officielle.",
    resourceType: "WEBSITE",
  },
  {
    id: 2,
    subjectId: 20,
    title: "Cours vidéo",
    url: "https://example.com/video",
    description: null,
    resourceType: "VIDEO",
  },
];

export const softwareEngineer: CareerOpportunity = {
  id: 1,
  title: "Software Engineer",
  sector: "Technology",
  description: "Design, build and maintain software systems.",
  required_skills: "Java, Git, Testing",
};

export const dataEngineer: CareerOpportunity = {
  id: 4,
  title: "Data Engineer",
  sector: "Data",
  description: null,
  required_skills: null,
};

export function apiError(status: number, message?: string) {
  return new AxiosError("Request failed", "ERR_BAD_RESPONSE", undefined, undefined, {
    status,
    statusText: "",
    headers: {},
    config: {} as InternalAxiosRequestConfig,
    data: message ? { status, message } : {},
  });
}

export function networkError() {
  return new AxiosError("Network Error", "ERR_NETWORK");
}
