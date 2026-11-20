import { render, screen, within } from "@testing-library/react";
import { describe, expect, it } from "vitest";
import { dataEngineer, softwareEngineer } from "../../test/fixtures";
import { CareerCard } from "./Career";

describe("carte Métier", () => {
  it("affiche le titre, le secteur, la description et les compétences", () => {
    render(<CareerCard career={softwareEngineer} />);

    expect(screen.getByRole("heading", { name: "Software Engineer" })).toBeInTheDocument();
    expect(screen.getByText("Technology")).toBeInTheDocument();
    expect(screen.getByText("Design, build and maintain software systems.")).toBeInTheDocument();

    const skills = within(screen.getByRole("list", { name: "Compétences requises" }));
    expect(skills.getAllByRole("listitem").map((li) => li.textContent)).toEqual(["Java", "Git", "Testing"]);
  });

  it("affiche une bulle par parcours", () => {
    render(<CareerCard career={softwareEngineer} parcoursNames={["EI2D", "P2S"]} />);

    const bubbles = within(screen.getByRole("list", { name: "Parcours" }));
    expect(bubbles.getAllByRole("listitem").map((li) => li.textContent)).toEqual(["EI2D", "P2S"]);
  });

  it("n'affiche pas de bulles sans parcours", () => {
    render(<CareerCard career={softwareEngineer} />);
    expect(screen.queryByRole("list", { name: "Parcours" })).toBeNull();
  });

  it("n'affiche pas de compétences quand il n'y en a pas", () => {
    render(<CareerCard career={dataEngineer} />);
    expect(screen.queryByRole("list", { name: "Compétences requises" })).toBeNull();
  });
});
