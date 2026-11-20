import { render, screen, within } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { beforeEach, describe, expect, it, vi } from "vitest";
import { getCareersWithParcours } from "../services/careers";
import { dataEngineer, networkError, softwareEngineer } from "../test/fixtures";
import { Careers } from "./Careers";

vi.mock("../services/careers");

const careers = [
  { ...softwareEngineer, parcoursNames: ["P2S"] },
  { ...dataEngineer, parcoursNames: ["EI2D", "P2S"] },
];

describe("page des métiers", () => {
  beforeEach(() => {
    vi.resetAllMocks();
    vi.mocked(getCareersWithParcours).mockResolvedValue(careers);
  });

  it("affiche tous les métiers du backend", async () => {
    render(<Careers />);

    expect(await screen.findByRole("heading", { name: "Software Engineer" })).toBeInTheDocument();
    expect(screen.getByRole("heading", { name: "Data Engineer" })).toBeInTheDocument();
    expect(screen.getByText("Java")).toBeInTheDocument();
  });

  it("affiche les parcours de chaque métier dans des bulles", async () => {
    render(<Careers />);

    const card = (await screen.findByRole("heading", { name: "Data Engineer" })).closest("article");
    const bubbles = within(within(card as HTMLElement).getByRole("list", { name: "Parcours" }));
    expect(bubbles.getAllByRole("listitem").map((li) => li.textContent)).toEqual(["EI2D", "P2S"]);
  });

  it("affiche un message quand il n'y a aucun métier", async () => {
    vi.mocked(getCareersWithParcours).mockResolvedValue([]);
    render(<Careers />);

    expect(await screen.findByText("Les métiers seront bientôt disponibles.")).toBeInTheDocument();
  });

  it("affiche l'erreur et permet de réessayer", async () => {
    vi.mocked(getCareersWithParcours).mockRejectedValueOnce(networkError());
    render(<Careers />);

    expect(await screen.findByRole("alert")).toHaveTextContent("Impossible de joindre le serveur");

    await userEvent.click(screen.getByRole("button", { name: "Réessayer" }));

    expect(await screen.findByRole("heading", { name: "Software Engineer" })).toBeInTheDocument();
  });
});
