import { screen, within } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { beforeEach, describe, expect, it, vi } from "vitest";
import { getCareerOpportunities, getCurriculum, getParcours } from "../services/parcours";
import { apiError, curriculum, networkError, p2s, softwareEngineer } from "../test/fixtures";
import { renderAt } from "../test/utils";
import { ParcoursDetail } from "./ParcoursDetail";

vi.mock("../services/parcours");

const open = (path = "/programmes/2") => renderAt(<ParcoursDetail />, { path, route: "/programmes/:id" });

describe("page d'un parcours", () => {
  beforeEach(() => {
    vi.resetAllMocks();
    vi.mocked(getParcours).mockResolvedValue(p2s);
    vi.mocked(getCurriculum).mockResolvedValue(curriculum);
    vi.mocked(getCareerOpportunities).mockResolvedValue([softwareEngineer]);
  });

  it("affiche les informations du parcours", async () => {
    open();

    expect(await screen.findByRole("heading", { level: 1, name: "P2S" })).toBeInTheDocument();
    expect(screen.getByText("Master 2")).toBeInTheDocument();
    expect(screen.getByText("Université Sorbonne Paris Nord")).toBeInTheDocument();
    expect(screen.getByText("Parcours Programmation, Sûreté et Sécurité.")).toBeInTheDocument();
  });

  it("affiche le premier semestre par défaut avec son total d'ECTS", async () => {
    open();

    expect(await screen.findByRole("link", { name: /Majeure I : Programmation/ })).toHaveAttribute("href", "/ue/1");
    expect(screen.getByRole("button", { name: "Semestre 3" })).toHaveAttribute("aria-pressed", "true");
    expect(screen.getByText(/3 unités d'enseignement · 24 ECTS/)).toBeInTheDocument();
    expect(screen.queryByRole("link", { name: /Mineures/ })).toBeNull();
  });

  it("change de semestre au clic", async () => {
    open();
    await screen.findByRole("link", { name: /Majeure I : Programmation/ });

    await userEvent.click(screen.getByRole("button", { name: "Semestre 4" }));

    expect(screen.getByRole("link", { name: /Mineures/ })).toHaveAttribute("href", "/ue/4");
    expect(screen.queryByRole("link", { name: /Majeure I : Programmation/ })).toBeNull();
    expect(screen.getByRole("button", { name: "Semestre 4" })).toHaveAttribute("aria-pressed", "true");
    expect(screen.getByText(/2 unités d'enseignement · 30 ECTS/)).toBeInTheDocument();
  });

  it("ouvre le semestre indiqué dans l'adresse", async () => {
    open("/programmes/2?semestre=4");

    expect(await screen.findByRole("link", { name: /Mineures/ })).toBeInTheDocument();
    expect(screen.getByRole("button", { name: "Semestre 4" })).toHaveAttribute("aria-pressed", "true");
  });

  it("affiche les ECTS d'une UE, et les masque quand ils sont absents", async () => {
    open();

    const withEcts = await screen.findByRole("link", { name: /Majeure I : Programmation/ });
    expect(within(withEcts).getByText("12 ECTS")).toBeInTheDocument();

    const withoutEcts = screen.getByRole("link", { name: /UE spécifique parcours EUR/ });
    expect(within(withoutEcts).queryByText(/ECTS/)).toBeNull();
  });

  it("affiche les métiers du parcours", async () => {
    open();

    expect(await screen.findByRole("heading", { level: 3, name: "Software Engineer" })).toBeInTheDocument();
    expect(screen.getByText("Java")).toBeInTheDocument();
  });

  it("affiche un message quand le parcours n'a pas de métiers", async () => {
    vi.mocked(getCareerOpportunities).mockResolvedValue([]);
    open();

    expect(await screen.findByText("Les débouchés de ce parcours seront bientôt disponibles.")).toBeInTheDocument();
  });

  it("affiche la page 404 quand le parcours n'existe pas", async () => {
    vi.mocked(getParcours).mockRejectedValue(apiError(404, "Parcours introuvable"));
    open("/programmes/99");

    expect(await screen.findByRole("heading", { name: "Page introuvable" })).toBeInTheDocument();
  });

  it("n'interroge pas le backend pour un identifiant non numérique", () => {
    open("/programmes/abc");

    expect(screen.getByRole("heading", { name: "Page introuvable" })).toBeInTheDocument();
    expect(getParcours).not.toHaveBeenCalled();
  });

  it("affiche l'erreur et permet de réessayer quand le backend est injoignable", async () => {
    vi.mocked(getParcours).mockRejectedValueOnce(networkError());
    open();

    expect(await screen.findByRole("alert")).toHaveTextContent("Impossible de joindre le serveur");

    await userEvent.click(screen.getByRole("button", { name: "Réessayer" }));

    expect(await screen.findByRole("heading", { level: 1, name: "P2S" })).toBeInTheDocument();
  });
});
