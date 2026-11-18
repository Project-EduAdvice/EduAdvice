export type Parcours = {
  id: number;
  name: string;
  level: string;
  diploma: string;
  mention: string | null;
  establishment: string;
  description: string | null;
};

export type Semester = {
  id: number;
  number: number;
  parcours: Parcours;
};

export type TeachingUnitCategory =
  | "MAJOR_1"
  | "MAJOR_2"
  | "MINOR"
  | "TRANSVERSAL"
  | "PROFESSIONALIZATION"
  | "OPTIONAL";

export type TeachingUnit = {
  id: number;
  semesterId: number;
  name: string;
  category: TeachingUnitCategory;
  ects: number | null; // vide en base pour certaines UE
};

export type SemesterWithUnits = {
  id: number;
  number: number;
  teachingUnits: TeachingUnit[];
};

export type Subject = {
  id: number;
  teachingUnitId: number;
  name: string;
  description: string | null;
  content: string | null;
  ects: number | null;
};

export type ResourceType = "DOCUMENT" | "VIDEO" | "WEBSITE";

export type LearningResource = {
  id: number;
  subjectId: number;
  title: string;
  url: string;
  description: string | null;
  resourceType: ResourceType;
};

export type CareerOpportunity = {
  id: number;
  title: string;
  sector: string | null;
  description: string | null;
  required_skills: string | null;
};

export type ApiError = {
  status: number;
  error?: string;
  message: string;
  path?: string;
  timestamp?: string;
};
