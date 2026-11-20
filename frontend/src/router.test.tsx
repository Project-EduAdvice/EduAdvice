import { render, screen } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import { describe, expect, it } from "vitest";
import { AppRoutes } from "./router";

const renderAt = (path: string) =>
  render(
    <MemoryRouter initialEntries={[path]}>
      <AppRoutes />
    </MemoryRouter>,
  );

describe("routes", () => {
  it("affiche la page 404 pour une adresse inconnue, avec l'en-tête du site", () => {
    renderAt("/nimporte-quoi");

    expect(screen.getByRole("heading", { name: "Page introuvable" })).toBeInTheDocument();
    expect(screen.getByRole("link", { name: "Retour à l'accueil" })).toHaveAttribute("href", "/");
    expect(screen.getByRole("navigation", { name: "Main" })).toBeInTheDocument();
  });

  it.each(["/programmes/abc", "/ue/abc", "/cours/abc"])("affiche la page 404 pour %s", (path) => {
    renderAt(path);
    expect(screen.getByRole("heading", { name: "Page introuvable" })).toBeInTheDocument();
  });
});
