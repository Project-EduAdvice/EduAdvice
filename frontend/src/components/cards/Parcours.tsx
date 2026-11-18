import { Link } from "react-router-dom";
import { ArrowRight, Building2 } from "lucide-react";
import type { ReactNode } from "react";
import type { Parcours as ParcoursData } from "../../services/types";

function Tag({ children, tone = "muted" }: { children: ReactNode; tone?: "muted" | "accent" }) {
  return (
    <span
      className={`inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-medium ${
        tone === "accent" ? "bg-accent-soft text-primary" : "bg-muted text-muted-foreground"
      }`}
    >
      {children}
    </span>
  );
}

export function Parcours({ p }: { p: ParcoursData }) {
  return (
    <Link to={`/programmes/${p.id}`} className="card-surface card-interactive group flex flex-col p-6">
      <div className="flex flex-wrap gap-2">
        <Tag tone="accent">{p.level}</Tag>
        {p.mention && <Tag>{p.mention}</Tag>}
      </div>
      <h3 className="mt-4 text-xl font-semibold">{p.name}</h3>
      <p className="mt-3 line-clamp-3 flex-1 text-sm text-muted-foreground">{p.description ?? ""}</p>
      <div className="mt-5 flex items-center justify-between border-t border-border pt-4 text-sm">
        <span className="flex items-center gap-1.5 text-muted-foreground">
          <Building2 className="size-4" />
          {p.establishment}
        </span>
        <span className="flex items-center gap-1 font-semibold text-accent">
          Découvrir <ArrowRight className="size-4 transition-transform group-hover:translate-x-1" />
        </span>
      </div>
    </Link>
  );
}