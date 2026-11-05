# Architecture générale du système — EduAdvice

## 1. Présentation

EduAdvice est une application web destinée à centraliser et organiser les informations relatives aux formations universitaires. Elle permet aux utilisateurs de consulter les programmes, les enseignements, les ressources pédagogiques et les débouchés professionnels associés aux parcours.

L'application repose sur une architecture composée de trois éléments principaux : un frontend React, un backend Java avec Spring Boot et une base de données PostgreSQL.

## 2. Vue d'ensemble de l'architecture

EduAdvice adopte une architecture client-serveur avec un frontend et un backend séparés.

- **Frontend :** application React chargée de l'interface utilisateur et des interactions.
- **Backend :** application Java avec Spring Boot, responsable du traitement des requêtes, de la logique métier et de l'accès aux données.
- **Base de données :** PostgreSQL, utilisée pour stocker les informations relatives aux établissements, formations, parcours, semestres, UE, matières, ressources pédagogiques et débouchés professionnels.

Le backend suit une architecture monolithique : les fonctionnalités métier sont regroupées dans une seule application Spring Boot.

## 3. Schéma général

```text
+-----------------------------+
|           Utilisateur       |
|        Navigateur web       |
+--------------+--------------+
               |
               v
+-----------------------------+
|          Frontend           |
|            React            |
|                             |
| - Pages et composants       |
| - Navigation                |
| - Affichage des données     |
| - Appels à l'API             |
+--------------+--------------+
               |
               | Requêtes HTTP
               | / Réponses JSON
               v
+-----------------------------+
|           Backend           |
|       Java / Spring Boot     |
|                             |
| - API REST                  |
| - Logique métier            |
| - Traitement des requêtes   |
| - Accès aux données         |
+--------------+--------------+
               |
               | Requêtes SQL
               v
+-----------------------------+
|          Base de données    |
|          PostgreSQL         |
|                             |
| - Formations                |
| - Parcours                  |
| - Semestres et UE           |
| - Matières                  |
| - Ressources                |
| - Débouchés et compétences  |
+-----------------------------+
```

## 4. Communication entre les composants

La communication entre le frontend et le backend s'effectue à travers une API REST.

1. L'utilisateur effectue une action depuis l'interface React, par exemple consulter un parcours.
2. Le frontend envoie une requête HTTP au backend.
3. Le backend reçoit la requête et applique les traitements nécessaires.
4. Si des données sont nécessaires, le backend interroge PostgreSQL.
5. La base de données retourne les résultats au backend.
6. Le backend renvoie une réponse au frontend, généralement au format JSON.
7. React affiche les informations reçues à l'utilisateur.

Le frontend ne communique pas directement avec la base de données. Toutes les opérations d'accès aux données passent par le backend.

## 5. Responsabilités des composants

### 5.1 Frontend — React

Le frontend est responsable de :

- Présenter les informations de manière claire et structurée.
- Permettre la navigation entre les formations, parcours, semestres, UE et matières.
- Afficher les ressources pédagogiques et les débouchés professionnels.
- Communiquer avec le backend à travers l'API REST.
- Gérer les états et les interactions de l'interface.

### 5.2 Backend — Java / Spring Boot

Le backend est responsable de :

- Exposer les endpoints de l'API REST.
- Recevoir et traiter les requêtes du frontend.
- Appliquer les règles de gestion de l'application.
- Vérifier et structurer les données échangées.
- Communiquer avec la base de données.
- Retourner les résultats au frontend.

### 5.3 Base de données — PostgreSQL

La base de données assure le stockage persistant des informations de l'application, notamment :

- Les établissements et les diplômes.
- Les mentions et les parcours.
- Les niveaux académiques et les semestres.
- Les unités d'enseignement et les matières.
- Les ressources pédagogiques.
- Les débouchés professionnels et les compétences associées.

## 6. Organisation du déploiement

Pendant le développement, les composants peuvent être exécutés séparément sur l'environnement local :

- Le frontend React est lancé avec le serveur de développement Vite.
- Le backend Spring Boot est lancé comme une application Java.
- PostgreSQL fonctionne comme un service de base de données.

Cette séparation facilite le développement indépendant du frontend et du backend, tout en permettant leur intégration à travers l'API REST.

Les modalités de déploiement en environnement de production seront précisées ultérieurement dans le guide de déploiement.

## 7. Évolution de l'architecture

L'architecture retenue permet de faire évoluer séparément le frontend et le backend.

Elle pourra notamment accueillir de nouvelles fonctionnalités, de nouveaux établissements et de nouveaux parcours sans nécessiter une refonte complète de l'application.

Les choix techniques détaillés concernant l'organisation interne du backend, les échanges de données et les tests seront documentés dans les documents d'architecture correspondants.

## 8. Synthèse

EduAdvice repose sur une architecture client-serveur composée d'un frontend React, d'un backend monolithique Java / Spring Boot et d'une base de données PostgreSQL.

Les échanges entre le frontend et le backend sont assurés par une API REST, tandis que le backend centralise la logique métier et l'accès aux données.