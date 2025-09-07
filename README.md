Gestion des Réunions – SKYTEK

📖 Description du projet

Ce projet est une application web développée dans le cadre de mon stage chez SKYTEK.
Il permet de gérer les utilisateurs, les salles de réunion et les réunions à travers une interface intuitive avec un calendrier interactif.

L’application est découpée en deux parties :

Backend : Développé en Spring Boot (Java) avec une architecture en couches.

Frontend : Développé en Angular pour une interface moderne et responsive.

🚀 Prérequis

Avant de lancer le projet, assurez-vous d’avoir installé :

Java 17+

Maven

Node.js 18+
 et npm

Angular CLI

Une base de données MySQL 

⚙️ Installation et exécution

1️⃣ Backend

# Dans le dossier backend
cd BackEnd

# Installer les dépendances
mvn clean install

# Lancer l'application Spring Boot
mvn spring-boot:run

2️⃣ Frontend

# Dans le dossier frontend
cd FrontEnd

# Installer les dépendances
npm install

# Lancer le serveur Angular
ng serve

🏗️ Structure du projet

📂 Backend (Spring Boot – architecture en couches)

BackEnd/
│── src/main/java/com/skytek/gestionreunions/
│   ├── entity/        # Entités JPA (tables de la base)
│   ├── repository/    # Repositories (DAO – accès BD)
│   ├── service/       # Services (logique métier)
│   ├── dto/           # Objets de transfert de données
│   ├── controller/    # API REST
│   ├── config/        # Configurations (sécurité, CORS…)
│   ├── mappers/       # Conversions Entity <-> DTO
│── src/main/resources/
│   ├── application.properties # Configuration BD, serveur…

📂 Frontend (Angular)

FrontEnd/
│── src/app/
│   ├── models/        # Définition des modèles de données
│   ├── services/      # Services pour appels API et logique
│   ├── components/    # Composants Angular (UI)
│   ├── guards/        # Guards (authentification / autorisation)
│   ├── interceptors/  # Intercepteurs HTTP
│── angular.json       # Configuration Angular
│── package.json       # Dépendances frontend

🔐 Sécurité

Authentification via JWT

Protection des routes avec Guards côté frontend

Gestion des rôles et permissions côté backend

Possibilité d’intégrer MFA (Multi-Factor Authentication)

📊 Fonctionnalités principales

👥 Gestion des utilisateurs

Création, modification et suppression de comptes utilisateurs.

Attribution de rôles et permissions (administrateur, utilisateur interne, utilisateur externe…).

Authentification sécurisée avec JWT et support du MFA (Multi-Factor Authentication) pour renforcer la sécurité.

Gestion des profils (informations personnelles, mot de passe, etc.).

🏢 Gestion des salles de réunion

Ajout, modification et suppression des salles avec leurs caractéristiques (capacité, équipements, localisation).

Réservation de salles en fonction de la disponibilité.

Système de vérification automatique pour éviter les conflits d’horaires.

📅 Gestion et suivi des réunions

Création de réunions avec description, date, heure et participants.

Intégration d’un calendrier interactif (FullCalendar) pour visualiser les réunions.

Envoi d’invitations et notifications aux participants.

Modification ou annulation des réunions avec mise à jour en temps réel.

⏳ Gestion des quotas et facturation

Attribution d’un quota mensuel d’heures de réservation pour les utilisateurs internes.

Consommation automatique du quota à chaque réservation validée.

Réinitialisation automatique du quota le 1er de chaque mois.

Facturation automatique en cas de dépassement du quota attribué.

📢 Gestion des réclamations et notifications

Système de gestion des réclamations pour les utilisateurs.

Notifications en temps réel (réservations confirmées, rappels de réunions, changements…).

Historique des réclamations et suivi de leur traitement.

📑 Rapports et statistiques

Génération de rapports détaillés sur :

Le nombre de réunions créées, annulées ou modifiées.

Le taux d’occupation des salles.

La consommation des quotas par utilisateur.

Les réunions passées et à venir (statistiques mensuelles).

Visualisation des statistiques sous forme de graphiques dynamiques.

👤 Auteur

👩‍💻 Takwa Saidi – Stage 2moins chez SKYTEK
