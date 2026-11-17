# EduAdvice – Frontend

Interface web de la plateforme **EduAdvice** : présentation des parcours universitaires (Master Informatique, Université Sorbonne Paris Nord – parcours P2S et EI2D), de leurs semestres, unités d'enseignement, matières, ressources et débouchés.

## Technologies

- React 19 + TypeScript
- Vite 8
- Tailwind CSS v4
- React Router v7 (`react-router-dom`)
- Axios (appels API)
- lucide-react (icônes)
- Oxlint (linter)

## Prérequis

- Node.js 24 (voir `.nvmrc`)
- Backend EduAdvice lancé en local (Spring Boot, port 8080) pour les appels API

```bash
nvm use
```

## Installation

```bash
npm ci
cp .env.example .env
```

## Variables d'environnement

| Variable            | Description                | Valeur par défaut           |
| ------------------- | -------------------------- | --------------------------- |
| `VITE_API_BASE_URL` | URL de base de l'API REST  | `http://localhost:8080/api` |

Le fichier `.env` n'est jamais versionné. Seul `.env.example` l'est.

## Scripts

| Commande          | Rôle                                              |
| ----------------- | ------------------------------------------------- |
| `npm run dev`     | Lance le serveur de développement                 |
| `npm run build`   | Vérifie les types (`tsc -b`) et construit le site |
| `npm run lint`    | Analyse le code avec Oxlint                       |
| `npm run preview` | Prévisualise le build de production               |

## Structure du projet

```text
src/
├── components/
│   ├── cards/        # Cartes (ex. Parcours)
│   └── layout/       # Header, Footer, PageShell
├── pages/            # Pages (Placeholder, NotFound)
├── services/         # Appels API (Axios) – à venir
├── App.tsx           # Page d'accueil
├── router.tsx        # Routes (React Router)
├── main.tsx          # Point d'entrée
└── styles.css        # Design system (tokens, utilitaires Tailwind)
```

## Routes

| Chemin        | Page                  |
| ------------- | --------------------- |
| `/`           | Accueil               |
| `/programmes` | Programmes            |
| `/metiers`    | Métiers et débouchés  |
| `/apropos`    | À propos              |
| `*`           | Page 404              |

## Conventions

- Branches : `feat/frontend/<nom>`
- Commits : `feat(frontend): description`
- Lint obligatoire avant chaque PR : `npm run lint && npm run build`

## API

Le frontend consomme l'API décrite dans `docs/api/api-contract.md` (préfixe `/api`, format JSON).
