import { beforeEach, describe, expect, it, vi } from "vitest";
import { semester3, unitMajor1, unitMinor, p2s } from "../test/fixtures";
import { api } from "./api";
import { getCurriculum } from "./parcours";

vi.mock("./api", () => ({ api: { get: vi.fn() } }));

describe("getCurriculum", () => {
  beforeEach(() => {
    vi.resetAllMocks();
  });

  it("charge les UE de chaque semestre et trie les semestres par numéro", async () => {
    const responses: Record<string, unknown> = {
      "/parcours/2/semesters": [
        { id: 11, number: 4, parcours: p2s },
        semester3,
      ],
      "/semesters/10/teaching-units": [unitMajor1],
      "/semesters/11/teaching-units": [unitMinor],
    };
    vi.mocked(api.get).mockImplementation((async (url: string) => ({ data: responses[url] })) as never);

    const result = await getCurriculum(2);

    expect(result).toEqual([
      { id: 10, number: 3, teachingUnits: [unitMajor1] },
      { id: 11, number: 4, teachingUnits: [unitMinor] },
    ]);
  });

  it("renvoie une liste vide quand le parcours n'a pas de semestre", async () => {
    vi.mocked(api.get).mockResolvedValue({ data: [] } as never);
    expect(await getCurriculum(2)).toEqual([]);
  });
});
