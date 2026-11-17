import { Link, NavLink } from "react-router-dom";
import { GraduationCap, Menu, X } from "lucide-react";
import { useState } from "react";

const nav = [
  { to: "/", label: "Accueil" },
  { to: "/programmes", label: "Programmes" },
  { to: "/metiers", label: "Métiers" },
  { to: "/apropos", label: "À propos" },
] as const;

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
          {nav.map((n) => (
            <NavLink key={n.to} to={n.to} end={n.to === "/"}
              className={({ isActive }) =>
                `rounded-full px-4 py-2 text-sm font-medium transition-colors hover:text-foreground ${isActive ? "bg-secondary text-foreground" : "text-muted-foreground"}`
              }>
              {n.label}
            </NavLink>
          ))}
        </nav>
        <button className="rounded-lg p-2 md:hidden" onClick={() => setOpen(!open)} aria-label="Ouvrir ou fermer le menu" aria-expanded={open}>{open ? <X /> : <Menu />}</button>
      </div>
      {open && (
        <nav className="border-t border-border px-4 py-3 md:hidden" aria-label="Mobile">
          {nav.map((n) => (
            <NavLink key={n.to} to={n.to} end={n.to === "/"} onClick={() => setOpen(false)}
              className={({ isActive }) =>
                `block rounded-lg px-3 py-2 text-sm font-medium ${isActive ? "bg-secondary" : ""}`
              }>
              {n.label}
            </NavLink>
          ))}
        </nav>
      )}
    </header>
  );
}