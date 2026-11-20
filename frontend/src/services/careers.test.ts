import { beforeEach, describe, expect, it, vi } from "vitest";
import { dataEngineer, ei2d, p2s, softwareEngineer } from "../test/fixtures";
import { api } from "./api";
import { getCareersWithParcours } from "./careers";
import { getCareerOpportunities, getParcoursList } from "./parcours";

vi.mock("./api", () => ({ api: { get: vi.fn() } }));
vi.mock("./parcours");

describe("getCareersWithParcours", () => {
  beforeEach(() => {
    vi.resetAllMocks();
    vi.mocked(api.get).mockResolvedValue({ data: [softwareEngineer, dataEngineer] } as never);
    vi.mocked(getParcoursList).mockResolvedValue([p2s, ei2d]);
    vi.mocked(getCareerOpportunities).mockImplementation(async (id) =>
      Number(id) === p2s.id ? [softwareEngineer, dataEngineer] : [dataEngineer],
    );
  });

  it("associe chaque métier aux parcours qui y mènent", async () => {
    const careers = await getCareersWithParcours();

    expect(careers.map((c) => [c.title, c.parcoursNames])).toEqual([
      ["Software Engineer", ["P2S"]],
      ["Data Engineer", ["P2S", "EI2D"]],
    ]);
  });

  it("appelle la liste complète des métiers", async () => {
    await getCareersWithParcours();
    expect(api.get).toHaveBeenCalledWith("/career-opportunities");
  });
});
