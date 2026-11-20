import { render, screen, within } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { MemoryRouter } from "react-router-dom";
import { describe, expect, it } from "vitest";
import { Header } from "./Header";

const renderAt = (path: string) =>
  render(
    <MemoryRouter initialEntries={[path]}>
      <Header />
    </MemoryRouter>,
  );

const isCurrent = (name: string) => screen.getByRole("link", { name }).getAttribute("aria-current") === "page";

describe("Header", () => {
  it("contient les liens de navigation", () => {
    renderAt("/");

    expect(screen.getByRole("link", { name: "Accueil" })).toHaveAttribute("href", "/");
    expect(screen.getByRole("link", { name: "Formations" })).toHaveAttribute("href", "/#formations");
    expect(screen.getByRole("link", { name: "Métiers" })).toHaveAttribute("href", "/metiers");
  });

  it.each([
    ["/", "Accueil"],
    ["/#formations", "Formations"],
    ["/programmes/2", "Formations"],
    ["/ue/1", "Formations"],
    ["/metiers", "Métiers"],
  ])("sur %s, seul « %s » est actif", (path, active) => {
    renderAt(path);

    for (const name of ["Accueil", "Formations", "Métiers"]) {
      expect(isCurrent(name)).toBe(name === active);
    }
  });

  it("ouvre et ferme le menu mobile", async () => {
    renderAt("/");
    expect(screen.queryByRole("navigation", { name: "Mobile" })).toBeNull();

    await userEvent.click(screen.getByRole("button", { name: "Ouvrir ou fermer le menu" }));
    const mobile = within(screen.getByRole("navigation", { name: "Mobile" }));
    expect(mobile.getByRole("link", { name: "Métiers" })).toBeInTheDocument();

    await userEvent.click(mobile.getByRole("link", { name: "Métiers" }));
    expect(screen.queryByRole("navigation", { name: "Mobile" })).toBeNull();
  });
});
