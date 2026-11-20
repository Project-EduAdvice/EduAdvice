import { render, screen } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import { describe, expect, it } from "vitest";
import { p2s } from "../../test/fixtures";
import { Parcours } from "./Parcours";

const renderCard = (p = p2s) =>
  render(
    <MemoryRouter>
      <Parcours p={p} />
    </MemoryRouter>,
  );

describe("carte Parcours", () => {
  it("affiche les informations du parcours et mène à sa page", () => {
    renderCard();

    expect(screen.getByRole("link")).toHaveAttribute("href", "/programmes/2");
    expect(screen.getByRole("heading", { name: "P2S" })).toBeInTheDocument();
    expect(screen.getByText("Master 2")).toBeInTheDocument();
    expect(screen.getByText("Informatique")).toBeInTheDocument();
    expect(screen.getByText("Université Sorbonne Paris Nord")).toBeInTheDocument();
    expect(screen.getByText("Parcours Programmation, Sûreté et Sécurité.")).toBeInTheDocument();
  });

  it("n'affiche pas la mention quand elle est absente", () => {
    renderCard({ ...p2s, mention: null, description: null });

    expect(screen.queryByText("Informatique")).toBeNull();
    expect(screen.getByRole("heading", { name: "P2S" })).toBeInTheDocument();
  });
});
