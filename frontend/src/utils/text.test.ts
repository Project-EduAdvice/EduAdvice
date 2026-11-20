import { describe, expect, it } from "vitest";
import { normalize, splitList } from "./text";

describe("normalize", () => {
  it("ignore la casse et les accents", () => {
    expect(normalize("Sûreté")).toBe("surete");
    expect(normalize("ÉLÈVE")).toBe("eleve");
  });

  it("laisse un texte simple inchangé", () => {
    expect(normalize("p2s")).toBe("p2s");
  });
});

describe("splitList", () => {
  it("découpe une liste séparée par des virgules et retire les espaces", () => {
    expect(splitList("Java, Git ,  Testing")).toEqual(["Java", "Git", "Testing"]);
  });

  it("ignore les éléments vides", () => {
    expect(splitList("a,, ,b")).toEqual(["a", "b"]);
  });

  it("renvoie une liste vide pour null ou une chaîne vide", () => {
    expect(splitList(null)).toEqual([]);
    expect(splitList("")).toEqual([]);
  });
});
