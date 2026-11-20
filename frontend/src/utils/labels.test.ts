import { describe, expect, it } from "vitest";
import { CATEGORY_LABELS, RESOURCE_TYPE_LABELS, plural } from "./labels";

describe("plural", () => {
  it("garde le singulier pour 0 et 1", () => {
    expect(plural(0, "unité", "unités")).toBe("0 unité");
    expect(plural(1, "unité", "unités")).toBe("1 unité");
  });

  it("utilise le pluriel à partir de 2", () => {
    expect(plural(2, "unité", "unités")).toBe("2 unités");
  });
});

describe("libellés des valeurs du backend", () => {
  it("couvre toutes les catégories d'UE", () => {
    expect(Object.keys(CATEGORY_LABELS).sort()).toEqual(
      ["MAJOR_1", "MAJOR_2", "MINOR", "OPTIONAL", "PROFESSIONALIZATION", "TRANSVERSAL"],
    );
  });

  it("couvre tous les types de ressources", () => {
    expect(Object.keys(RESOURCE_TYPE_LABELS).sort()).toEqual(["DOCUMENT", "VIDEO", "WEBSITE"]);
  });
});
