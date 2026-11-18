import { api } from "./api";
import type { LearningResource, Subject, TeachingUnit } from "./types";

export async function getSubjects(teachingUnitId: number | string): Promise<Subject[]> {
  const { data } = await api.get<Subject[]>(`/teaching-units/${teachingUnitId}/subjects`);
  return data;
}

export async function getSubject(id: number | string): Promise<Subject> {
  const { data } = await api.get<Subject>(`/subjects/${id}`);
  return data;
}

export async function getLearningResources(subjectId: number | string): Promise<LearningResource[]> {
  const { data } = await api.get<LearningResource[]>(`/subjects/${subjectId}/learning-resources`);
  return data;
}

export async function getSubjectTeachingUnit(subjectId: number | string): Promise<TeachingUnit> {
  const { data } = await api.get<TeachingUnit>(`/subjects/${subjectId}/teaching-unit`);
  return data;
}