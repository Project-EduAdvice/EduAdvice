import axios, { isAxiosError } from "axios";
import type { ApiError } from "./types";

export const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? "/api",
  headers: { Accept: "application/json" },
  // En prod, le backend (Render Free) s'endort après 15 min d'inactivité et met
  // environ 1 min à redémarrer. 90 s laisse le temps au réveil sans attendre indéfiniment.
  timeout: 90_000,
});

function isServerUnreachable(error: unknown): boolean {
  if (!isAxiosError(error)) return false;
  return !error.response || [502, 503, 504].includes(error.response.status);
}

export function getErrorMessage(error: unknown): string {
  if (isAxiosError<ApiError>(error)) {
    if (error.response && !isServerUnreachable(error)) {
      // trim() : le backend renvoie parfois un message avec un espace au début
      return error.response.data?.message?.trim() || `Erreur ${error.response.status}`;
    }
    return "Impossible de joindre le serveur. Vérifiez que le backend est lancé.";
  }
  return "Une erreur inattendue est survenue.";
}
