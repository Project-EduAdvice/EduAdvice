import { api } from "./api";
import type {
  CareerOpportunity,
  Parcours,
  Semester,
  SemesterWithUnits,
  TeachingUnit,
} from "./types";

export async function getParcoursList(): Promise<Parcours[]> {
  const { data } = await api.get<Parcours[]>("/parcours");
  return data;
}

export async function getParcours(id: number | string): Promise<Parcours> {
  const { data } = await api.get<Parcours>(`/parcours/${id}`);
  return data;
}

async function getSemesters(parcoursId: number | string): Promise<Semester[]> {
  const { data } = await api.get<Semester[]>(`/parcours/${parcoursId}/semesters`);
  return data;
}

async function getTeachingUnits(semesterId: number | string): Promise<TeachingUnit[]> {
  const { data } = await api.get<TeachingUnit[]>(`/semesters/${semesterId}/teaching-units`);
  return data;
}

export async function getCurriculum(parcoursId: number | string): Promise<SemesterWithUnits[]> {
  const semesters = await getSemesters(parcoursId);
  const withUnits = await Promise.all(
    semesters.map(async (s) => ({
      id: s.id,
      number: s.number,
      teachingUnits: await getTeachingUnits(s.id),
    })),
  );
  return withUnits.sort((a, b) => a.number - b.number);
}

export async function getCareerOpportunities(parcoursId: number | string): Promise<CareerOpportunity[]> {
  const { data } = await api.get<CareerOpportunity[]>(`/parcours/${parcoursId}/career-opportunities`);
  return data;
}
