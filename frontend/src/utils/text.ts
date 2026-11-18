// Ignore la casse et les accents : "surete" trouve "Sûreté"
export const normalize = (text: string) =>
  text
    .normalize("NFD")
    .replace(/\p{Diacritic}/gu, "")
    .toLowerCase();

export function splitList(value: string | null): string[] {
  return value ? value.split(",").map((item) => item.trim()).filter(Boolean) : [];
}