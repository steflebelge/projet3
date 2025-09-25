# Projet3

**Projet3** est une API Spring Boot pour la gestion de locations et de messages associés, avec authentification JWT, gestion des utilisateurs, et upload d’images pour les locations.

---

## Technologies

Le projet utilise les technologies et dépendances suivantes :

- **Java 17** : langage principal du projet.
- **Spring Boot 3.5.4** : framework principal pour la création de l’API REST et la gestion du serveur.
  - **spring-boot-starter-web** : création des endpoints REST et gestion des requêtes HTTP.
  - **spring-boot-starter-data-jpa** : intégration JPA/Hibernate pour la persistance avec MariaDB.
  - **spring-boot-starter-security** : sécurité, gestion des utilisateurs et intégration Spring Security.
  - **spring-boot-starter-oauth2-resource-server** : support JWT et OAuth2 pour l’authentification stateless.
  - **spring-boot-starter-validation** : validation des DTOs avec annotations `@Valid`, `@NotNull`, `@Size`, etc.
- **Spring Security OAuth2 JOSE** : pour encoder et décoder les JWT.
- **MariaDB Java Client** : driver JDBC pour MariaDB.
- **Lombok** : génération automatique de getters, setters, constructeurs, etc.
- **Springdoc OpenAPI (springdoc-openapi-starter-webmvc-ui 2.3.0)** : documentation Swagger UI pour l’API REST.
- Upload et gestion des images via `MultipartFile`

---

## Architecture du projet

- **Auth** : Contient un provider pour gérer l'authentification des utilisateurs
- **DTO** : Validation et formatage des requetes et réponses
- **Model (JPA)** : Définition des modèles utilisés en base
- **Repositories** : Déclaration des repository pour génération automatique des CRUDs et ajout de fonctions spécifiques au besoin.
- **Service** : Chaque entité a son service qui permet au controlleur de délégué certaines taches
- **Services** : Fichier qui permet la génération des JWT.
- **Controllers** : Endpoints REST pour communication avec l'API depuis le front.
- **Utils** : `DateUtils` pour formater les timestamps.
- **Configuration** : Fichiers de configurations pour la sécurité, les fichiers statiques et le swagger.

---

## Installation

1. Clone le projet :

```bash
git clone <repo-url>
cd projet3_save_old
./mvnw clean install -Dmaven.test.skip=true && ./mvnw spring-boot:run
```

## Documentation Swagger / OpenAPI

L’API dispose d’une documentation interactive générée automatiquement via **Springdoc OpenAPI**.  

- **Swagger UI** : permet de visualiser et tester tous les endpoints de l’API directement depuis le navigateur.  
- **Upload et gestion des images** : les routes `POST` et `PUT` de `rentals` utilisent `MultipartFile` pour l’upload d’images.  

### URL d’accès

- Swagger UI : [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)  
- JSON de l’API : [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)  

### Notes

- Swagger UI est accessible sans authentification.  
- Pour tester les endpoints sécurisés, ajoutez le token JWT dans le champ **Authorize** de Swagger UI.


## Initialisation de la base de données

Pour initialiser la base de données, exécuter le script `script.sql`.