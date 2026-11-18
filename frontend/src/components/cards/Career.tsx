import { Briefcase } from "lucide-react";
import type { CareerOpportunity } from "../../services/types";
import { splitList } from "../../utils/text";

export function CareerCard({ career, parcoursNames = [] }: { career: CareerOpportunity; parcoursNames?: string[] }) {
  const skills = splitList(career.required_skills);
  return (
    <article className="card-surface flex flex-col p-5">
      <div className="flex items-center gap-3">
        <span className="grid size-10 shrink-0 place-items-center rounded-full bg-primary text-primary-foreground" aria-hidden="true">
          <Briefcase className="size-5" />
        </span>
        <div className="min-w-0">
          <h3 className="font-semibold">{career.title}</h3>
          {career.sector && <p className="text-sm text-accent">{career.sector}</p>}
        </div>
        {parcoursNames.length > 0 && (
          <ul className="ml-auto flex shrink-0 flex-wrap justify-end gap-1.5 self-start" aria-label="Parcours">
            {parcoursNames.map((name) => (
              <li key={name} className="rounded-full bg-primary px-2.5 py-0.5 text-xs font-semibold text-primary-foreground">
                {name}
              </li>
            ))}
          </ul>
        )}
      </div>
      {career.description && <p className="mt-3 flex-1 text-sm text-muted-foreground">{career.description}</p>}
      {skills.length > 0 && (
        <ul className="mt-4 flex flex-wrap gap-2" aria-label="Compétences requises">
          {skills.map((skill) => (
            <li key={skill} className="rounded-full bg-accent-soft px-2.5 py-0.5 text-xs font-medium text-primary">
              {skill}
            </li>
          ))}
        </ul>
      )}
    </article>
  );
}