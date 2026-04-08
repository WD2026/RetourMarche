# Accès aux Services et Outils

Ce document liste les commandes pour démarrer l'infrastructure du projet RetourMarche et les URLs pour y accéder localement.

## Démarrer les services

Pour démarrer correctement la base de données, l'application et tous les outils de monitoring / logs gérés par Docker Compose, exécutez la commande suivante à la racine du projet (`/home/cytech/01_Kasetsart/WebProjet/RetourMarche`) :

```bash
docker compose up -d
```

Cette commande lancera les conteneurs en arrière-plan.

*(Si vous avez fait des modifications dans le code de l'application, pensez à reconstruire l'image de l'application avec `docker compose up -d --build app`)*.

---

## Adresses Locales

Une fois les services démarrés, voici comment y accéder via votre navigateur :

### 1. Application Principale (RetourMarche)
- **URL** : [http://localhost:8080](http://localhost:8080)
- **Reverse Proxy Nginx** : [http://localhost:8081](http://localhost:8081)

### 2. Logs en temps réel (Dozzle)
Dozzle offre une interface lisible pour voir l'ensemble des logs de tous les conteneurs, au format JSON ou texte standard. Les événements de connexion de l'application y seront clairement visibles.
- **URL** : [http://localhost:8888](http://localhost:8888)

### 3. Monitoring et Dashboards (Grafana)
Tableaux de bord visuels pour la surveillance des requêtes, des temps de latence et des ressources.
- **URL** : [http://localhost:3000](http://localhost:3000)
- **Identifiants par défaut** : 
  - Utilisateur : `admin`
  - Mot de passe : `admin`

### 4. Métriques de l'Application (Prometheus)
Outil qui collecte les métriques brutes de l'application (exposées via l'actuator).
- **URL** : [http://localhost:9090](http://localhost:9090)
