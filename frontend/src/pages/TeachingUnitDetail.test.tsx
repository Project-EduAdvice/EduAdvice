import { screen, within } from "@testing-library/react";
import { beforeEach, describe, expect, it, vi } from "vitest";
import { getSubjects } from "../services/subjects";
import { getTeachingUnit, getTeachingUnitSemester } from "../services/teachingUnits";
import { apiError, semester3, subjectBare, subjectProofs, unitMajor1 } from "../test/fixtures";
import { renderAt } from "../test/utils";
import { TeachingUnitDetail } from "./TeachingUnitDetail";

vi.mock("../services/subjects");
vi.mock("../services/teachingUnits");

const open = (path = "/ue/1") => renderAt(<TeachingUnitDetail />, { path, route: "/ue/:id" });

describe("page d'une unité d'enseignement", () => {
  beforeEach(() => {
    vi.resetAllMocks();
    vi.mocked(getTeachingUnit).mockResolvedValue(unitMajor1);
    vi.mocked(getTeachingUnitSemester).mockResolvedValue(semester3);
    vi.mocked(getSubjects).mockResolvedValue([subjectProofs, subjectBare]);
  });

  it("affiche l'UE, sa catégorie et ses ECTS", async () => {
    open();

    expect(await screen.findByRole("heading", { level: 1, name: "Majeure I : Programmation" })).toBeInTheDocument();
    expect(screen.getByText("Majeure 1")).toBeInTheDocument();
    expect(screen.getByText("12 ECTS")).toBeInTheDocument();
  });

  it("affiche un fil d'Ariane qui revient au bon semestre", async () => {
    open();

    const crumbs = within(await screen.findByRole("navigation", { name: "Fil d'Ariane" }));
    expect(crumbs.getByRole("link", { name: "Formations" })).toHaveAttribute("href", "/#formations");
    expect(crumbs.getByRole("link", { name: "P2S" })).toHaveAttribute("href", "/programmes/2");
    expect(crumbs.getByRole("link", { name: "Semestre 3" })).toHaveAttribute("href", "/programmes/2?semestre=3");
    expect(crumbs.getByText("Majeure I : Programmation")).toHaveAttribute("aria-current", "page");
  });

  it("liste les cours enseignés, chacun menant à sa page", async () => {
    open();

    expect(await screen.findByRole("heading", { name: "Cours enseignés" })).toBeInTheDocument();
    expect(screen.getByRole("link", { name: /Programmes et preuves/ })).toHaveAttribute("href", "/cours/20");
    expect(screen.getByRole("link", { name: /Projet/ })).toHaveAttribute("href", "/cours/21");
    expect(screen.getByText("Étude des méthodes formelles.")).toBeInTheDocument();
  });

  it("affiche un message quand l'UE n'a pas de cours", async () => {
    vi.mocked(getSubjects).mockResolvedValue([]);
    open();

    expect(
      await screen.findByText("Les matières de cette unité d'enseignement seront bientôt disponibles."),
    ).toBeInTheDocument();
  });

  it("garde l'UE affichée quand seule la liste des cours est en erreur", async () => {
    vi.mocked(getSubjects).mockRejectedValue(apiError(500, "Erreur interne"));
    open();

    expect(await screen.findByRole("alert")).toHaveTextContent("Erreur interne");
    expect(screen.getByRole("heading", { level: 1, name: "Majeure I : Programmation" })).toBeInTheDocument();
  });

  it("affiche la page 404 quand l'UE n'existe pas", async () => {
    vi.mocked(getTeachingUnit).mockRejectedValue(apiError(404, "UE introuvable"));
    open("/ue/99");

    expect(await screen.findByRole("heading", { name: "Page introuvable" })).toBeInTheDocument();
  });

  it("n'interroge pas le backend pour un identifiant non numérique", () => {
    open("/ue/abc");

    expect(screen.getByRole("heading", { name: "Page introuvable" })).toBeInTheDocument();
    expect(getTeachingUnit).not.toHaveBeenCalled();
  });
});
