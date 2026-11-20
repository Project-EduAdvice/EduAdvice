import { screen } from "@testing-library/react";
import { beforeEach, describe, expect, it, vi } from "vitest";
import { getLearningResources, getSubject, getSubjectTeachingUnit } from "../services/subjects";
import { getTeachingUnitSemester } from "../services/teachingUnits";
import { apiError, resources, semester3, subjectBare, subjectProofs, unitMajor1 } from "../test/fixtures";
import { renderAt } from "../test/utils";
import { SubjectDetail } from "./SubjectDetail";

vi.mock("../services/subjects");
vi.mock("../services/teachingUnits");

const open = (path = "/cours/20") => renderAt(<SubjectDetail />, { path, route: "/cours/:id" });

describe("page d'un cours", () => {
  beforeEach(() => {
    vi.resetAllMocks();
    vi.mocked(getSubject).mockResolvedValue(subjectProofs);
    vi.mocked(getSubjectTeachingUnit).mockResolvedValue(unitMajor1);
    vi.mocked(getTeachingUnitSemester).mockResolvedValue(semester3);
    vi.mocked(getLearningResources).mockResolvedValue(resources);
  });

  it("affiche le cours, son UE, ses ECTS, sa description et son contenu", async () => {
    open();

    expect(await screen.findByRole("heading", { level: 1, name: "Programmes et preuves" })).toBeInTheDocument();
    expect(screen.getByText(/Majeure I : Programmation · 3 ECTS/)).toBeInTheDocument();
    expect(screen.getByText("Étude des méthodes formelles.")).toBeInTheDocument();
    expect(screen.getByRole("heading", { name: "Contenu du cours" })).toBeInTheDocument();
    expect(screen.getByText("Logique, assertions, invariants et preuves de programmes.")).toBeInTheDocument();
  });

  it("n'affiche ni contenu ni ECTS quand le backend ne les fournit pas", async () => {
    vi.mocked(getSubject).mockResolvedValue(subjectBare);
    open("/cours/21");

    expect(await screen.findByRole("heading", { level: 1, name: "Projet" })).toBeInTheDocument();
    expect(screen.queryByRole("heading", { name: "Contenu du cours" })).toBeNull();
    expect(screen.getByText("Majeure I : Programmation", { selector: "p" })).toBeInTheDocument();
  });

  it("liste les ressources avec leur type et un lien qui s'ouvre dans un nouvel onglet", async () => {
    open();

    expect(await screen.findByRole("heading", { name: "Supports de cours" })).toBeInTheDocument();
    const links = await screen.findAllByRole("link", { name: /Ouvrir la ressource/ });
    expect(links).toHaveLength(2);
    expect(links[0]).toHaveAttribute("href", "https://coq.inria.fr/doc");
    expect(links[0]).toHaveAttribute("target", "_blank");
    expect(screen.getByText("Documentation Coq")).toBeInTheDocument();
    expect(screen.getByText("Site web")).toBeInTheDocument();
    expect(screen.getByText("Vidéo")).toBeInTheDocument();
  });

  it("affiche un message quand le cours n'a aucune ressource", async () => {
    vi.mocked(getLearningResources).mockResolvedValue([]);
    open();

    expect(
      await screen.findByText("Aucune ressource n'est disponible pour ce cours pour le moment."),
    ).toBeInTheDocument();
  });

  it("garde le cours affiché quand seules les ressources sont en erreur", async () => {
    vi.mocked(getLearningResources).mockRejectedValue(apiError(500, "Ressources indisponibles"));
    open();

    expect(await screen.findByRole("alert")).toHaveTextContent("Ressources indisponibles");
    expect(screen.getByRole("heading", { level: 1, name: "Programmes et preuves" })).toBeInTheDocument();
  });

  it("affiche la page 404 quand le cours n'existe pas", async () => {
    vi.mocked(getSubject).mockRejectedValue(apiError(404, "Matière introuvable"));
    open("/cours/99");

    expect(await screen.findByRole("heading", { name: "Page introuvable" })).toBeInTheDocument();
  });

  it("n'interroge pas le backend pour un identifiant non numérique", () => {
    open("/cours/abc");

    expect(screen.getByRole("heading", { name: "Page introuvable" })).toBeInTheDocument();
    expect(getSubject).not.toHaveBeenCalled();
  });
});
