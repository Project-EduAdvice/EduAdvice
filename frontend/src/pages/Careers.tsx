import { CareerCard } from "../components/cards/Career";
import { PageShell, SectionTitle } from "../components/layout/PageShell";
import { EmptyState, ErrorState, LoadingState } from "../components/states";
import { useAsync } from "../hooks/useAsync";
import { getErrorMessage } from "../services/api";
import { getCareersWithParcours } from "../services/careers";

export function Careers() {
  const { state, reload } = useAsync(getCareersWithParcours);

  return (
    <PageShell>
      <SectionTitle eyebrow="Après vos études" title="Métiers et débouchés">
        Tous les métiers vers lesquels mènent nos parcours.
      </SectionTitle>
      {state.status === "loading" && <LoadingState label="Chargement des métiers…" />}
      {state.status === "error" && <ErrorState message={getErrorMessage(state.error)} onRetry={reload} />}
      {state.status === "success" &&
        (state.data.length === 0 ? (
          <EmptyState>Les métiers seront bientôt disponibles.</EmptyState>
        ) : (
          <div className="grid gap-5 sm:grid-cols-2 xl:grid-cols-3">
            {state.data.map((c) => (
              <CareerCard key={c.id} career={c} parcoursNames={c.parcoursNames} />
            ))}
          </div>
        ))}
    </PageShell>
  );
}