import { render, screen, within } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import { describe, expect, it } from "vitest";
import { Breadcrumb } from "./Breadcrumb";

function renderBreadcrumb(items: Parameters<typeof Breadcrumb>[0]["items"]) {
  render(
    <MemoryRouter>
      <Breadcrumb items={items} />
    </MemoryRouter>,
  );
  return within(screen.getByRole("navigation", { name: "Fil d'Ariane" }));
}

describe("Breadcrumb", () => {
  it("affiche le lien vers l'accueil et les étapes cliquables", () => {
    const nav = renderBreadcrumb([
      { label: "Formations", to: "/#formations" },
      { label: "P2S", to: "/programmes/2" },
      { label: "Semestre 3" },
    ]);

    expect(nav.getByRole("link", { name: "Accueil" })).toHaveAttribute("href", "/");
    expect(nav.getByRole("link", { name: "Formations" })).toHaveAttribute("href", "/#formations");
    expect(nav.getByRole("link", { name: "P2S" })).toHaveAttribute("href", "/programmes/2");
  });

  it("marque la dernière étape comme page courante, sans lien", () => {
    const nav = renderBreadcrumb([{ label: "Formations", to: "/#formations" }, { label: "Semestre 3", to: "/x" }]);

    expect(nav.getByText("Semestre 3")).toHaveAttribute("aria-current", "page");
    expect(nav.queryByRole("link", { name: "Semestre 3" })).toBeNull();
  });

  it("affiche en simple texte une étape intermédiaire sans adresse", () => {
    const nav = renderBreadcrumb([{ label: "Étape" }, { label: "Fin" }]);

    expect(nav.getByText("Étape")).toBeInTheDocument();
    expect(nav.queryByRole("link", { name: "Étape" })).toBeNull();
  });
});
