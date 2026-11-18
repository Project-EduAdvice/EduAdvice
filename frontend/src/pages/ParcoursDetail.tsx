import { Link, useParams, useSearchParams } from "react-router-dom";
import { isAxiosError } from "axios";
import { ArrowRight, Award, Building2, GraduationCap, Layers } from "lucide-react";
import type { ReactNode } from "react";
import { Breadcrumb } from "../components/Breadcrumb";
import { CareerCard } from "../components/cards/Career";
import { EmptyState, ErrorState, LoadingState } from "../components/states";
import { PageShell, SectionTitle } from "../components/layout/PageShell";
import { useAsync } from "../hooks/useAsync";
import { getErrorMessage } from "../services/api";
import { getCareerOpportunities, getCurriculum, getParcours } from "../services/parcours";
import type { SemesterWithUnits, TeachingUnit } from "../services/types";
import { CATEGORY_LABELS, plural } from "../utils/labels";
import { NotFound } from "./NotFound";

function InfoTile({ icon, label, value }: { icon: ReactNode; label: string; value: string }) {
  return (
    <div className="flex items-center gap-3 rounded-2xl bg-primary-foreground/10 px-4 py-3">
      <span className="text-accent" aria-hidden="true">
        {icon}
      </span>
      <div className="min-w-0">
        <p className="text-xs text-primary-foreground/70">{label}</p>
        <p className="truncate font-semibold">{value}</p>
      </div>
    </div>
  );
}

function UnitCard({ ue }: { ue: TeachingUnit }) {
  return (
    <Link to={`/ue/${ue.id}`} className="card-surface card-interactive group flex flex-col p-5">
      <div className="flex items-center justify-between gap-2">
        <p className="flex items-center gap-1.5 text-sm text-muted-foreground">
          <Layers className="size-4" aria-hidden="true" />
          {CATEGORY_LABELS[ue.category] ?? ue.category}
        </p>
        {ue.ects !== null && (
          <span className="rounded-full bg-accent-soft px-2.5 py-0.5 text-xs font-medium text-primary">
            {ue.ects} ECTS
          </span>
        )}
      </div>
      <h3 className="mt-3 flex-1 text-lg font-semibold">{ue.name}</h3>
      <span className="mt-4 flex items-center gap-1 text-sm font-semibold text-accent">
        Voir les matières <ArrowRight className="size-4 transition-transform group-hover:translate-x-1" aria-hidden="true" />
      </span>
    </Link>
  );
}

function Curriculum({ semesters }: { semesters: SemesterWithUnits[] }) {
  const [params, setParams] = useSearchParams();

  if (semesters.length === 0) {
    return <EmptyState>Le cursus de ce parcours sera bientôt disponible.</EmptyState>;
  }

  const current = semesters.find((s) => s.number === Number(params.get("semestre"))) ?? semesters[0];
  const totalEcts = current.teachingUnits.reduce((sum, ue) => sum + (ue.ects ?? 0), 0);

  return (
    <>
      <div className="flex flex-wrap gap-2" role="group" aria-label="Choix du semestre">
        {semesters.map((s) => (
          <button
            key={s.id}
            type="button"
            aria-pressed={s.id === current.id}
            onClick={() => setParams({ semestre: String(s.number) }, { replace: true })}
            className={`rounded-full px-4 py-2 text-sm font-semibold transition-colors ${
              s.id === current.id
                ? "bg-primary text-primary-foreground"
                : "bg-secondary text-secondary-foreground hover:bg-accent-soft"
            }`}
          >
            Semestre {s.number}
          </button>
        ))}
      </div>

      <p className="mt-5 text-sm text-muted-foreground">
        {plural(current.teachingUnits.length, "unité d'enseignement", "unités d'enseignement")} ·{" "}
        {totalEcts} ECTS
      </p>

      {current.teachingUnits.length === 0 ? (
        <EmptyState>Aucune unité d'enseignement pour ce semestre.</EmptyState>
      ) : (
        <div className="mt-4 grid gap-5 sm:grid-cols-2 xl:grid-cols-3">
          {current.teachingUnits.map((ue) => (
            <UnitCard key={ue.id} ue={ue} />
          ))}
        </div>
      )}
    </>
  );
}

export function ParcoursDetail() {
  const { id = "" } = useParams();
  // Un id qui n'est pas un nombre ne peut pas exister : inutile d'interroger le backend
  if (!/^\d+$/.test(id)) return <NotFound />;
  // key={id} : tout repart de zéro quand on passe d'un parcours à un autre
  return <ParcoursDetailContent key={id} id={id} />;
}

function ParcoursDetailContent({ id }: { id: string }) {
  const parcoursQuery = useAsync(() => getParcours(id));
  const curriculumQuery = useAsync(() => getCurriculum(id));
  const careersQuery = useAsync(() => getCareerOpportunities(id));

  if (parcoursQuery.state.status === "loading") {
    return (
      <PageShell>
        <LoadingState label="Chargement du parcours…" />
      </PageShell>
    );
  }

  if (parcoursQuery.state.status === "error") {
    const { error } = parcoursQuery.state;
    if (isAxiosError(error) && error.response?.status === 404) return <NotFound />;
    return (
      <PageShell>
        <ErrorState message={getErrorMessage(error)} onRetry={parcoursQuery.reload} />
      </PageShell>
    );
  }

  const parcours = parcoursQuery.state.data;
  const curriculum = curriculumQuery.state;
  const careers = careersQuery.state;

  return (
    <PageShell>
      <Breadcrumb items={[{ label: "Formations", to: "/#formations" }, { label: parcours.name }]} />

      <section className="rounded-3xl bg-primary p-6 text-primary-foreground sm:p-8">
        <span className="inline-flex rounded-full bg-accent px-3 py-1 text-xs font-semibold text-accent-foreground">
          {parcours.level}
        </span>
        <h1 className="mt-4 text-4xl font-bold sm:text-5xl">{parcours.name}</h1>
        {parcours.description && (
          <p className="mt-3 max-w-2xl text-primary-foreground/80">{parcours.description}</p>
        )}
        <div className="mt-6 grid gap-3 sm:grid-cols-3">
          <InfoTile icon={<GraduationCap className="size-5" />} label="Diplôme" value={parcours.diploma} />
          {parcours.mention && (
            <InfoTile icon={<Award className="size-5" />} label="Mention" value={parcours.mention} />
          )}
          <InfoTile icon={<Building2 className="size-5" />} label="Établissement" value={parcours.establishment} />
        </div>
      </section>

      <section className="mt-12">
        <SectionTitle eyebrow="Cursus" title="Semestres et unités d'enseignement">
          Choisissez un semestre pour voir les unités d'enseignement qu'il contient.
        </SectionTitle>
        {curriculum.status === "loading" && <LoadingState label="Chargement du cursus…" />}
        {curriculum.status === "error" && (
          <ErrorState message={getErrorMessage(curriculum.error)} onRetry={curriculumQuery.reload} />
        )}
        {curriculum.status === "success" && <Curriculum semesters={curriculum.data} />}
      </section>

      <section id="metiers" className="mt-12 scroll-mt-20">
        <SectionTitle eyebrow="Après vos études" title="Métiers et débouchés">
          Les métiers auxquels ce parcours prépare.
        </SectionTitle>
        {careers.status === "loading" && <LoadingState label="Chargement des métiers…" />}
        {careers.status === "error" && (
          <ErrorState message={getErrorMessage(careers.error)} onRetry={careersQuery.reload} />
        )}
        {careers.status === "success" &&
          (careers.data.length === 0 ? (
            <EmptyState>Les débouchés de ce parcours seront bientôt disponibles.</EmptyState>
          ) : (
            <div className="grid gap-5 sm:grid-cols-2 xl:grid-cols-3">
              {careers.data.map((c) => (
                <CareerCard key={c.id} career={c} />
              ))}
            </div>
          ))}
      </section>
    </PageShell>
  );
}