# EduAdvice — Backend

Le backend d'EduAdvice est une API REST développée avec Java et Spring Boot. Il repose sur une architecture monolithique et communique avec une base de données PostgreSQL.

## 1. Technologies utilisées

| Technologie | Version / Détails |
|---|---|
| Java JDK | 21 |
| Spring Boot | 4.1.2 |
| Maven | Version définie dans le Maven Wrapper |
| Base de données | PostgreSQL |
| ORM | Hibernate / Spring Data JPA |
| API | Spring Web MVC |
| Configuration | YAML (`application.yml`) |
| Tests | JUnit 5 / Spring Boot Test |

**Gestion des versions :** Spring Boot gère les versions compatibles de ses dépendances grâce à son parent POM. Il est donc préférable de ne pas préciser individuellement les versions des dépendances, sauf besoin particulier.

## 2. Prérequis

Avant de lancer le backend, assurez-vous de disposer des éléments suivants :

- Java JDK 21 installé.
- PostgreSQL installé.
- Git installé.
- Un terminal ou un environnement de développement (IntelliJ IDEA, Eclipse ou VSCodium).

**Maven :** Il n'est pas nécessaire d'installer Maven globalement. Le projet utilise le Maven Wrapper (`mvnw`), qui télécharge et utilise automatiquement la version de Maven configurée pour le projet.

Pour vérifier Java :

```bash
java -version
javac -version
```

Pour vérifier PostgreSQL :

```bash
psql --version
```

## 3. Structure du projet

```text
backend/
├── .mvn/
│   └── wrapper/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/eduadvice/backend/
│   │   │       ├── BackendApplication.java
│   │   │       └── controller/
│   │   │           └── HealthController.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
│       └── java/
│           └── com/eduadvice/backend/
│               ├── BackendApplicationTests.java
│               └── controller/
│                   └── HealthControllerTests.java
├── .gitignore
├── mvnw
├── mvnw.cmd
└── pom.xml
```

L'architecture backend évoluera progressivement vers une architecture en couches :

- **Controller :** reçoit et traite les requêtes HTTP, puis retourne les réponses.
- **Service :** contient la logique métier de l'application.
- **Repository :** gère l'accès aux données et les interactions avec la base de données.
- **Entity :** représente les entités de la base de données.
- **DTO :** définit les données échangées entre le client et l'API.
- **Exception :** centralise la gestion des erreurs.
- **Config :** regroupe les configurations de l'application.

## 4. Dépendances principales

| Dépendance | Rôle |
|---|---|
| Spring Web MVC | Développer les endpoints REST et gérer les requêtes HTTP. |
| Spring Data JPA | Simplifier l'accès à la base de données à travers des repositories. |
| PostgreSQL Driver | Permettre la communication entre Java et PostgreSQL. |
| Spring Boot Validation | Valider les données reçues par l'API. |
| Spring Boot DevTools | Améliorer l'expérience de développement, notamment grâce au redémarrage automatique. |

Des dépendances supplémentaires sont présentes pour les tests de l'application et de la couche web.

## 5. Configuration de l'application

Le fichier principal de configuration est :

`src/main/resources/application.yml`

Le nom de l'application est `backend` et le port HTTP utilisé par défaut est `8080`.

### Configuration de la base de données

La connexion à PostgreSQL est configurée à l'aide de variables d'environnement :

| Variable | Valeur par défaut / Rôle |
|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/eduadvice` |
| `DB_USERNAME` | `eduadvice_app` |
| `DB_PASSWORD` | Mot de passe du compte PostgreSQL (obligatoire). |

Les valeurs par défaut de l'URL et du nom d'utilisateur peuvent être remplacées selon la configuration locale de chaque développeur.

Les variables doivent être définies dans le terminal avant de démarrer l'application ou d'exécuter les tests.

**Linux / macOS :**

```bash
export DB_URL="jdbc:postgresql://localhost:5432/eduadvice"
export DB_USERNAME="eduadvice_app"
export DB_PASSWORD="VOTRE_MOT_DE_PASSE"
```

**Windows PowerShell :**

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/eduadvice"
$env:DB_USERNAME="eduadvice_app"
$env:DB_PASSWORD="VOTRE_MOT_DE_PASSE"
```

Ces variables sont propres à chaque environnement local. Elles restent disponibles dans le terminal courant et ses processus enfants. Si vous ouvrez un nouveau terminal, vous devrez les définir à nouveau.

Ne partagez jamais vos mots de passe et ne les ajoutez jamais au dépôt Git.

**Important :** chaque développeur doit disposer d'une base PostgreSQL locale nommée `eduadvice` et d'un utilisateur disposant des droits nécessaires. La procédure de création est détaillée dans la section suivante.

La propriété Hibernate `ddl-auto: none` désactive la génération et la modification automatiques du schéma. Hibernate ne créera donc pas les tables au démarrage de l'application. Le schéma de la base de données sera géré séparément par l'équipe.

## 6. Installation et configuration de PostgreSQL

Chaque membre de l'équipe doit disposer de sa propre installation locale de PostgreSQL. La base de données n'est pas partagée automatiquement entre les développeurs.

Cette section explique comment installer ou démarrer PostgreSQL, créer l'utilisateur et la base de données nécessaires au backend EduAdvice.

### 6.1. Installer et démarrer PostgreSQL

Si PostgreSQL n'est pas encore installé, consultez la page officielle de téléchargement et choisissez les instructions correspondant à votre système d'exploitation :

[https://www.postgresql.org/download/](https://www.postgresql.org/download/)

Vérifiez que PostgreSQL est installé :

```bash
psql --version
```

Assurez-vous que le serveur PostgreSQL est démarré avant de continuer.

**Linux avec systemd (exemple) :**

```bash
sudo systemctl start postgresql
```

Cette commande démarre PostgreSQL pour la session actuelle. Il n'est pas nécessaire de l'activer automatiquement au démarrage de l'ordinateur.

**Windows :**

PostgreSQL peut être démarré depuis les Services Windows. Recherchez le service PostgreSQL et vérifiez qu'il est en cours d'exécution.

**macOS :**

Si PostgreSQL a été installé avec Homebrew, démarrez le service avec la commande correspondant à la formule installée. Par exemple :

```bash
brew services start postgresql
```

### 6.2. Se connecter au compte administrateur PostgreSQL

Pour créer un utilisateur et une base de données, il faut d'abord se connecter avec un compte PostgreSQL disposant des privilèges nécessaires.

**Linux :**

Sur une installation utilisant le compte système `postgres` :

```bash
sudo -u postgres psql
```

**Windows :**

```powershell
psql -U postgres -h localhost -W
```

**macOS :**

Selon votre installation, connectez-vous avec le compte administrateur PostgreSQL, par exemple :

```bash
psql -U postgres -d postgres
```

Si la connexion réussit, vous arrivez dans le terminal PostgreSQL. L'invite ressemble généralement à :

```text
postgres=#
```

### 6.3. Créer l'utilisateur de l'application

Dans le terminal PostgreSQL, exécutez :

```sql
CREATE ROLE eduadvice_app
WITH LOGIN
PASSWORD 'CHANGEZ_CE_MOT_DE_PASSE';
```

Remplacez `CHANGEZ_CE_MOT_DE_PASSE` par un mot de passe personnel et sécurisé.

Cet utilisateur sera utilisé par l'application Spring Boot pour se connecter à PostgreSQL.

**Important :** chaque développeur choisit son propre mot de passe. Ne partagez pas votre mot de passe et ne l'ajoutez jamais au dépôt Git.

Si le rôle `eduadvice_app` existe déjà dans votre installation, ne relancez pas cette commande : utilisez le rôle existant et vérifiez son mot de passe.

### 6.4. Créer la base de données

Toujours dans le terminal PostgreSQL, exécutez :

```sql
CREATE DATABASE eduadvice
OWNER eduadvice_app;
```

La base de données `eduadvice` appartient maintenant à l'utilisateur `eduadvice_app`.

En tant que propriétaire, cet utilisateur dispose des droits nécessaires pour travailler dans cette base de données.

Si la base existe déjà, ne relancez pas cette commande.

Pour quitter le terminal PostgreSQL :

```sql
\q
```

### 6.5. Vérifier la connexion à la base de données

Depuis votre terminal habituel, exécutez :

```bash
psql -h localhost -p 5432 -U eduadvice_app -d eduadvice -W
```

Saisissez le mot de passe choisi précédemment.

Si la connexion réussit, vous êtes connecté à la base de données `eduadvice`.

Pour afficher les bases de données :

```sql
\l
```

Pour afficher les tables :

```sql
\dt
```

À ce stade, aucune table applicative n'est encore créée. C'est normal : le schéma de la base de données sera intégré séparément au projet.

Quittez avec :

```sql
\q
```

Si la connexion échoue avec une erreur d'authentification, vérifiez que la configuration locale de PostgreSQL autorise l'authentification par mot de passe pour les connexions locales TCP. La méthode dépend du système et de la configuration de PostgreSQL.

### 6.6. Configurer les variables d'environnement

Spring Boot utilise les variables d'environnement suivantes pour établir la connexion à PostgreSQL :

| Variable | Valeur |
|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/eduadvice` |
| `DB_USERNAME` | `eduadvice_app` |
| `DB_PASSWORD` | Le mot de passe choisi lors de la création de l'utilisateur |

**Linux / macOS :**

```bash
export DB_URL="jdbc:postgresql://localhost:5432/eduadvice"
export DB_USERNAME="eduadvice_app"
export DB_PASSWORD="VOTRE_MOT_DE_PASSE"
```

**Windows PowerShell :**

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/eduadvice"
$env:DB_USERNAME="eduadvice_app"
$env:DB_PASSWORD="VOTRE_MOT_DE_PASSE"
```

Ces commandes doivent être exécutées dans le terminal qui sera utilisé pour démarrer le backend.

### 6.7. Vérifier la configuration Spring Boot

Le fichier `src/main/resources/application.yml` utilise ces variables pour configurer la connexion :

```yaml
spring:
  application:
    name: backend

  datasource:
    url: ${DB_URL:jdbc:postgresql://localhost:5432/eduadvice}
    username: ${DB_USERNAME:eduadvice_app}
    password: ${DB_PASSWORD}

  jpa:
    hibernate:
      ddl-auto: none
    open-in-view: false
    show-sql: false

server:
  port: 8080
```

**Important :** `ddl-auto: none` empêche Hibernate de créer, modifier ou supprimer automatiquement les tables. La structure de la base de données sera gérée séparément par l'équipe.

## 7. Installation et démarrage du backend

### Étape 1 — Cloner le dépôt

Depuis le dossier dans lequel vous souhaitez installer le projet :

```bash
git clone https://github.com/meloustabalan12/EduAdvice.git
cd EduAdvice/backend
```

### Étape 2 — Vérifier les prérequis

Assurez-vous que Java 21 et PostgreSQL sont installés et que le serveur PostgreSQL est démarré.

```bash
java -version
psql --version
```

### Étape 3 — Configurer les variables d'environnement

Définissez les variables décrites dans la section 6.6, en utilisant les informations de votre installation PostgreSQL.

### Étape 4 — Compiler et exécuter les tests

Sous Linux et macOS :

```bash
chmod +x mvnw
./mvnw clean verify
```

Sous Windows :

```powershell
.\mvnw.cmd clean verify
```

Cette commande nettoie les anciens fichiers de compilation, compile le projet, exécute les tests et vérifie le résultat du build.

PostgreSQL doit être accessible et les variables d'environnement doivent être définies pour les tests qui chargent le contexte complet de Spring.

### Étape 5 — Démarrer le backend

Sous Linux et macOS :

```bash
./mvnw spring-boot:run
```

Sous Windows :

```powershell
.\mvnw.cmd spring-boot:run
```

L'API sera accessible à l'adresse suivante :

`http://localhost:8080`

## 8. Vérification de l'API — Health Check

Un endpoint permet de vérifier que le backend répond correctement.

- **Méthode HTTP :** `GET`
- **Endpoint :** `/api/health`
- **URL complète :** `http://localhost:8080/api/health`

Réponse attendue :

```text
EduAdvice API is running!
```

Cet endpoint vérifie que l'API est accessible. Il ne vérifie pas à lui seul le bon fonctionnement des opérations sur la base de données.

## 9. Exécution des tests

Pour exécuter l'ensemble des vérifications :

**Linux / macOS :**

```bash
./mvnw clean verify
```

**Windows :**

```powershell
.\mvnw.cmd clean verify
```

Les tests actuels vérifient notamment :

- Le chargement correct du contexte Spring.
- Le fonctionnement de l'endpoint `/api/health`.
- Le contenu de la réponse HTTP retournée.

Les tests qui chargent le contexte complet de Spring nécessitent une base PostgreSQL accessible et les variables d'environnement correctement configurées.

## 10. Conventions de développement

Afin de faciliter le travail collaboratif, les règles suivantes doivent être respectées :

- Ne pas effectuer de commits directement sur la branche `main`.
- Créer une branche dédiée pour chaque fonctionnalité ou tâche.
- Respecter le workflow Git défini par l'équipe.
- Séparer les responsabilités entre Controllers, Services, Repositories, Entities et DTOs.
- Ne jamais versionner les mots de passe, les fichiers d'environnement locaux ou les fichiers générés.
- Exécuter les tests avant de créer une Pull Request.

### Exemple de workflow Git

Récupérer les dernières modifications de `main` :

```bash
git switch main
git pull origin main
```

Créer une nouvelle branche :

```bash
git switch -c feature/nom-de-la-fonctionnalite
```

Après avoir terminé le travail :

```bash
git add .
git commit -m "feat: description de la modification"
git push -u origin feature/nom-de-la-fonctionnalite
```

Enfin, créer une Pull Request vers la branche `main` afin que les modifications puissent être examinées avant leur intégration.

---

**EduAdvice — Backend Team**