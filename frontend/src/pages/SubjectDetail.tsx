import { isAxiosError } from "axios";
import { ExternalLink, FileText, Globe, PlayCircle, type LucideIcon } from "lucide-react";
import { useParams } from "react-router-dom";
import { Breadcrumb } from "../components/Breadcrumb";
import { PageShell, SectionTitle } from "../components/layout/PageShell";
import { EmptyState, ErrorState, LoadingState } from "../components/states";
import { useAsync } from "../hooks/useAsync";
import { getErrorMessage } from "../services/api";
import { getLearningResources, getSubject, getSubjectTeachingUnit } from "../services/subjects";
import { getTeachingUnitSemester } from "../services/teachingUnits";
import type { LearningResource, ResourceType } from "../services/types";
import { RESOURCE_TYPE_LABELS } from "../utils/labels";
import { NotFound } from "./NotFound";

const RESOURCE_ICONS: Record<ResourceType, LucideIcon> = {
  DOCUMENT: FileText,
  VIDEO: PlayCircle,
  WEBSITE: Globe,
};

function ResourceCard({ resource }: { resource: LearningResource }) {
  const Icon = RESOURCE_ICONS[resource.resourceType] ?? Globe;
  return (
    <article className="card-surface flex gap-4 p-5">
      <span className="grid size-11 shrink-0 place-items-center rounded-full bg-accent-soft text-primary" aria-hidden="true">
        <Icon className="size-5" />
      </span>
      <div className="min-w-0 flex-1">
        <span className="rounded-full bg-muted px-2.5 py-0.5 text-xs font-medium text-muted-foreground">
          {RESOURCE_TYPE_LABELS[resource.resourceType] ?? resource.resourceType}
        </span>
        <h3 className="mt-2 font-semibold">{resource.title}</h3>
        {resource.description && <p className="mt-1 text-sm text-muted-foreground">{resource.description}</p>}
        <a
          href={resource.url}
          target="_blank"
          rel="noopener noreferrer"
          className="mt-3 inline-flex items-center gap-1 text-sm font-medium text-accent hover:underline"
        >
          Ouvrir la ressource <ExternalLink className="size-3.5" aria-hidden="true" />
        </a>
      </div>
    </article>
  );
}

export function SubjectDetail() {
  const { id = "" } = useParams();
  if (!/^\d+$/.test(id)) return <NotFound />;
  return <SubjectContent key={id} id={id} />;
}

function SubjectContent({ id }: { id: string }) {
  const subjectQuery = useAsync(async () => {
    const [subject, unit] = await Promise.all([getSubject(id), getSubjectTeachingUnit(id)]);
    const semester = await getTeachingUnitSemester(unit.id);
    return { subject, unit, semester };
  });
  const resourcesQuery = useAsync(() => getLearningResources(id));

  if (subjectQuery.state.status === "loading") {
    return (
      <PageShell>
        <LoadingState label="Chargement du cours…" />
      </PageShell>
    );
  }

  if (subjectQuery.state.status === "error") {
    const { error } = subjectQuery.state;
    if (isAxiosError(error) && error.response?.status === 404) return <NotFound />;
    return (
      <PageShell>
        <ErrorState message={getErrorMessage(error)} onRetry={subjectQuery.reload} />
      </PageShell>
    );
  }

  const { subject, unit, semester } = subjectQuery.state.data;
  const { parcours } = semester;
  const resources = resourcesQuery.state;

  return (
    <PageShell>
      <Breadcrumb
        items={[
          { label: "Formations", to: "/#formations" },
          { label: parcours.name, to: `/programmes/${parcours.id}` },
          { label: `Semestre ${semester.number}`, to: `/programmes/${parcours.id}?semestre=${semester.number}` },
          { label: unit.name, to: `/ue/${unit.id}` },
          { label: subject.name },
        ]}
      />

      <section className="rounded-2xl bg-accent-soft p-6 sm:p-8">
        <p className="text-xs font-semibold uppercase tracking-wider text-accent">
          {unit.name}
          {subject.ects !== null && ` · ${subject.ects} ECTS`}
        </p>
        <h1 className="mt-2 text-3xl font-bold sm:text-4xl">{subject.name}</h1>
        {subject.description && <p className="mt-3 max-w-3xl text-muted-foreground">{subject.description}</p>}
      </section>

      {subject.content && (
        <section className="mt-12">
          <SectionTitle eyebrow="Programme" title="Contenu du cours" />
          <p className="card-surface p-6 text-muted-foreground">{subject.content}</p>
        </section>
      )}

      <section className="mt-12">
        <SectionTitle eyebrow="Ressources" title="Supports de cours" />
        {resources.status === "loading" && <LoadingState label="Chargement des ressources…" />}
        {resources.status === "error" && (
          <ErrorState message={getErrorMessage(resources.error)} onRetry={resourcesQuery.reload} />
        )}
        {resources.status === "success" &&
          (resources.data.length === 0 ? (
            <EmptyState>Aucune ressource n'est disponible pour ce cours pour le moment.</EmptyState>
          ) : (
            <div className="grid gap-5 sm:grid-cols-2">
              {resources.data.map((r) => (
                <ResourceCard key={r.id} resource={r} />
              ))}
            </div>
          ))}
      </section>
    </PageShell>
  );
}