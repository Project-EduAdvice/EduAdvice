# EduAdvice — Frontend

Application frontend du projet **EduAdvice**, développée avec React et Vite.

## Technologies utilisées

- **React 19** — bibliothèque JavaScript pour la création d'interfaces utilisateur.
- **Vite 8** — outil de développement et de compilation.
- **Tailwind CSS 4** — framework CSS pour la mise en forme.
- **Oxlint** — outil d'analyse statique et de qualité du code.
- **Node.js** et **npm** — environnement d'exécution et gestionnaire de dépendances.

## Prérequis

Avant de commencer, assurez-vous d'avoir installé :

- Node.js 24 LTS (version 24.x).
- npm (fourni avec Node.js).

Vérifier les versions installées :

```bash
node -v
npm -v
```
## Utiliser la même version de Node.js

Le fichier .nvmrc, situé à la racine du dossier frontend/, définit la version de Node.js utilisée par le projet.

Pour installer et activer automatiquement cette version avec NVM, exécuter depuis le dossier frontend/ :

```bash
nvm install
nvm use
```
## Installation

Depuis la racine du dépôt EduAdvice, accéder au dossier frontend :

```bash
cd frontend
```

Installer les dépendances du projet :

```bash
npm ci
```

Cette commande installe les dépendances à partir du fichier `package-lock.json` afin de garantir un environnement cohérent pour tous les membres de l'équipe.

## Lancer le serveur de développement

```bash
npm run dev
```

Le serveur de développement est généralement accessible à l'adresse :

```text
http://localhost:5173/
```

## Compiler le projet

Pour générer la version de production :

```bash
npm run build
```

Les fichiers générés sont placés dans le dossier `dist/`.

## Vérification de la qualité du code

Pour lancer l'analyse statique avec Oxlint :

```bash
npm run lint
```

## Variables d'environnement

Créer un fichier `.env` à partir du fichier `.env.example` :

```bash
cp .env.example .env
```

Configurer ensuite l'URL de l'API backend :

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

Ne jamais versionner les fichiers `.env` contenant des configurations locales ou des informations sensibles.

## Structure du projet

```text
frontend/
├── public/
├── src/
│   ├── assets/
│   ├── components/
│   ├── hooks/
│   ├── layouts/
│   ├── pages/
│   ├── services/
│   ├── utils/
│   ├── App.jsx
│   ├── App.css
│   ├── index.css
│   └── main.jsx
├── .env.example
├── .gitignore
├── index.html
├── package.json
├── package-lock.json
└── vite.config.js
```

## Scripts disponibles

| Commande | Description |
|---|---|
| `npm run dev` | Lance le serveur de développement. |
| `npm run build` | Compile le projet pour la production. |
| `npm run lint` | Vérifie la qualité du code avec Oxlint. |
| `npm run preview` | Prévisualise la version compilée. |

---

*EduAdvice — Projet universitaire de Master P2S.*





# React + Vite

This template provides a minimal setup to get React working in Vite with HMR and some Oxlint rules.

Currently, two official plugins are available:

- [@vitejs/plugin-react](https://github.com/vitejs/vite-plugin-react/blob/main/packages/plugin-react) uses [Oxc](https://oxc.rs)
- [@vitejs/plugin-react-swc](https://github.com/vitejs/vite-plugin-react/blob/main/packages/plugin-react-swc) uses [SWC](https://swc.rs/)

## React Compiler

The React Compiler is not enabled on this template because of its impact on dev & build performances. To add it, see [this documentation](https://react.dev/learn/react-compiler/installation).

## Expanding the Oxlint configuration

If you are developing a production application, we recommend using TypeScript with type-aware lint rules enabled. Check out the [TS template](https://github.com/vitejs/vite/tree/main/packages/create-vite/template-react-ts) for information on how to integrate TypeScript and Oxlint's TypeScript related rules in your project.
