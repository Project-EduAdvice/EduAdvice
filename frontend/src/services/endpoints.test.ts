import { beforeEach, describe, expect, it, vi } from "vitest";
import { api } from "./api";
import { getCareerOpportunities, getParcours, getParcoursList } from "./parcours";
import { getLearningResources, getSubject, getSubjectTeachingUnit, getSubjects } from "./subjects";
import { getTeachingUnit, getTeachingUnitSemester } from "./teachingUnits";

vi.mock("./api", () => ({ api: { get: vi.fn() } }));

const cases: [string, () => Promise<unknown>, string][] = [
  ["getParcoursList", () => getParcoursList(), "/parcours"],
  ["getParcours", () => getParcours(2), "/parcours/2"],
  ["getCareerOpportunities", () => getCareerOpportunities(2), "/parcours/2/career-opportunities"],
  ["getTeachingUnit", () => getTeachingUnit(1), "/teaching-units/1"],
  ["getTeachingUnitSemester", () => getTeachingUnitSemester(1), "/teaching-units/1/semester"],
  ["getSubjects", () => getSubjects(1), "/teaching-units/1/subjects"],
  ["getSubject", () => getSubject(20), "/subjects/20"],
  ["getSubjectTeachingUnit", () => getSubjectTeachingUnit(20), "/subjects/20/teaching-unit"],
  ["getLearningResources", () => getLearningResources(20), "/subjects/20/learning-resources"],
];

describe("routes de l'API appelées par le frontend", () => {
  beforeEach(() => {
    vi.resetAllMocks();
    vi.mocked(api.get).mockResolvedValue({ data: [] } as never);
  });

  it.each(cases)("%s appelle %s", async (_name, call, url) => {
    await call();
    expect(api.get).toHaveBeenCalledWith(url);
  });
});
