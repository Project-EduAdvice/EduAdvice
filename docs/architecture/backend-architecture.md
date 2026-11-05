# Architecture du backend — EduAdvice

## 1. Présentation

Le backend d'EduAdvice est développé en Java avec Spring Boot. Il constitue le cœur applicatif du système : il expose les API REST, applique la logique métier et assure la communication avec la base de données PostgreSQL.

Le backend adopte une architecture monolithique organisée en plusieurs couches afin de séparer les responsabilités et de faciliter la maintenance et l'évolution du projet.

## 2. Architecture en couches

L'application repose sur les couches suivantes :

| Couche | Responsabilité |
|---|---|
| Controller | Réception des requêtes HTTP et exposition des endpoints REST. |
| Service | Implémentation de la logique métier et coordination des opérations. |
| Repository | Accès aux données et communication avec PostgreSQL. |
| Entity | Représentation des entités persistées en base de données. |
| DTO | Structuration des données échangées entre le frontend et le backend. |

Le traitement d'une requête suit généralement le chemin suivant :

`Client → Controller → Service → Repository → Database`

La réponse suit le chemin inverse.

## 3. Organisation du projet

Structure proposée pour le backend :

```text
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/eduadvice/
│   │   │       ├── EduAdviceApplication.java
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── repository/
│   │   │       ├── entity/
│   │   │       ├── dto/
│   │   │       ├── exception/
│   │   │       └── config/
│   │   └── resources/
│   │       └── application.yml
│   └── test/
│       └── java/
│           └── com/eduadvice/
├── pom.xml
└── README.md
```

Cette organisation pourra évoluer en fonction de la taille du projet.

## 4. Description des couches

### 4.1 Controller

Les contrôleurs exposent les endpoints REST et reçoivent les requêtes du frontend.

Leurs responsabilités sont :
- Définir les routes de l'API.
- Recevoir les paramètres et les données envoyées.
- Déclencher les traitements nécessaires via les services.
- Retourner les réponses HTTP appropriées.

Les contrôleurs ne doivent pas contenir la logique métier.

### 4.2 Service

Les services regroupent la logique métier de l'application.

Leurs responsabilités sont :
- Appliquer les règles de gestion.
- Coordonner les opérations entre les repositories.
- Vérifier les conditions nécessaires aux traitements.
- Préparer les données à retourner aux contrôleurs.

### 4.3 Repository

Les repositories assurent l'accès aux données persistées.

Le projet utilisera Spring Data JPA pour simplifier les opérations de lecture et d'écriture en base de données.

Hibernate sera utilisé comme implémentation JPA pour le mapping objet-relationnel.

### 4.4 Entity

Les entités représentent les objets persistés dans PostgreSQL.

Elles correspondent aux principales structures du modèle de données, notamment :
- Establishment
- Diploma
- Mention
- Parcours
- AcademicLevel
- Semester
- TeachingUnit
- Subject
- LearningResource
- CareerOpportunity
- Skill

Les relations entre les entités devront respecter le modèle relationnel défini pour le projet.

### 4.5 DTO

Les Data Transfer Objects (DTO) définissent les données échangées entre le frontend et le backend.

Ils permettent de :
- Contrôler les informations exposées par l'API.
- Éviter d'exposer directement les entités JPA.
- Adapter les réponses aux besoins du frontend.
- Séparer le modèle de persistance du format des échanges.

### 4.6 Exception et validation

La gestion des erreurs sera centralisée afin de fournir des réponses cohérentes à l'API.

La validation des données entrantes sera réalisée notamment avec Jakarta Bean Validation.

Les erreurs devront être associées aux codes HTTP appropriés et accompagnées de messages compréhensibles.

### 4.7 Config

Cette couche regroupe les configurations techniques nécessaires au fonctionnement du backend, notamment :
- Configuration de l'application.
- Configuration de la connexion à PostgreSQL.
- Configuration CORS pour autoriser les échanges avec le frontend.
- Autres configurations techniques nécessaires.

## 5. Configuration et variables d'environnement

Les informations sensibles et les paramètres dépendant de l'environnement ne doivent pas être écrits directement dans le code source.

Les variables suivantes sont notamment prévues :

| Variable | Description |
|---|---|
| `DB_URL` | URL de connexion PostgreSQL |
| `DB_USERNAME` | Nom d'utilisateur de la base |
| `DB_PASSWORD` | Mot de passe de la base |

Un fichier `.env.example` ou un mécanisme équivalent pourra documenter les variables nécessaires sans contenir de véritables identifiants.

## 6. Tests du backend

Les tests permettront de vérifier le bon fonctionnement des différentes couches.

- **Tests unitaires :** vérification de la logique métier des services.
- **Tests d'intégration :** vérification des interactions entre les composants et la base de données.
- **Tests des contrôleurs :** vérification des endpoints et des réponses HTTP.

Les outils de test seront précisés dans la stratégie de tests du projet.

## 7. Sécurité

Dans le périmètre actuel du MVP, aucune fonctionnalité d'authentification ou de gestion de comptes utilisateurs n'est prévue.

Les bonnes pratiques de sécurité restent néanmoins applicables :
- Validation des données entrantes.
- Gestion sécurisée des variables d'environnement.
- Protection contre les injections SQL grâce aux mécanismes de persistance.
- Configuration contrôlée des origines autorisées pour les requêtes CORS.
- Gestion appropriée des erreurs sans exposer d'informations internes.

## 8. Évolutivité

L'architecture en couches permet de faire évoluer les fonctionnalités sans mélanger les responsabilités.

De nouvelles fonctionnalités pourront être ajoutées en étendant les services, les contrôleurs, les repositories et les DTO concernés.

Cette organisation facilite également les tests, la maintenance et le travail collaboratif entre les membres de l'équipe.