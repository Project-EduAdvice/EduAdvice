# Guide de déploiement — EduAdvice

> **Responsable DevOps :** Oumar
> **Dernière mise à jour :** 19 novembre 2026
> **Document lié :** [Architecture de déploiement](../architecture/deployment-architecture.md) (le *pourquoi* des choix)

Ce guide explique **comment** lancer EduAdvice en local, comment il est déployé en production,
comment vérifier qu'il fonctionne, et ce qui a été appris en le mettant en place.

---

## Sommaire

1. [État d'avancement](#1-état-davancement)
2. [Environnement de production](#2-environnement-de-production)
3. [Prérequis](#3-prérequis)
4. [Lancement en local](#4-lancement-en-local)
5. [Configuration et secrets](#5-configuration-et-secrets)
6. [Procédure de déploiement](#6-procédure-de-déploiement)
7. [Vérifications après déploiement](#7-vérifications-après-déploiement)
8. [Migrations de base de données — règles pour l'équipe](#8-migrations-de-base-de-données--règles-pour-léquipe)
9. [Choix techniques réalisés](#9-choix-techniques-réalisés)
10. [Problèmes rencontrés et solutions](#10-problèmes-rencontrés-et-solutions)
11. [Limites connues](#11-limites-connues)
12. [Reste à faire](#12-reste-à-faire)

---

## 1. État d'avancement

| Phase | Contenu | Statut |
|---|---|---|
| 0 | Builds reproductibles (frontend + backend), base locale Docker, configuration unifiée | ✅ Terminé |
| 1 | [Architecture de déploiement](../architecture/deployment-architecture.md) documentée | ✅ Terminé (PR #7) |
| 2 | Backend conteneurisé (Docker) et déployé sur Render, base PostgreSQL sur Neon | ✅ Terminé (PR #12) |
| 3 | Frontend déployé sur Vercel, communication avec le backend via CORS | ✅ Terminé (PR #13) |
| 4 | Sauvegardes automatiques chiffrées + test de restauration | 🔜 À faire |
| 5 | CI/CD GitHub Actions (tests automatiques, déploiement depuis `main`, protection de branche) | 🔜 À faire |
| 6 | Durcissement sécurité (HSTS backend, endpoints Actuator), journalisation | 🔜 À faire |
| 7 | Validation finale | 🔜 À faire |

**Critères d'acceptation :**

| Critère | Statut |
|---|---|
| URL publique du frontend | ✅ https://eduadvice.vercel.app |
| URL publique du backend | ✅ https://eduadvice-api.onrender.com |
| HTTPS activé | ✅ Certificats gérés par Vercel et Render, redirection HTTP → HTTPS |
| Aucun secret dans le dépôt | ✅ Secrets uniquement dans les variables d'environnement des plateformes |
| Build et tests automatisés | 🔜 Phase 5 (les builds et les 115 tests passent déjà en local) |
| Processus de déploiement documenté | ✅ Ce document |

---

## 2. Environnement de production

| Composant | Hébergeur | URL / emplacement |
|---|---|---|
| Frontend (React + Vite) | Vercel, offre Hobby | https://eduadvice.vercel.app |
| Backend (Spring Boot, Java 21) | Render, offre Free, Docker, Francfort | https://eduadvice-api.onrender.com/api |
| Santé du backend | — | https://eduadvice-api.onrender.com/actuator/health |
| Base de données | Neon, offre Free, PostgreSQL 18, AWS eu-central-1 (Francfort) | Projet `eduadvice`, base `eduadvice` |

```
Navigateur ──HTTPS──► Vercel (fichiers statiques React)
    │
    └──HTTPS + CORS──► Render (conteneur Spring Boot) ──TLS──► Neon (PostgreSQL 18)
```

Le code React s'exécute **dans le navigateur** : c'est le navigateur, et non Vercel, qui appelle l'API.
Les deux sont sur des domaines différents, d'où la configuration **CORS** côté backend.

---

## 3. Prérequis

| Outil | Version | Usage |
|---|---|---|
| Git | récent | Cloner le dépôt |
| Docker Desktop | récent | Base PostgreSQL locale, construction de l'image backend |
| Java (JDK) | 21 | Backend (Maven est fourni via `./mvnw`, inutile de l'installer) |
| Node.js / npm | 24 LTS / 10+ | Frontend |
| Vercel CLI | via `npx vercel@latest` | Déploiement du frontend (DevOps uniquement) |

Si le dépôt a été cloné avant son transfert vers l'organisation, mettre à jour le remote :

```bash
git remote set-url origin https://github.com/Project-EduAdvice/EduAdvice.git
```

---

## 4. Lancement en local

### 4.1 Base de données

À la racine du dépôt :

```bash
docker compose up -d          # PostgreSQL 18 sur localhost:5432
docker compose ps             # attendre l'état "healthy"
```

> Si vous aviez une ancienne base locale (autre version ou autres identifiants), repartez de zéro :
> `docker compose down -v` puis `docker compose up -d`. Les variables `POSTGRES_*` ne sont lues
> qu'à la **première** initialisation du volume.

### 4.2 Backend

```bash
cd backend
DB_PASSWORD=eduadvice_dev ./mvnw clean verify          # compile + 115 tests
DB_PASSWORD=eduadvice_dev ./mvnw spring-boot:run       # http://localhost:8080
```

Au démarrage, **Flyway** crée ou met à jour le schéma automatiquement, puis des données d'exemple sont insérées.

> Toujours utiliser `clean` en cas de comportement étrange : d'anciens fichiers dans `target/`
> peuvent écraser la configuration actuelle.

### 4.3 Frontend

```bash
cd frontend
npm ci              # installe exactement les versions de package-lock.json
npm run dev         # http://localhost:5173
```

En local, Vite redirige `/api` vers `http://localhost:8080` (proxy dans `vite.config.ts`) :
aucune configuration CORS n'est nécessaire.

### 4.4 (Optionnel) Tester l'image Docker de production en local

```bash
docker build -t eduadvice-backend backend
docker run --rm -p 8080:8080 \
  -e DB_URL=jdbc:postgresql://host.docker.internal:5432/eduadvice \
  -e DB_PASSWORD=eduadvice_dev \
  -e SPRING_PROFILES_ACTIVE=prod \
  eduadvice-backend
```

Dans un conteneur, `localhost` désigne le conteneur lui-même : on utilise `host.docker.internal`
pour atteindre la base qui tourne sur la machine hôte.

---

## 5. Configuration et secrets

**Principe :** le même code tourne partout ; seule la configuration change, via des
**variables d'environnement** (*12-Factor App*). Le dépôt étant **public**, aucun secret n'y est jamais écrit.

### 5.1 Backend (variables d'environnement Render)

| Variable | Rôle | Valeur en local (par défaut) | Secret ? |
|---|---|---|---|
| `DB_URL` | URL JDBC de la base | `jdbc:postgresql://localhost:5432/eduadvice` | Oui |
| `DB_USERNAME` | Utilisateur PostgreSQL | `eduadvice_user` | Oui |
| `DB_PASSWORD` | Mot de passe PostgreSQL | *(obligatoire, `eduadvice_dev` en local)* | **Oui** |
| `SPRING_PROFILES_ACTIVE` | Active `application-prod.yaml` | *(vide)* → `prod` en production | Non |
| `CORS_ALLOWED_ORIGINS` | Origines autorisées à appeler l'API (séparées par des virgules) | `http://localhost:5173` | Non |
| `PORT` | Port d'écoute, imposé par Render | `8080` | Non |

Format de `DB_URL` en production (connexion **directe** Neon, TLS obligatoire) :

```
jdbc:postgresql://<hôte-neon>/eduadvice?sslmode=require
```

### 5.2 Frontend (variable du projet Vercel)

| Variable | Rôle | Local | Production |
|---|---|---|---|
| `VITE_API_BASE_URL` | Adresse de l'API | `/api` (proxy Vite) | `https://eduadvice-api.onrender.com/api` |

> ⚠️ Les variables `VITE_*` sont **copiées en clair dans le JavaScript** au moment du build.
> Elles sont lisibles par tout visiteur : **jamais de secret** dedans. Changer cette valeur impose
> de **reconstruire** le frontend.

### 5.3 Où sont stockés les secrets

- Variables de production du backend : tableau de bord **Render → Environment**.
- Variable du frontend : projet **Vercel → Settings → Environment Variables**.
- Mot de passe de la base : gestionnaire de mots de passe du responsable DevOps. Il ne transite ni par Git, ni par la messagerie.
- Fichiers ignorés par Git : `.env*` (sauf `.env.example`) et `.vercel/`.

---

## 6. Procédure de déploiement

> **Situation actuelle :** déploiement **manuel**, réalisé par le responsable DevOps, toujours
> depuis `main` à jour. La Phase 5 automatisera exactement ces étapes avec GitHub Actions.
> L'*auto-deploy* de Render est **désactivé** volontairement.

### 6.1 Base de données (Neon), configuration initiale déjà faite

| Paramètre | Valeur |
|---|---|
| Projet / région | `eduadvice` / AWS eu-central-1 (Francfort) |
| Version | PostgreSQL 18 |
| Base / rôle | `eduadvice` / `eduadvice_user` |
| Connexion | Directe (pas le *pooler*), TLS obligatoire (`sslmode=disable` est refusé) |

Le schéma n'est **jamais** créé à la main : ce sont les migrations Flyway, exécutées par le
backend déployé, qui le créent et le font évoluer.
On ne lance **pas** le backend local sur la base de production.

### 6.2 Backend (Render), configuration du service

| Paramètre | Valeur |
|---|---|
| Type | Web Service, runtime **Docker** |
| Dépôt / branche | `Project-EduAdvice/EduAdvice` / `main` |
| Root Directory | `backend` (contient le `Dockerfile`) |
| Région / offre | Frankfurt / Free |
| Health Check Path | `/actuator/health` |
| Auto-Deploy | Off |
| Variables | voir § 5.1 |

**Déployer une nouvelle version :**

1. Vérifier que la PR est mergée dans `main`.
2. Render → service `eduadvice-api` → **Manual Deploy → Deploy latest commit**.
3. Suivre les **Logs** jusqu'à `Started BackendApplication in … seconds` (environ 1 minute).
4. Faire les vérifications du § 7.

**Revenir en arrière :** Render → **Events** → choisir un déploiement précédent → **Rollback**.
Attention : une migration de base déjà appliquée n'est **pas** annulée par un rollback
(voir § 8).

### 6.3 Frontend (Vercel CLI)

Depuis `frontend/`, sur `main` à jour :

```bash
git switch main && git pull

cd frontend
npx vercel@latest login                                   # une seule fois
npx vercel@latest link                                    # une seule fois → projet "eduadvice"
npx vercel@latest pull --yes --environment=production     # récupère la config (dont VITE_API_BASE_URL)
npx vercel@latest build --prod                            # build local avec la config de prod
npx vercel@latest deploy --prebuilt --prod                # publie le résultat
```

Le fichier [`frontend/vercel.json`](../../frontend/vercel.json) renvoie `index.html` pour toute URL
inconnue. Sans lui, recharger une page comme `/programmes/1` donnerait une erreur 404, car React Router
gère ces routes **dans le navigateur**, pas sur le serveur.

**Revenir en arrière :** Vercel → **Deployments** → choisir un déploiement précédent → **Promote to Production**.

### 6.4 Ajouter une nouvelle origine CORS (ex. domaine personnalisé)

1. Render → Environment → `CORS_ALLOWED_ORIGINS` = `https://eduadvice.vercel.app,https://nouveau-domaine`
   (sans `/` final).
2. Enregistrer et redéployer le backend.

Le backend **refuse de démarrer** si la valeur contient `*`.

---

## 7. Vérifications après déploiement

```bash
API=https://eduadvice-api.onrender.com

# 1. Santé du backend → {"status":"UP"}
curl -s $API/actuator/health

# 2. L'API répond
curl -s $API/api/parcours | head -c 300

# 3. CORS : l'origine du frontend est autorisée → en-tête access-control-allow-origin
curl -s -i -X OPTIONS $API/api/parcours \
  -H "Origin: https://eduadvice.vercel.app" \
  -H "Access-Control-Request-Method: GET" | grep -i access-control

# 4. CORS : une origine inconnue est refusée → HTTP 403
curl -s -o /dev/null -w "%{http_code}\n" -X OPTIONS $API/api/parcours \
  -H "Origin: https://site-inconnu.example" \
  -H "Access-Control-Request-Method: GET"

# 5. HTTP est redirigé vers HTTPS → 301
curl -s -o /dev/null -w "%{http_code}\n" http://eduadvice-api.onrender.com/actuator/health

# 6. Les endpoints sensibles d'Actuator ne sont pas exposés → 404
curl -s -o /dev/null -w "%{http_code}\n" $API/actuator/env

# 7. Le frontend publié contient bien la dernière version
JS=$(curl -s https://eduadvice.vercel.app/ | grep -o '/assets/index-[^"]*\.js' | head -1)
echo "$JS"     # le nom (hash) change à chaque nouveau build
```

**Dans le navigateur** (https://eduadvice.vercel.app, F12) :

- onglet Console : aucune erreur ;
- navigation complète (programmes → UE → cours, page métiers) ;
- touche F5 sur `/programmes/1` : la page se recharge (pas de 404) ;
- onglet Network : la réponse de `/api/parcours` contient `access-control-allow-origin`.

---

## 8. Migrations de base de données — règles pour l'équipe

Le schéma est géré **uniquement** par Flyway (`backend/src/main/resources/db/migration`).
Hibernate est en mode `ddl-auto: validate` : il **vérifie** que les entités correspondent aux tables,
mais ne modifie jamais rien.

| Règle | Pourquoi |
|---|---|
| Toute modification d'entité JPA s'accompagne d'une migration `V<n>__description.sql` **dans la même PR** | Sinon le backend refuse de démarrer en production (`Schema validation: missing …`) |
| Ne **jamais** modifier une migration déjà appliquée (V1, V2…) | Flyway stocke une somme de contrôle ; une migration modifiée bloque le démarrage |
| Pas de « rollback » de migration : on corrige par une **nouvelle** migration | La base de production a déjà été modifiée |
| Pour les changements risqués : *expand / contract* (ajouter d'abord, supprimer plus tard) | Permet de revenir à la version précédente du code sans casser la base |

Attention : `validate` ne vérifie **pas** les contraintes `CHECK` ni `NOT NULL`. Ces points
doivent être relus en revue de code.

Migrations actuelles :

| Version | Contenu |
|---|---|
| V1 | Schéma initial (parcours, semestres, UE, matières, ressources, débouchés) |
| V2 | Alignement avec les entités : table `career_opportunity`, relation N-N `parcours_career_opportunities` (données existantes conservées), colonne `subjects.ects`, nouvelles catégories d'UE |

---

## 9. Choix techniques réalisés

### Image Docker du backend ([`backend/Dockerfile`](../../backend/Dockerfile))

| Choix | Bénéfice |
|---|---|
| Build **multi-étapes** (JDK pour compiler, JRE Alpine pour exécuter) | Pas d'outils de compilation dans l'image de production |
| Dépendances Maven téléchargées **avant** la copie du code | Cache Docker : les rebuilds ne retéléchargent pas tout |
| Utilisateur **non-root** `spring` | Limite les dégâts en cas de faille applicative |
| JVM réglée pour 512 Mo / 0,1 CPU (`MaxRAMPercentage=75`, `SerialGC`, `TieredStopAtLevel=1`, `ExitOnOutOfMemoryError`) | Tient dans les limites de Render Free ; redémarrage propre en cas de manque de mémoire |
| **CDS** (*Class Data Sharing*) généré pendant le build | Démarrage divisé par ~2 (116–145 s → ~60 s) |
| `ENTRYPOINT` en forme *exec* | Java reçoit le signal d'arrêt et s'arrête proprement |
| `HEALTHCHECK` sur `/actuator/health` | L'état du conteneur est surveillé |

Taille de l'image : **496 Mo**. Démarrage mesuré sur Render : **55,7 s**.

### Profil de production ([`application-prod.yaml`](../../backend/src/main/resources/application-prod.yaml))

- Pool de 5 connexions (instance de 512 Mo, offre Neon gratuite).
- Prise en compte des en-têtes `X-Forwarded-*` du proxy Render (HTTPS terminé en amont).
- Erreurs renvoyées **sans** trace technique ni message interne.
- `/actuator/health` ne montre que `{"status":"UP"}` ; seuls `health` et `info` sont exposés.

### CORS ([`CorsConfig.java`](../../backend/src/main/java/com/eduadvice/backend/config/CorsConfig.java))

- Origines lues depuis `CORS_ALLOWED_ORIGINS` ; par défaut, uniquement `http://localhost:5173` (**fermé par défaut**).
- `*` interdit : l'application refuse de démarrer.
- Seulement `/api/**`, méthodes `GET` et `POST`, sans cookies.
- Couvert par 4 tests automatisés (origine autorisée, origine inconnue, méthode interdite, `*` refusé).

### Réveil du backend côté frontend

- Délai d'attente des requêtes porté de 10 s à **90 s** (`frontend/src/services/api.ts`).
- Après 5 s de chargement, message « *Le serveur démarre, cela peut prendre jusqu'à une minute…* »
  (`frontend/src/components/states.tsx`).

---

## 10. Problèmes rencontrés et solutions

| Problème | Cause | Solution | Leçon |
|---|---|---|---|
| Build frontend en échec (`@tailwindcss/vite` introuvable) | Dépendance non déclarée dans `package.json` | Dépendance ajoutée | Un build doit réussir sur une machine **neuve** (`npm ci`) |
| Tests backend : *connection refused* | Pas de base locale | `compose.yaml` à la racine | L'environnement de dev doit être décrit dans le dépôt |
| *Password authentication failed* | Deux fichiers de config (`.properties` et `.yaml`) avec des valeurs différentes | Fichier unique `application.yaml` + variables d'environnement | Une seule source de vérité pour la configuration (*configuration drift*) |
| Ancienne configuration toujours active | Fichiers obsolètes dans `target/` | `./mvnw clean` | Toujours construire à partir d'un état propre |
| `relation "parcours" does not exist` | Le schéma dépendait de `ddl-auto=update` (Hibernate) | Flyway + migration V1 | Le schéma doit être versionné, pas « deviné » par l'ORM |
| `Schema validation: missing table [career_opportunity]` | Entités modifiées sans migration | Migration V2 (sans toucher à V1) | Entité modifiée ⇒ migration dans la même PR |
| Tests qui échouent une fois sur trois | Assertions dépendant de l'ordre d'un `HashSet` | Assertions indépendantes de l'ordre | Un test instable est un bug à corriger, pas à relancer |
| Image Docker de 602 Mo | Un fichier supprimé avec `rm` reste dans une couche précédente | Extraction du JAR dans l'étape de build | Chaque instruction Docker crée une couche définitive |
| Démarrage de 116–145 s à 0,1 CPU | Spring Boot sur un CPU très limité | CDS + réglages JVM → ~56 s | Mesurer avant d'optimiser |
| `gh pr create` : *Head repository can't be blank* | Dépôt transféré vers l'organisation, remote local obsolète | `git remote set-url` | — |
| Production différente de `main` après un merge | Déploiement manuel lancé depuis un dossier local pas à jour | Redéploiement depuis `main` | « Mergé » ≠ « déployé » : la Phase 5 confie le déploiement à la CI |

---

## 11. Limites connues

| Limite | Impact | Atténuation |
|---|---|---|
| Render Free : mise en veille après 15 min sans trafic | Première visite après inactivité : environ 1 min d'attente | Message d'attente côté frontend ; ouvrir le site quelques minutes **avant une démonstration** |
| Render Free : 512 Mo RAM, 0,1 CPU | Démarrage lent | Réglages JVM + CDS |
| Neon Free : historique de restauration limité à **6 h** | Une erreur découverte tard ne peut pas être annulée avec Neon seul | Sauvegarde quotidienne indépendante (Phase 4) |
| Vercel Hobby : usage non commercial uniquement | Conforme au cadre universitaire | Offre payante si le projet devient commercial |
| Pas d'environnement de pré-production | Les changements arrivent directement en production | Revue de code + CI (Phase 5) |

---

## 12. Reste à faire

- **Phase 4 — Sauvegardes :** workflow GitHub Actions quotidien (`pg_dump` chiffré, conservé 30 jours) et test de restauration documenté.
- **Phase 5 — CI/CD :** tests backend et frontend à chaque PR, protection de la branche `main` (relecture + CI verte obligatoires), déploiement automatique depuis `main` puis *smoke tests*.
- **Phase 6 — Sécurité :** en-tête HSTS sur le backend, désactivation de la page de découverte `/actuator`, revue des journaux.
- **Mesure :** temps de réveil réel du backend après mise en veille (à ajouter au § 11).
