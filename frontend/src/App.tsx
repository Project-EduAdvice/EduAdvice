import { useNavigate } from "react-router-dom";
import { Search } from "lucide-react";
import { useState } from "react";
import { PageShell, SectionTitle } from "./components/layout/PageShell";
import { Parcours, type Program } from "./components/cards/Parcours";

// À remplacer par tes deux vrais parcours (puis, plus tard, par les données du backend)
const PROGRAMS: Program[] = [
  {
    id: 1,
    level: "Master",
    mention: "Informatique",
    name: "P2S",
    description: "Parcours Programmation Sûreté et Sécurité.",
    establishment: "Université Sorbonne Paris Nord",
  },
  {
    id: 2,
    level: "Master",
    mention: "Informatique",
    name: "EI2D",
    description: "Parcours Exploration informatique des données et décisionnel.",
    establishment: "Université Sorbonne Paris Nord",
  },
];

const STEPS: [string, string][] = [
  ["Choisissez votre parcours", "Trouvez votre parcours par son nom."],
  ["Choisissez un semestre", "Consultez chaque unité d'enseignement et ses crédits ECTS."],
  ["Ouvrez un cours", "Parcourez les matières de chaque unité d'enseignement."],
  ["Apprenez et projetez-vous", "Utilisez des ressources choisies et explorez les métiers."],
];

export default function App() {
  const [term, setTerm] = useState("");
  const navigate = useNavigate();

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
            const q = term.trim();
            navigate(q ? `/programmes?q=${encodeURIComponent(q)}` : "/programmes");
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

      <section className="mt-24">
        <SectionTitle eyebrow="Formations" title="Nos parcours de formation" />
        <div className="grid gap-5 sm:grid-cols-2">
          {PROGRAMS.map((p) => (
            <Parcours key={p.id} p={p} />
          ))}
        </div>
      </section>
    </PageShell>
  );
}
