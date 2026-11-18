import type { ResourceType, TeachingUnitCategory } from "../services/types";

// Traduction des valeurs (enums) renvoyées par le backend en libellés affichables
export const CATEGORY_LABELS: Record<TeachingUnitCategory, string> = {
  MAJOR_1: "Majeure 1",
  MAJOR_2: "Majeure 2",
  MINOR: "Mineure",
  TRANSVERSAL: "Transversale",
  PROFESSIONALIZATION: "Professionnalisation",
  OPTIONAL: "Facultative",
};

export const RESOURCE_TYPE_LABELS: Record<ResourceType, string> = {
  DOCUMENT: "Document",
  VIDEO: "Vidéo",
  WEBSITE: "Site web",
};

export function plural(n: number, singular: string, pluralForm: string): string {
  return `${n} ${n > 1 ? pluralForm : singular}`;
}
