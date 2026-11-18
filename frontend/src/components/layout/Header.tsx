import { Link, useLocation } from "react-router-dom";
import { GraduationCap, Menu, X } from "lucide-react";
import { useState } from "react";

const nav = [
  { to: "/", label: "Accueil", isActive: (path: string, hash: string) => path === "/" && hash !== "#formations" },
  {
    to: "/#formations",
    label: "Formations",
    isActive: (path: string, hash: string) =>
      (path === "/" && hash === "#formations") || path.startsWith("/programmes") || path.startsWith("/ue"),
  },
  { to: "/metiers", label: "Métiers", isActive: (path: string) => path.startsWith("/metiers") },
];

const styles = {
  desktop: {
    base: "rounded-full px-4 py-2 text-sm font-medium transition-colors hover:text-foreground",
    active: "bg-secondary text-foreground",
    inactive: "text-muted-foreground",
  },
  mobile: {
    base: "block rounded-lg px-3 py-2 text-sm font-medium",
    active: "bg-secondary",
    inactive: "",
  },
};

function NavLinks({ variant, onNavigate }: { variant: keyof typeof styles; onNavigate?: () => void }) {
  const { pathname, hash } = useLocation();
  const s = styles[variant];
  return nav.map((n) => {
    const active = n.isActive(pathname, hash);
    return (
      <Link
        key={n.to}
        to={n.to}
        onClick={onNavigate}
        aria-current={active ? "page" : undefined}
        className={`${s.base} ${active ? s.active : s.inactive}`}
      >
        {n.label}
      </Link>
    );
  });
}

export function Header() {
  const [open, setOpen] = useState(false);
  return (
    <header className="sticky top-0 z-40 border-b border-border bg-background/85 backdrop-blur">
      <div className="flex h-16 items-center justify-between px-4 sm:px-6 lg:px-[10%]">
        <Link to="/" className="flex items-center gap-2 font-display text-lg font-bold text-primary">
          <span className="grid size-9 place-items-center rounded-xl bg-primary text-primary-foreground"><GraduationCap className="size-5" /></span>
          EduAdvice
        </Link>
        <nav className="hidden items-center gap-1 md:flex" aria-label="Main">
          <NavLinks variant="desktop" />
        </nav>
        <button className="rounded-lg p-2 md:hidden" onClick={() => setOpen(!open)} aria-label="Ouvrir ou fermer le menu" aria-expanded={open}>{open ? <X /> : <Menu />}</button>
      </div>
      {open && (
        <nav className="border-t border-border px-4 py-3 md:hidden" aria-label="Mobile">
          <NavLinks variant="mobile" onNavigate={() => setOpen(false)} />
        </nav>
      )}
    </header>
  );
}