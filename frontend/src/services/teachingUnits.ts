import { api } from "./api";
import type { Semester, TeachingUnit } from "./types";

export async function getTeachingUnit(id: number | string): Promise<TeachingUnit> {
  const { data } = await api.get<TeachingUnit>(`/teaching-units/${id}`);
  return data;
}

export async function getTeachingUnitSemester(id: number | string): Promise<Semester> {
  const { data } = await api.get<Semester>(`/teaching-units/${id}/semester`);
  return data;
}