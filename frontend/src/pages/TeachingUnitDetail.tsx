import { Link, useParams } from "react-router-dom";
import { isAxiosError } from "axios";
import { ArrowRight, BookOpen } from "lucide-react";
import { Breadcrumb } from "../components/Breadcrumb";
import { PageShell, SectionTitle } from "../components/layout/PageShell";
import { EmptyState, ErrorState, LoadingState } from "../components/states";
import { useAsync } from "../hooks/useAsync";
import { getErrorMessage } from "../services/api";
import { getSubjects } from "../services/subjects";
import { getTeachingUnit, getTeachingUnitSemester } from "../services/teachingUnits";
import type { Subject } from "../services/types";
import { CATEGORY_LABELS } from "../utils/labels";
import { NotFound } from "./NotFound";

function SubjectCard({ subject }: { subject: Subject }) {
  return (
    <Link to={`/cours/${subject.id}`} className="card-surface card-interactive group flex gap-4 p-5">
      <span className="grid size-11 shrink-0 place-items-center rounded-full bg-accent-soft text-primary" aria-hidden="true">
        <BookOpen className="size-5" />
      </span>
      <div className="min-w-0 flex-1">
        <div className="flex items-start justify-between gap-2">
          <h3 className="font-semibold">{subject.name}</h3>
          {subject.ects !== null && (
            <span className="shrink-0 rounded-full bg-accent-soft px-2.5 py-0.5 text-xs font-medium text-primary">
              {subject.ects} ECTS
            </span>
          )}
        </div>
        {subject.description && <p className="mt-1.5 text-sm text-muted-foreground">{subject.description}</p>}
        <span className="mt-3 inline-flex items-center gap-1 text-sm font-medium text-primary">
          Voir le cours <ArrowRight className="size-4 transition-transform group-hover:translate-x-0.5" />
        </span>
      </div>
    </Link>
  );
}

export function TeachingUnitDetail() {
  const { id = "" } = useParams();
  // Un id qui n'est pas un nombre ne peut pas exister : inutile d'interroger le backend
  if (!/^\d+$/.test(id)) return <NotFound />;
  // key={id} : tout repart de zéro quand on passe d'une UE à une autre
  return <TeachingUnitContent key={id} id={id} />;
}

function TeachingUnitContent({ id }: { id: string }) {
  // L'UE et son semestre (avec le parcours) servent à l'en-tête et au fil d'Ariane
  const unitQuery = useAsync(async () => {
    const [unit, semester] = await Promise.all([getTeachingUnit(id), getTeachingUnitSemester(id)]);
    return { unit, semester };
  });
  const subjectsQuery = useAsync(() => getSubjects(id));

  if (unitQuery.state.status === "loading") {
    return (
      <PageShell>
        <LoadingState label="Chargement de l'unité d'enseignement…" />
      </PageShell>
    );
  }

  if (unitQuery.state.status === "error") {
    const { error } = unitQuery.state;
    if (isAxiosError(error) && error.response?.status === 404) return <NotFound />;
    return (
      <PageShell>
        <ErrorState message={getErrorMessage(error)} onRetry={unitQuery.reload} />
      </PageShell>
    );
  }

  const { unit, semester } = unitQuery.state.data;
  const subjects = subjectsQuery.state;

  return (
    <PageShell>
      <Breadcrumb
        items={[
          { label: "Formations", to: "/#formations" },
          { label: semester.parcours.name, to: `/programmes/${semester.parcours.id}` },
          { label: `Semestre ${semester.number}`, to: `/programmes/${semester.parcours.id}?semestre=${semester.number}` },
          { label: unit.name },
        ]}
      />

      <section className="card-surface p-6 sm:p-8">
        <div className="flex flex-wrap items-center gap-2">
          <span className="rounded-full bg-muted px-2.5 py-0.5 text-xs font-medium text-muted-foreground">
            {CATEGORY_LABELS[unit.category] ?? unit.category}
          </span>
          {unit.ects !== null && (
            <span className="rounded-full bg-accent-soft px-2.5 py-0.5 text-xs font-medium text-primary">
              {unit.ects} ECTS
            </span>
          )}
        </div>
        <h1 className="mt-3 text-3xl font-bold sm:text-4xl">{unit.name}</h1>
        <p className="mt-2 text-muted-foreground">
          <Link to={`/programmes/${semester.parcours.id}`} className="hover:text-foreground hover:underline">
            {semester.parcours.name}
          </Link>{" "}
          · Semestre {semester.number}
        </p>
      </section>

      <section className="mt-12">
        <SectionTitle eyebrow="Matières" title="Cours enseignés" />
        {subjects.status === "loading" && <LoadingState label="Chargement des matières…" />}
        {subjects.status === "error" && (
          <ErrorState message={getErrorMessage(subjects.error)} onRetry={subjectsQuery.reload} />
        )}
        {subjects.status === "success" &&
          (subjects.data.length === 0 ? (
            <EmptyState>Les matières de cette unité d'enseignement seront bientôt disponibles.</EmptyState>
          ) : (
            <div className="grid gap-5 sm:grid-cols-2">
              {subjects.data.map((s) => (
                <SubjectCard key={s.id} subject={s} />
              ))}
            </div>
          ))}
      </section>
    </PageShell>
  );
}