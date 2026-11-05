# Contrat d'API REST — EduAdvice

## 1. Présentation

Le backend d'EduAdvice expose une API REST permettant au frontend de consulter les informations relatives aux formations universitaires.

Les échanges entre les deux applications reposent sur le protocole HTTP. Les données sont transmises au format JSON.

## 2. Conventions générales

- Préfixe des routes : `/api`
- Format des échanges : JSON (`application/json`)
- Nommage des ressources : noms au pluriel.
- Identification des ressources : identifiant dans l'URL.
- Les réponses de l'API utilisent les codes de statut HTTP appropriés.
- Les données échangées sont structurées à l'aide de DTO.

## 3. Endpoints

Les endpoints ci-dessous constituent une première proposition du contrat. Ils seront ajustés au cours de l'implémentation.

| Méthode | Endpoint | Description |
|---|---|---|
| GET | `/api/establishments` | Consulter les établissements |
| GET | `/api/diplomas` | Consulter les diplômes |
| GET | `/api/parcours` | Consulter les parcours |
| GET | `/api/parcours/{id}` | Consulter les détails d'un parcours |
| GET | `/api/parcours/{id}/semesters` | Consulter les semestres d'un parcours |
| GET | `/api/semesters/{id}/teaching-units` | Consulter les UE d'un semestre |
| GET | `/api/teaching-units/{id}/subjects` | Consulter les matières d'une UE |
| GET | `/api/subjects/{id}` | Consulter les détails d'une matière |
| GET | `/api/subjects/{id}/resources` | Consulter les ressources d'une matière |
| GET | `/api/parcours/{id}/careers` | Consulter les débouchés d'un parcours |

## 4. Format des réponses

Les réponses sont retournées au format JSON.

Exemple de réponse pour la consultation d'une matière :

```json
{
  "id": 1,
  "code": "INF101",
  "name": "Algorithmique",
  "description": "Introduction aux concepts fondamentaux de l'algorithmique."
}
```

Les propriétés exactes dépendront des données disponibles et des DTO définis lors de l'implémentation.

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
  "status": 404,
  "message": "Matière introuvable"
}
```

Le format définitif des réponses d'erreur sera harmonisé lors de l'implémentation de la gestion globale des exceptions.

## 7. Évolution du contrat

Le contrat d'API pourra évoluer en fonction des besoins fonctionnels et des contraintes techniques identifiées pendant le développement.

Toute modification des endpoints ou des formats de données devra être communiquée à l'équipe frontend afin de maintenir la compatibilité entre les deux applications.

## 8. Initialisation et gestion des données

Les données initiales de l'application seront intégrées directement dans la base de données PostgreSQL par l'équipe de développement.

Cette intégration pourra être réalisée à l'aide de scripts SQL ou de mécanismes d'importation adaptés.

Dans le périmètre du MVP, l'API est principalement destinée à la consultation des informations. La création, la modification et la suppression des données ne sont pas accessibles depuis l'interface utilisateur.

La gestion des données sera assurée au niveau de la base de données pendant cette première version.