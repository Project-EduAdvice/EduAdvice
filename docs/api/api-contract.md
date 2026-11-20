# Contrat d'API REST — EduAdvice

## 1. Présentation

Le backend d'EduAdvice expose une API REST permettant au frontend de consulter les informations relatives aux formations universitaires, aux parcours, aux semestres, aux unités d'enseignement (UE), aux matières, aux ressources pédagogiques et aux débouchés professionnels.

Les échanges entre le frontend et le backend reposent sur le protocole HTTP. Les données sont transmises au format JSON (`application/json`).

## 2. Conventions générales

- Préfixe des routes : `/api`
- Format des échanges : JSON (`application/json`)
- Nommage des ressources : noms au pluriel.
- Identification des ressources : identifiant dans l'URL.
- Les réponses de l'API utilisent les codes de statut HTTP appropriés.
- Les données échangées sont structurées à l'aide de DTO.

## 3. Endpoints

### 3.1. Accès aux ressources principales

Chaque entité dispose de deux endpoints permettant de consulter l'ensemble de ses éléments ou un élément précis.

|Méthode|Endpoint|Description|
|---|---|---|
|GET|`/api/parcours`|Récupérer tous les parcours|
|GET|`/api/parcours/{id}`|Récupérer un parcours par son identifiant|
|GET|`/api/semesters`|Récupérer tous les semestres|
|GET|`/api/semesters/{id}`|Récupérer un semestre par son identifiant|
|GET|`/api/teaching-units`|Récupérer toutes les unités d'enseignement|
|GET|`/api/teaching-units/{id}`|Récupérer une unité d'enseignement par son identifiant|
|GET|`/api/subjects`|Récupérer toutes les matières|
|GET|`/api/subjects/{id}`|Récupérer une matière par son identifiant|
|GET|`/api/learning-resources`|Récupérer toutes les ressources pédagogiques|
|GET|`/api/learning-resources/{id}`|Récupérer une ressource pédagogique par son identifiant|
|GET|`/api/career-opportunities`|Récupérer tous les débouchés professionnels|
|GET|`/api/career-opportunities/{id}`|Récupérer un débouché professionnel par son identifiant|

### 3.2. Navigation entre les entités

L'API permet également de naviguer entre les entités en utilisant des endpoints relationnels.

Ces endpoints permettent de remonter vers une entité parente ou de descendre vers les entités qui lui sont associées.

| Méthode | Endpoint                                  | Description                                         |
| ------- | ----------------------------------------- | --------------------------------------------------- |
| GET     | `/api/parcours/{id}/semesters`            | Récupérer les semestres d'un parcours               |
| GET     | `/api/parcours/{id}/career-opportunities` | Récupérer les débouchés associés à un parcours      |
| GET     | `/api/career-opportunities/{id}/parcours` | Récupérer les parcours associés à un débouché       |
| GET     | `/api/semesters/{id}/teaching-units`      | Récupérer les UE d'un semestre                      |
| GET     | `/api/semesters/{id}/parcours`            | Récupérer le parcours auquel appartient un semestre |
| GET     | `/api/teaching-units/{id}/semester`       | Récupérer le semestre auquel appartient une UE      |
| GET     | `/api/teaching-units/{id}/subjects`       | Récupérer les matières d'une UE                     |
| GET     | `/api/subjects/{id}/teaching-unit`        | Récupérer l'UE à laquelle appartient une matière    |
| GET     | `/api/subjects/{id}/learning-resources`   | Récupérer les ressources pédagogiques d'une matière |
| GET     | `/api/learning-resources/{id}/subject`    | Récupérer la matière associée à une ressource       |

## 4. Format des réponses

Les réponses sont retournées au format JSON.

Exemple de réponse pour la consultation d'une matière :

```json
{
	"id":1,
	"teachingUnitId":1,
	"name":"Apprentissage statistique",
	"description":"Étude des méthodes statistiques permettant aux systèmes informatiques d'apprendre à partir de données.",
	"content":"Apprentissage supervisé, régression, classification, estimation, généralisation et évaluation des modèles.",
	"ects":3.0
}
```
Exemple de réponse pour la consultation des matières d'une unité d'enseignement :

```json
  {
	  "id":1,
	  "teachingUnitId":1,
	  "name":"Apprentissage statistique",
	  "description":"Étude des méthodes statistiques permettant aux systèmes informatiques d'apprendre à partir de données.",
	  "content":"Apprentissage supervisé, régression, classification, estimation, généralisation et évaluation des modèles.",
	  "ects":3.0
	},

	{
		"id":2,
		"teachingUnitId":1,
		"name":"Deep Learning",
		"description":"Étude des réseaux de neurones profonds utilisés pour apprendre des représentations complexes à partir de données.",
		"content":"Réseaux de neurones, rétropropagation, fonctions d'activation, optimisation, réseaux convolutifs et architectures profondes.",
		"ects":3.0
	}
```
Les propriétés exactes des réponses dépendent des DTO définis dans le backend et des informations disponibles dans la base de données.

## 5. Codes de statut HTTP

| Code | Signification | Utilisation |
|---|---|---|
| 200 | OK | Requête exécutée avec succès |
| 201 | Created | Ressource créée |
| 204 | No Content | Requête exécutée sans contenu à retourner |
| 400 | Bad Request | Requête invalide |
| 404 | Not Found | Ressource introuvable |
| 500 | Internal Server Error | Erreur interne du serveur |

## 6. Gestion des erreurs

Les erreurs sont traitées par le backend et retournées sous forme de réponses JSON.

Exemple :

```json
{
	"status":404,
	"error":"Not Found",
	"message":"Subject not found with id: 99",
	"path":"/api/subjects/99",
	"timestamp":"2026-11-20T01:36:25.043635031"
}
```

Le format définitif des réponses d'erreur sera harmonisé lors de l'implémentation de la gestion globale des exceptions.

## 7. Évolution du contrat

Le contrat d'API pourra évoluer en fonction des besoins fonctionnels et des contraintes techniques identifiées pendant le développement.

Toute modification des endpoints ou des formats de données devra être communiquée à l'équipe frontend afin de maintenir la compatibilité entre les deux applications.

## 8. Initialisation et gestion des données

Les données initiales de l'application sont intégrées directement dans la base de données PostgreSQL par l'équipe de développement.

L'initialisation est réalisée à l'aide de mécanismes de peuplement des données (_Data Seeding_), permettant de créer les parcours, les semestres, les unités d'enseignement, les matières, les ressources pédagogiques et les débouchés professionnels nécessaires au fonctionnement de l'application.

Dans le périmètre du MVP, l'API est principalement destinée à la consultation des informations.

La création, la modification et la suppression des données ne sont pas accessibles depuis l'interface utilisateur. La gestion des données initiales reste sous la responsabilité de l'équipe de développement.
