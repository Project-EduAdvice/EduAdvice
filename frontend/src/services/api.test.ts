import { describe, expect, it } from "vitest";
import { apiError, networkError } from "../test/fixtures";
import { getErrorMessage } from "./api";

describe("getErrorMessage", () => {
  it("renvoie le message du backend sans espaces autour", () => {
    expect(getErrorMessage(apiError(404, " Parcours introuvable "))).toBe("Parcours introuvable");
  });

  it("affiche le code HTTP quand le backend n'envoie pas de message", () => {
    expect(getErrorMessage(apiError(500))).toBe("Erreur 500");
  });

  it("signale un serveur injoignable quand le proxy renvoie 502", () => {
    expect(getErrorMessage(apiError(502))).toBe(
      "Impossible de joindre le serveur. Vérifiez que le backend est lancé.",
    );
  });

  it("signale un serveur injoignable quand il n'y a aucune réponse", () => {
    expect(getErrorMessage(networkError())).toBe(
      "Impossible de joindre le serveur. Vérifiez que le backend est lancé.",
    );
  });

  it("gère une erreur inattendue", () => {
    expect(getErrorMessage(new Error("boom"))).toBe("Une erreur inattendue est survenue.");
  });
});
