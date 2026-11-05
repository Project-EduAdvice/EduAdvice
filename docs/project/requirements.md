# Exigences et critères d'acceptation

## 1. Objet du document

Ce document présente les besoins fonctionnels et les exigences du projet EduAdvice, ainsi que les critères permettant de vérifier le bon fonctionnement des fonctionnalités prévues dans le MVP.

Il complète le rapport initial du projet en formalisant le travail réalisé par l'équipe au cours de la phase de cadrage.

## 2. Présentation du périmètre fonctionnel

EduAdvice est une plateforme web destinée à centraliser les informations relatives aux formations universitaires et à faciliter leur consultation par les futurs étudiants et les étudiants inscrits.

Le MVP se concentre sur les fonctionnalités suivantes :

- Présentation de la formation.
- Consultation du programme par semestre.
- Consultation des fiches matières.
- Accès aux ressources pédagogiques.
- Consultation des débouchés professionnels.
- Navigation entre les différentes rubriques.

## 3. Besoins fonctionnels

### BF-01 — Présentation de la formation

L'utilisateur doit pouvoir consulter une présentation de la formation, comprenant les informations disponibles sur ses objectifs et son organisation.

### BF-02 — Programme par semestre

L'utilisateur doit pouvoir consulter le programme de la formation, organisé par semestre, et identifier les matières associées à chaque semestre.

### BF-03 — Fiches des matières

L'utilisateur doit pouvoir consulter une fiche dédiée à chaque matière afin d'accéder aux informations disponibles sur ses objectifs et son contenu.

### BF-04 — Ressources pédagogiques

L'utilisateur doit pouvoir accéder à des ressources pédagogiques gratuites associées aux matières, au moyen de liens vers des contenus externes.

### BF-05 — Débouchés professionnels

L'utilisateur doit pouvoir consulter les métiers, les secteurs professionnels et les compétences associés à la formation lorsque ces informations sont disponibles.

### BF-06 — Navigation

L'utilisateur doit pouvoir naviguer facilement entre les différentes rubriques de la plateforme et revenir aux pages précédemment consultées.

## 4. Critères d'acceptation

Les critères suivants définissent les conditions de vérification des fonctionnalités du MVP.

### CA-01 — Présentation de la formation

- [ ] La page de présentation est accessible depuis la navigation.
- [ ] La description, # Exigences et critères d'acceptation

## 1. Objet du document

Ce document présente les besoins fonctionnels et les exigences du projet EduAdvice, ainsi que les critères permettant de vérifier le bon fonctionnement des fonctionnalités prévues dans le MVP.

Il complète le rapport initial du projet en formalisant le travail réalisé par l'équipe au cours de la phase de cadrage.

## 2. Présentation du périmètre fonctionnel

EduAdvice est une plateforme web destinée à centraliser les informations relatives aux formations universitaires et à faciliter leur consultation par les futurs étudiants et les étudiants inscrits.

Le MVP se concentre sur les fonctionnalités suivantes :

- Présentation de la formation.
- Consultation du programme par semestre.
- Consultation des fiches matières.
- Accès aux ressources pédagogiques.
- Consultation des débouchés professionnels.
- Navigation entre les différentes rubriques.

## 3. Besoins fonctionnels

### BF-01 — Présentation de la formation

L'utilisateur doit pouvoir consulter une présentation de la formation, comprenant les informations disponibles sur ses objectifs et son organisation.

### BF-02 — Programme par semestre

L'utilisateur doit pouvoir consulter le programme de la formation, organisé par semestre, et identifier les matières associées à chaque semestre.

### BF-03 — Fiches des matières

L'utilisateur doit pouvoir consulter une fiche dédiée à chaque matière afin d'accéder aux informations disponibles sur ses objectifs et son contenu.

### BF-04 — Ressources pédagogiques

L'utilisateur doit pouvoir accéder à des ressources pédagogiques gratuites associées aux matières, au moyen de liens vers des contenus externes.

### BF-05 — Débouchés professionnels

L'utilisateur doit pouvoir consulter les métiers, les secteurs professionnels et les compétences associés à la formation lorsque ces informations sont disponibles.

### BF-06 — Navigation

L'utilisateur doit pouvoir naviguer facilement entre les différentes rubriques de la plateforme et revenir aux pages précédemment consultées.

## 4. Critères d'acceptation

Les critères suivants définissent les conditions de vérification des fonctionnalités du MVP.

### CA-01 — Présentation de la formation

- [ ] La page de présentation est accessible depuis la navigation.
- [ ] La description, les objectifs et l'organisation de la formation sont affichés à partir des données disponibles.
- [ ] Aucune section obligatoire n'est laissée vide si les informations correspondantes ont été fournies.

### CA-02 — Programme par semestre

- [ ] La liste des semestres disponibles est affichée.
- [ ] Chaque semestre permet d'identifier les matières qui lui sont associées.
- [ ] La sélection d'un semestre affiche les matières de ce semestre.
- [ ] Un semestre sans matière renseignée affiche un état vide compréhensible, sans erreur.

### CA-03 — Fiches des matières

- [ ] L'utilisateur peut accéder à une fiche depuis le programme.
- [ ] La fiche affiche le nom de la matière et les informations disponibles sur ses objectifs et son contenu.
- [ ] Une information absente n'est pas remplacée par un contenu inventé.
- [ ] Le retour au programme est possible.

### CA-04 — Ressources pédagogiques

- [ ] Chaque ressource affichée possède un titre et un lien.
- [ ] Le clic sur une ressource ouvre la destination attendue.
- [ ] Les ressources sélectionnées sont gratuites, au moins pour le contenu référencé.
- [ ] Une ressource dont le lien est invalide est corrigée ou retirée avant validation.

### CA-05 — Débouchés professionnels

- [ ] La rubrique présente les métiers et secteurs documentés.
- [ ] Les compétences associées sont affichées lorsqu'elles sont disponibles.
- [ ] Les informations sont rattachées à la formation ou à une source pertinente.
- [ ] L'absence de données ne provoque ni erreur ni affichage trompeur.

### CA-06 — Navigation

- [ ] Les rubriques du MVP sont accessibles par les moyens de navigation prévus.
- [ ] Chaque lien interne conduit à la bonne page ou section.
- [ ] Le retour vers les rubriques précédentes fonctionne.
- [ ] Aucun lien interne ne mène vers une page inexistante.

**Remarque :** Le dernier critère visible dans la capture est tronqué. Il sera ajouté après vérification de sa formulation complète.

## 5. Organisation des informations pédagogiques

Les informations pédagogiques sont organisées selon la structure retenue par l'équipe :

**Semestre → Unité d'Enseignement (UE) → Matières / enseignements.**

Cette organisation doit rester cohérente avec les maquettes pédagogiques officielles de la formation.

## 6. Fonctionnalités exclues du MVP

Les fonctionnalités suivantes ne sont pas prévues dans la première version :

- Création de comptes et authentification.
- Suivi personnalisé de la progression des étudiants.
- Témoignages d'anciens étudiants.
- Espace d'administration destiné aux enseignants.
- Quiz et évaluations.
- Recommandations personnalisées de carrières.

Ces fonctionnalités pourront être étudiées dans le cadre d'évolutions ultérieures.

## 7. Exigences non fonctionnelles

- **Ergonomie :** l'interface doit être compréhensible et facile à utiliser.
- **Fiabilité :** les informations affichées doivent être basées sur des sources identifiables et pertinentes.
- **Maintenabilité :** le code doit être structuré pour faciliter les corrections et les évolutions.
- **Compatibilité :** la plateforme doit être accessible depuis les navigateurs web courants.
- **Performance :** la consultation des informations doit rester fluide dans des conditions normales d'utilisation.
- **Évolutivité :** la conception doit permettre d'intégrer ultérieurement d'autres formations et fonctionnalités.

## 8. Évolution des exigences

Ce document pourra être enrichi au cours des prochains sprints en fonction des décisions collectives de l'équipe et de l'avancement du développement.

Toute évolution du périmètre fonctionnel devra être discutée et documentée.les objectifs et l'organisation de la formation sont affichés à partir des données disponibles.
- [ ] Aucune section obligatoire n'est laissée vide si les informations correspondantes ont été fournies.

### CA-02 — Programme par semestre

- [ ] La liste des semestres disponibles est affichée.
- [ ] Chaque semestre permet d'identifier les matières qui lui sont associées.
- [ ] La sélection d'un semestre affiche les matières de ce semestre.
- [ ] Un semestre sans matière renseignée affiche un état vide compréhensible, sans erreur.

### CA-03 — Fiches des matières

- [ ] L'utilisateur peut accéder à une fiche depuis le programme.
- [ ] La fiche affiche le nom de la matière et les informations disponibles sur ses objectifs et son contenu.
- [ ] Une information absente n'est pas remplacée par un contenu inventé.
- [ ] Le retour au programme est possible.

### CA-04 — Ressources pédagogiques

- [ ] Chaque ressource affichée possède un titre et un lien.
- [ ] Le clic sur une ressource ouvre la destination attendue.
- [ ] Les ressources sélectionnées sont gratuites, au moins pour le contenu référencé.
- [ ] Une ressource dont le lien est invalide est corrigée ou retirée avant validation.

### CA-05 — Débouchés professionnels

- [ ] La rubrique présente les métiers et secteurs documentés.
- [ ] Les compétences associées sont affichées lorsqu'elles sont disponibles.
- [ ] Les informations sont rattachées à la formation ou à une source pertinente.
- [ ] L'absence de données ne provoque ni erreur ni affichage trompeur.

### CA-06 — Navigation

- [ ] Les rubriques du MVP sont accessibles par les moyens de navigation prévus.
- [ ] Chaque lien interne conduit à la bonne page ou section.
- [ ] Le retour vers les rubriques précédentes fonctionne.
- [ ] Aucun lien interne ne mène vers une page inexistante.

**Remarque :** Le dernier critère visible dans la capture est tronqué. Il sera ajouté après vérification de sa formulation complète.

## 5. Organisation des informations pédagogiques

Les informations pédagogiques sont organisées selon la structure retenue par l'équipe :

**Semestre → Unité d'Enseignement (UE) → Matières / enseignements.**

Cette organisation doit rester cohérente avec les maquettes pédagogiques officielles de la formation.

## 6. Fonctionnalités exclues du MVP

Les fonctionnalités suivantes ne sont pas prévues dans la première version :

- Création de comptes et authentification.
- Suivi personnalisé de la progression des étudiants.
- Témoignages d'anciens étudiants.
- Espace d'administration destiné aux enseignants.
- Quiz et évaluations.
- Recommandations personnalisées de carrières.

Ces fonctionnalités pourront être étudiées dans le cadre d'évolutions ultérieures.

## 7. Exigences non fonctionnelles

- **Ergonomie :** l'interface doit être compréhensible et facile à utiliser.
- **Fiabilité :** les informations affichées doivent être basées sur des sources identifiables et pertinentes.
- **Maintenabilité :** le code doit être structuré pour faciliter les corrections et les évolutions.
- **Compatibilité :** la plateforme doit être accessible depuis les navigateurs web courants.
- **Performance :** la consultation des informations doit rester fluide dans des conditions normales d'utilisation.
- **Évolutivité :** la conception doit permettre d'intégrer ultérieurement d'autres formations et fonctionnalités.

## 8. Évolution des exigences

Ce document pourra être enrichi au cours des prochains sprints en fonction des décisions collectives de l'équipe et de l'avancement du développement.

Toute évolution du périmètre fonctionnel devra être discutée et documentée.