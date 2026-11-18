import { api } from "./api";
import { getCareerOpportunities, getParcoursList } from "./parcours";
import type { CareerOpportunity } from "./types";

type CareerWithParcours = CareerOpportunity & { parcoursNames: string[] };

export async function getCareersWithParcours(): Promise<CareerWithParcours[]> {
  const [{ data: careers }, parcoursList] = await Promise.all([
    api.get<CareerOpportunity[]>("/career-opportunities"),
    getParcoursList(),
  ]);
  const perParcours = await Promise.all(
    parcoursList.map(async (p) => ({ name: p.name, careers: await getCareerOpportunities(p.id) })),
  );
  return careers.map((career) => ({
    ...career,
    parcoursNames: perParcours.filter((p) => p.careers.some((c) => c.id === career.id)).map((p) => p.name),
  }));
}