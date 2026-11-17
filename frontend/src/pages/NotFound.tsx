import { Link } from "react-router-dom";
import { PageShell, SectionTitle } from "../components/layout/PageShell";

export function NotFound() {
  return (
    <PageShell>
      <SectionTitle eyebrow="Erreur 404" title="Page introuvable">
        La page que vous cherchez n'existe pas ou a été déplacée.
      </SectionTitle>
      <Link to="/" className="font-semibold text-accent hover:underline">
        Retour à l'accueil
      </Link>
    </PageShell>
  );
}