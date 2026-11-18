import { Fragment } from "react";
import { Link } from "react-router-dom";
import { ChevronRight, House } from "lucide-react";

type Crumb = { label: string; to?: string };

// Fil d'Ariane : l'accueil, puis les étapes ; la dernière est la page courante
export function Breadcrumb({ items }: { items: Crumb[] }) {
  return (
    <nav aria-label="Fil d'Ariane" className="mb-6 flex flex-wrap items-center gap-2 text-sm text-muted-foreground">
      <Link to="/" aria-label="Accueil" className="hover:text-foreground">
        <House className="size-4" />
      </Link>
      {items.map((item, i) => (
        <Fragment key={`${i}-${item.label}`}>
          <ChevronRight className="size-3.5 shrink-0" aria-hidden="true" />
          {i === items.length - 1 ? (
            <span aria-current="page" className="font-semibold text-foreground">
              {item.label}
            </span>
          ) : item.to ? (
            <Link to={item.to} className="hover:text-foreground">
              {item.label}
            </Link>
          ) : (
            <span>{item.label}</span>
          )}
        </Fragment>
      ))}
    </nav>
  );
}