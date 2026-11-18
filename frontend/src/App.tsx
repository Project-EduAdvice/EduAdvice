import { Search, X } from "lucide-react";
import { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import { PageShell, SectionTitle } from "./components/layout/PageShell";
import { Parcours } from "./components/cards/Parcours";
import { EmptyState, ErrorState, LoadingState } from "./components/states";
import { useAsync } from "./hooks/useAsync";
import { getErrorMessage } from "./services/api";
import { getParcoursList } from "./services/parcours";
import type { Parcours as ParcoursData } from "./services/types";
import { normalize } from "./utils/text";

// Cartes des parcours, filtrées par la recherche (nom, description, mention)
function ParcoursList({ parcours, term, onReset }: { parcours: ParcoursData[]; term: string; onReset: () => void }) {
  if (parcours.length === 0) return <EmptyState>Aucun parcours disponible pour le moment.</EmptyState>;

  const search = normalize(term.trim());
  const shown = search
    ? parcours.filter((p) => normalize([p.name, p.description ?? "", p.mention ?? ""].join(" ")).includes(search))
    : parcours;

  return (
    <>
      {search && (
        <div className="mb-4 flex items-center justify-between gap-3">
          <p role="status" className="text-sm text-muted-foreground">
            {shown.length} résultat{shown.length > 1 ? "s" : ""} pour « {term.trim()} »
          </p>
          <button type="button" onClick={onReset} className="flex items-center gap-1 text-sm font-semibold text-accent hover:underline">
            <X className="size-4" aria-hidden="true" />
            Réinitialiser
          </button>
        </div>
      )}
      {shown.length === 0 ? (
        <EmptyState>Aucun parcours ne correspond à votre recherche.</EmptyState>
      ) : (
        <div className="grid gap-5 sm:grid-cols-2">
          {shown.map((p) => (
            <Parcours key={p.id} p={p} />
          ))}
        </div>
      )}
    </>
  );
}

const STEPS: [string, string][] = [
  ["Choisissez votre parcours", "Trouvez votre parcours par son nom."],
  ["Choisissez un semestre", "Consultez chaque unité d'enseignement et ses crédits ECTS."],
  ["Ouvrez un cours", "Parcourez les matières de chaque unité d'enseignement."],
  ["Apprenez et projetez-vous", "Utilisez des ressources choisies et explorez les métiers."],
];

export default function App() {
  const [term, setTerm] = useState("");
  const { state, reload } = useAsync(getParcoursList);
  const { hash } = useLocation();

  // Une fois les cartes chargées, on recale la page sur la section demandée (/#formations)
  useEffect(() => {
    if (state.status === "success" && hash === "#formations") {
      document.getElementById("formations")?.scrollIntoView();
    }
  }, [state.status, hash]);

  return (
    <PageShell>
      <section>
        <SectionTitle eyebrow="Bienvenue" title="EduAdvice">
          Parcourez nos deux parcours de formation en informatique semestre par semestre, explorez
          leurs cours et une sélection de ressources, puis découvrez les métiers auxquels ils
          préparent.
        </SectionTitle>
        <form
          role="search"
          className="flex gap-2 rounded-2xl border border-border bg-card p-2 shadow-soft"
          onSubmit={(e) => {
            e.preventDefault();
            // Les cartes se filtrent déjà en tapant : on descend simplement jusqu'à elles
            document.getElementById("formations")?.scrollIntoView();
          }}
        >
          <Search className="ml-2 mt-2.5 size-5 shrink-0 text-muted-foreground" />
          <input
            value={term}
            onChange={(e) => setTerm(e.target.value)}
            placeholder="Rechercher un parcours"
            aria-label="Rechercher un parcours"
            className="min-w-0 flex-1 bg-transparent px-1 outline-none placeholder:text-muted-foreground"
          />
          <button
            type="submit"
            className="rounded-xl bg-primary px-4 py-2 text-sm font-medium text-primary-foreground transition-colors hover:bg-primary/90"
          >
            Rechercher
          </button>
        </form>
      </section>

      <section className="mt-24">
        <SectionTitle
          eyebrow="Comment ça marche"
          title="Du parcours aux ressources de cours en quelques clics"
        />
        <ol className="grid gap-5 sm:grid-cols-2 xl:grid-cols-4">
          {STEPS.map(([title, text], i) => (
            <li key={title} className="card-surface p-6">
              <span className="font-display text-3xl font-extrabold text-accent">0{i + 1}</span>
              <p className="mt-3 font-semibold">{title}</p>
              <p className="mt-1 text-sm text-muted-foreground">{text}</p>
            </li>
          ))}
        </ol>
      </section>

      <section id="formations" className="mt-24 scroll-mt-20">
        <SectionTitle eyebrow="Formations" title="Nos parcours de formation" />
        {state.status === "loading" && <LoadingState label="Chargement des parcours…" />}
        {state.status === "error" && <ErrorState message={getErrorMessage(state.error)} onRetry={reload} />}
        {state.status === "success" && <ParcoursList parcours={state.data} term={term} onReset={() => setTerm("")} />}
      </section>
    </PageShell>
  );
}