import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { describe, expect, it, vi } from "vitest";
import { EmptyState, ErrorState, LoadingState } from "./states";

describe("LoadingState", () => {
  it("annonce le chargement avec un texte par défaut", () => {
    render(<LoadingState />);
    expect(screen.getByRole("status")).toHaveTextContent("Chargement…");
  });

  it("accepte un texte personnalisé", () => {
    render(<LoadingState label="Chargement des parcours…" />);
    expect(screen.getByRole("status")).toHaveTextContent("Chargement des parcours…");
  });
});

describe("ErrorState", () => {
  it("affiche le message d'erreur", () => {
    render(<ErrorState message="Impossible de joindre le serveur." />);
    expect(screen.getByRole("alert")).toHaveTextContent("Impossible de joindre le serveur.");
  });

  it("n'affiche pas de bouton sans action de rechargement", () => {
    render(<ErrorState message="Erreur" />);
    expect(screen.queryByRole("button", { name: "Réessayer" })).toBeNull();
  });

  it("appelle l'action de rechargement au clic sur Réessayer", async () => {
    const onRetry = vi.fn();
    render(<ErrorState message="Erreur" onRetry={onRetry} />);

    await userEvent.click(screen.getByRole("button", { name: "Réessayer" }));

    expect(onRetry).toHaveBeenCalledTimes(1);
  });
});

describe("EmptyState", () => {
  it("affiche son contenu", () => {
    render(<EmptyState>Rien à afficher</EmptyState>);
    expect(screen.getByText("Rien à afficher")).toBeInTheDocument();
  });
});
