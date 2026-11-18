# EduAdvice – Frontend

Interface web de la plateforme **EduAdvice** : elle présente les parcours universitaires du Master Informatique (Université Sorbonne Paris Nord, parcours P2S et EI2D), leurs semestres, unités d'enseignement, cours, ressources pédagogiques et débouchés professionnels.

Toutes les informations affichées proviennent du backend Spring Boot (API REST).

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
- Le backend EduAdvice lancé en local sur le port 8080

```bash
nvm use
```

## Installation

```bash
npm ci
cp .env.example .env
```

## Lancer le projet

1. **Base de données et backend** (depuis le dossier `backend/`) :

```bash
export DB_PASSWORD=<mot_de_passe>
docker compose up -d
./mvnw spring-boot:run
```

Les données de départ (parcours, semestres, unités d'enseignement, cours, ressources, métiers) sont créées automatiquement au premier lancement.

2. **Frontend** (depuis le dossier `frontend/`) :

```bash
npm run dev
```

L'application est disponible sur http://localhost:5173.

En développement, les appels `/api` sont redirigés par Vite vers `http://localhost:8080` (proxy défini dans `vite.config.ts`). Cela évite les problèmes de CORS.

## Variables d'environnement

| Variable            | Description               | Valeur par défaut |
| ------------------- | ------------------------- | ----------------- |
| `VITE_API_BASE_URL` | URL de base de l'API REST | `/api`            |

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
│   ├── cards/          # Cartes réutilisables (Parcours, Career)
│   ├── layout/         # Header, Footer, PageShell
│   ├── Breadcrumb.tsx  # Fil d'Ariane
│   └── states.tsx      # États de chargement, d'erreur et de liste vide
├── hooks/
│   └── useAsync.ts     # Chargement de données (loading / error / success + réessayer)
├── pages/              # Pages de l'application
├── services/           # Appels API (Axios) et types des données
├── utils/              # Libellés et fonctions utilitaires
├── App.tsx             # Page d'accueil
├── router.tsx          # Routes (React Router)
├── main.tsx            # Point d'entrée
└── styles.css          # Design system (couleurs, polices, utilitaires Tailwind)
```

## Routes

| Chemin            | Page                                                       |
| ----------------- | ---------------------------------------------------------- |
| `/`               | Accueil (recherche et liste des formations, `#formations`) |
| `/programmes/:id` | Détail d'un parcours : cursus par semestre et métiers      |
| `/ue/:id`         | Détail d'une unité d'enseignement et de ses cours          |
| `/cours/:id`      | Détail d'un cours : contenu et ressources                  |
| `/metiers`        | Tous les métiers, avec le parcours associé                 |
| `*`               | Page 404                                                   |

Le semestre affiché sur la page d'un parcours est conservé dans l'adresse (`?semestre=4`), ce qui permet de revenir au bon semestre depuis le fil d'Ariane.

## API utilisée

Le frontend consomme l'API du backend (préfixe `/api`, format JSON) :

| Endpoint                                  | Utilisation                         |
| ----------------------------------------- | ----------------------------------- |
| `GET /parcours`                           | Liste des formations (accueil)      |
| `GET /parcours/{id}`                      | Détail d'un parcours                |
| `GET /parcours/{id}/semesters`            | Semestres d'un parcours             |
| `GET /parcours/{id}/career-opportunities` | Métiers d'un parcours               |
| `GET /semesters/{id}/teaching-units`      | Unités d'enseignement d'un semestre |
| `GET /teaching-units/{id}`                | Détail d'une unité d'enseignement   |
| `GET /teaching-units/{id}/semester`       | Semestre (et parcours) d'une unité  |
| `GET /teaching-units/{id}/subjects`       | Cours d'une unité d'enseignement    |
| `GET /subjects/{id}`                      | Détail d'un cours                   |
| `GET /subjects/{id}/teaching-unit`        | Unité d'enseignement d'un cours     |
| `GET /subjects/{id}/learning-resources`   | Ressources d'un cours               |
| `GET /career-opportunities`               | Liste de tous les métiers           |

Les erreurs de l'API (404, serveur injoignable, etc.) sont affichées avec un bouton « Réessayer ».

## Problèmes fréquents

| Symptôme                                            | Solution                                                               |
| --------------------------------------------------- | ---------------------------------------------------------------------- |
| `ECONNREFUSED 127.0.0.1:8080` dans le terminal Vite | Le backend n'est pas lancé : le démarrer (voir « Lancer le projet »)   |
| « Impossible de joindre le serveur » sur le site    | Même cause : vérifier que le backend tourne sur le port 8080           |
| `port 5432 already in use` avec Docker              | Un PostgreSQL local occupe déjà le port : l'arrêter ou changer le port |

## Conventions

- Branches : `feat/frontend/<nom>`
- Commits : `feat(frontend): description`
- Avant chaque PR : `npm run lint && npm run build`
