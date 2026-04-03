# RetourMarche - Application de e-commerce d'achat de smartphones et d'accessoires reconditionnés

## Grille d'évaluation

### Fonctionnalités: 9/10

#### l'application contient bien les fonctionnalités demandées
**SATISFAIT** - L'application implémente un système complet de e-commerce avec :
- Catalogue de produits (Smartphones et Accessoires)
- Recherche et filtrage par catégories
- Système de panier et commandes
- Gestion administrateur (dashboard)
- Authentification utilisateur
- Profils utilisateur
- Système de codes promo
- Assurances produits

#### l'application permet d'insérer, mettre à jour, supprimer, chercher une entité en BDD
**SATISFAIT** - Opérations CRUD complètes pour toutes les entités :
- **INSERT**: ProductController (addSmartphone, addAccessoire), AuthController (register)
- **UPDATE**: DashboardController (updateSmartphone, updateAccessoire, updateUser, updateStock)
- **DELETE**: DashboardController (deleteSmartphone, deleteAccessoire, deleteUser)
- **SEARCH**: HomeController (recherche par nom/marque), ProductRepository (requêtes SQL paramétrées)

#### l'application permet de lier deux entités en BDD
**SATISFAIT** - Relations bidirectionnelles implementées :
- **Smartphone ↔ Accessoire** (@ManyToMany) : Les accessoires peuvent être associés à des smartphones
- **Order ↔ OrderItem** (@OneToMany) : Une commande contient plusieurs articles
- **Cart ↔ Product** (@ManyToOne) : Le panier référence les produits
- **OrderItem ↔ Product** (@ManyToOne) : Les articles de commande référencent les produits

#### l'application permet, pour une entité donnée, de créer un lien à une autre entité en BDD
**SATISFAIT** - Création de liaisons dynamiques :
- Ajout d'accessoires à des smartphones existants via le dashboard
- Création de commandes avec ajout automatique d'articles au panier
- Association d'assurances aux produits commandés (InsuranceType)
- Liaison automatique des commandes aux utilisateurs

#### l'application est jolie / utilise un framework CSS
**SATISFAIT** - Framework Bootstrap 5.3.8 implémenté :
- Utilisation de composants Bootstrap (carousel, cards, forms, modals)
- Design responsive et moderne
- Personnalisation CSS additionnelle dans les templates
- Interface intuitive et cohérente

---

### Technique: 4.5/5

#### l'application utilise le design pattern MVC pour chaque fonctionnalité
**SATISFAIT** - Architecture MVC stricte :
- **Model** : Entités JPA (User, Product, Smartphone, Accessoire, Order, Cart, etc.)
*Remarque* Le choix et la logique d'entités peut être améliorée
- **View** : Templates Thymeleaf (index.html, dashboard.html, basket.html, etc.)
- **Controller** : 
  - AuthController (authentification)
  - HomeController (accueil, recherche, profil)
  - ProductController (ajout produits)
  - CartController (gestion panier)
  - DashboardController (administration)
- Repositories pour accès aux données (Spring Data JPA)

#### les controlleurs utilisent les méthodes HTTP: GET, POST, PUT, DELETE
**SATISFAIT** - Utilisation de GET et POST présentes :
- **GET** : HomeController (@GetMapping), ProductController (@GetMapping), DashboardController (@GetMapping)
- **POST** : AuthController, ProductController (@PostMapping), CartController (@PostMapping), DashboardController (@PostMapping)
- **DELETE** : DashboardController (@GetMapping "/delete*") - implémenté mais via GET plutôt que DELETE
- **PUT** : Non utilisé (opérations UPDATE implémentées via POST)

#### chaque vue manipule des données transmises par son controlleur
**SATISFAIT** - Utilisation cohérente de Thymeleaf :
- Modèles de données passés via Model.addAttribute()
- Affichage dynamique des produits, utilisateurs, commandes
- Conditions d'affichage basées sur les données (user role, stock disponible, etc.)
- Formulaires pré-remplis avec les données du modèle
- Pagination et filtrage appliqués côté vue

---

### Soutenance et Evaluation individuelle: 4.5/5

#### la soutenance est bien préparée
**SATISFAIT** - Présentation bien préparée
*Remarque* De légers détails sur le diaporama peuvent être améliorés

#### chacun parle suffisamment
**SATISFAIT** - Chaque membre participe activement

#### le timing est respecté
**SATISFAIT** - Durée respectée

#### les réponses aux questions sont correctes
**SATISFAIT** - Réponses précises et justifiées

#### le repo comporte des commits réguliers de chaque membre du groupe
**SATISFAIT** - Historique Git avec commits réguliers de tous les membres

#### Note finale : 18/20

Boutique : http://localhost:8081 (via Nginx) ou http://localhost:8080 (direct)
Logs (Dozzle) : http://localhost:8888
Metrics (Grafana) : http://localhost:3000 (admin / admin)
Prometheus : http://localhost:9090
