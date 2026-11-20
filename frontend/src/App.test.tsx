import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { MemoryRouter } from "react-router-dom";
import { beforeEach, describe, expect, it, vi } from "vitest";
import App from "./App";
import { getParcoursList } from "./services/parcours";
import { ei2d, networkError, p2s } from "./test/fixtures";

vi.mock("./services/parcours");

const renderHome = () =>
  render(
    <MemoryRouter>
      <App />
    </MemoryRouter>,
  );

const searchBox = () => screen.getByRole("textbox", { name: "Rechercher un parcours" });

describe("page d'accueil", () => {
  beforeEach(() => {
    vi.resetAllMocks();
    vi.mocked(getParcoursList).mockResolvedValue([p2s, ei2d]);
  });

  it("affiche un chargement puis les parcours du backend", async () => {
    renderHome();
    expect(screen.getByRole("status")).toHaveTextContent("Chargement des parcours…");

    expect(await screen.findByRole("heading", { name: "P2S" })).toBeInTheDocument();
    expect(screen.getByRole("heading", { name: "EI2D" })).toBeInTheDocument();
    expect(screen.getByRole("link", { name: /P2S/ })).toHaveAttribute("href", "/programmes/2");
  });

  it("filtre les parcours en tapant dans la recherche", async () => {
    renderHome();
    await screen.findByRole("heading", { name: "P2S" });

    await userEvent.type(searchBox(), "intelligence");

    expect(screen.getByRole("heading", { name: "EI2D" })).toBeInTheDocument();
    expect(screen.queryByRole("heading", { name: "P2S" })).toBeNull();
    expect(screen.getByRole("status")).toHaveTextContent("1 résultat pour « intelligence »");
  });

  it("ignore les accents et la casse", async () => {
    renderHome();
    await screen.findByRole("heading", { name: "P2S" });

    await userEvent.type(searchBox(), "SURETE");

    expect(screen.getByRole("heading", { name: "P2S" })).toBeInTheDocument();
    expect(screen.queryByRole("heading", { name: "EI2D" })).toBeNull();
  });

  it("indique quand aucun parcours ne correspond, puis permet de réinitialiser", async () => {
    renderHome();
    await screen.findByRole("heading", { name: "P2S" });

    await userEvent.type(searchBox(), "zzz");
    expect(screen.getByText("Aucun parcours ne correspond à votre recherche.")).toBeInTheDocument();

    await userEvent.click(screen.getByRole("button", { name: "Réinitialiser" }));

    expect(searchBox()).toHaveValue("");
    expect(screen.getByRole("heading", { name: "P2S" })).toBeInTheDocument();
    expect(screen.getByRole("heading", { name: "EI2D" })).toBeInTheDocument();
  });

  it("affiche un message quand le backend ne renvoie aucun parcours", async () => {
    vi.mocked(getParcoursList).mockResolvedValue([]);
    renderHome();

    expect(await screen.findByText("Aucun parcours disponible pour le moment.")).toBeInTheDocument();
  });

  it("affiche l'erreur et permet de réessayer quand le backend est injoignable", async () => {
    vi.mocked(getParcoursList).mockRejectedValueOnce(networkError());
    renderHome();

    expect(await screen.findByRole("alert")).toHaveTextContent("Impossible de joindre le serveur");

    await userEvent.click(screen.getByRole("button", { name: "Réessayer" }));

    expect(await screen.findByRole("heading", { name: "P2S" })).toBeInTheDocument();
  });
});
