# Partie 1 — Spring Core : IoC et injection de dépendances

## 1. Qu’est-ce que Spring Core et à quoi sert-il dans une application Java ?

**Réponse :** Spring Core est le module fondamental du framework Spring. Il fournit notamment le conteneur IoC et les mécanismes d’injection de dépendances. Il aide à créer, configurer et relier les objets de l’application sans devoir gérer toutes leurs dépendances manuellement.

## 2. Que signifie le principe d’Inversion de Contrôle (IoC) ?

**Réponse :** L’IoC signifie que le contrôle de la création et de la gestion des objets est confié au framework plutôt qu’au code de l’application. Spring crée les objets nécessaires et gère leurs dépendances.

## 3. Quelle est la différence entre IoC et Injection de Dépendances (DI) ?

**Réponse :**
- **IoC** est le principe général : le framework prend le contrôle de la création et de la gestion des objets.
- **DI** est une manière de réaliser l’IoC : les dépendances sont fournies à un objet par Spring, par exemple via son constructeur.
## 4. Qu’est-ce qu’un bean dans Spring ?

**Réponse :** Un bean est un objet créé, configuré et géré par le conteneur Spring. Il peut être déclaré avec `@Component`, `@Service`, `@Repository`, `@Controller` ou avec une méthode annotée `@Bean`.

## 5. Quel est le rôle du conteneur IoC ?

**Réponse :** Le conteneur IoC crée les beans, injecte leurs dépendances, gère leur cycle de vie et leur portée (*scope*). Il lit la configuration XML, les annotations ou les classes Java de configuration.

## 6. Quelle est la différence entre ApplicationContext et BeanFactory ?

**Réponse :**
- `BeanFactory` fournit les fonctions essentielles du conteneur IoC.
- `ApplicationContext` étend ces fonctionnalités et apporte notamment la gestion des événements, l’accès aux ressources et une meilleure intégration avec les fonctionnalités d’entreprise.

Dans la plupart des applications Spring, on utilise `ApplicationContext`.

## 7. Quelles sont les trois approches de configuration dans Spring ?

**Réponse :**
1. **XML** : les beans et leurs dépendances sont définis dans un fichier XML.
2. **Annotations** : les classes sont déclarées avec des annotations comme `@Component` et `@Service`.
3. **Java Config** : la configuration est écrite dans des classes Java annotées avec `@Configuration`, contenant éventuellement des méthodes `@Bean`.

Ces approches peuvent être combinées, mais une configuration cohérente est préférable.

## 8. À quoi sert l’annotation `@Configuration` ?

**Réponse :** `@Configuration` indique qu’une classe contient une configuration Spring, notamment des méthodes annotées avec `@Bean`.

```java
@Configuration
public class RootConfig {
}
```

## 9. À quoi sert l’annotation `@ComponentScan` ?

**Réponse :** `@ComponentScan` indique à Spring les packages dans lesquels rechercher les classes annotées avec `@Component` et ses spécialisations, comme `@Service`, `@Repository` et `@Controller`.

```java
@Configuration
@ComponentScan("com.usermanagement.service")
public class RootConfig {
}
```

## 10. À quoi sert l’annotation `@Bean` ?

**Réponse :** `@Bean` indique que le résultat d’une méthode doit être enregistré comme bean Spring. Elle est utile pour configurer des classes tierces ou des objets dont on ne peut pas modifier le code source.

```java
@Configuration
public class AppConfig {
    @Bean
    public MyService myService() {
        return new MyService();
    }
}
```

## 11. À quoi servent `@Component`, `@Service`, `@Repository` et `@Controller` ?

**Réponse :**
- `@Component` : composant Spring générique.
- `@Service` : composant de la couche métier.
- `@Repository` : composant d’accès aux données ; permet aussi la traduction de certaines exceptions de persistance.
- `@Controller` : composant de la couche web, généralement utilisé avec Spring MVC pour retourner une vue.

Ces annotations permettent la détection automatique des composants lorsque le package est couvert par `@ComponentScan`.

## 12. À quoi servent `@Autowired` et `@Qualifier` ?

**Réponse :**
- `@Autowired` demande à Spring d’injecter une dépendance.
- `@Qualifier` permet de préciser quel bean injecter lorsqu’il existe plusieurs beans compatibles.

L’injection par constructeur est généralement recommandée. Si une classe possède un seul constructeur, `@Autowired` n’est habituellement pas nécessaire.

```java
@Service
public class NotificationService {
    private final MessageSender sender;

    public NotificationService(@Qualifier("emailSender") MessageSender sender) {
        this.sender = sender;
    }
}
```

## 13. Comment Spring détecte et crée automatiquement les composants ?

**Réponse :** Spring parcourt les packages déclarés dans `@ComponentScan`. Il détecte les classes portant une annotation de composant, construit les beans correspondants, résout leurs dépendances et les enregistre dans le conteneur.

La détection automatique nécessite une configuration de scan adaptée.

## 14. Quelles sont les étapes du cycle de vie d’un bean ?

**Réponse :** De façon simplifiée :
1. Spring instancie le bean.
2. Spring injecte ses dépendances.
3. Les méthodes de préparation et les post-processeurs sont exécutés.
4. La méthode annotée `@PostConstruct` peut effectuer l’initialisation.
5. Le bean est prêt à être utilisé.
6. À la destruction du contexte, la méthode `@PreDestroy` peut être appelée.

Les callbacks de destruction ne sont pas automatiquement gérés de la même façon pour tous les scopes ; par exemple, Spring ne suit généralement pas la destruction des beans `prototype` après leur remise au client.

## 15. Quelle est la différence entre les scopes de bean ?

**Réponse :**
- `singleton` : une seule instance par conteneur Spring ; c’est le scope par défaut.
- `prototype` : une nouvelle instance est créée chaque fois que le bean est demandé au conteneur.
- `request` : une instance par requête HTTP, dans une application web.
- `session` : une instance par session HTTP, dans une application web.
- `application` : une instance associée au `ServletContext`.

Les scopes web nécessitent un environnement web adapté.

## 16. Pourquoi la configuration manuelle avant Spring Boot est-elle importante à comprendre ?

**Réponse :** Elle permet de comprendre ce que Spring Boot automatise : création du contexte, configuration des beans, connexion à la base, configuration MVC et démarrage du serveur. Dans une application classique, le développeur doit déclarer explicitement ces éléments et maîtriser leurs relations.

---

# Partie 2 — Spring Data JPA : persistance et transactions

## 17. Qu’est-ce que Spring Data JPA et quel problème résout-il ?

**Réponse :** Spring Data JPA simplifie l’accès aux données avec JPA. Il permet de déclarer des interfaces de repository et génère leur implémentation à l’exécution. Cela réduit le code répétitif nécessaire pour les opérations CRUD et certaines requêtes.

## 18. Quelle est la différence entre JPA et Hibernate ?

**Réponse :**
- **JPA** est une spécification Java qui définit une API de persistance objet-relationnelle.
- **Hibernate** est une implémentation de cette spécification, capable de mapper les objets Java vers les tables d’une base de données.

Spring Data JPA s’appuie sur un fournisseur JPA, souvent Hibernate.

## 19. Qu’est-ce qu’une entité JPA ?

**Réponse :** Une entité est une classe Java persistée en base de données. Elle est généralement annotée avec `@Entity` et possède un identifiant annoté avec `@Id`.

```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    private String email;
}
```

Les stratégies d’identifiant et les types utilisés doivent être compatibles avec le fournisseur JPA et la base de données.

## 20. À quoi sert le `DataSource` ?

**Réponse :** Le `DataSource` fournit des connexions à la base de données. Il contient ou utilise les paramètres nécessaires à la connexion et peut s’appuyer sur un pool de connexions, comme HikariCP, afin de réutiliser les connexions efficacement.

## 21. Que fait l’`EntityManager` ?

**Réponse :** L’`EntityManager` est l’API JPA qui permet de gérer les entités persistantes. Il propose notamment :
- `persist()` : rendre une nouvelle entité persistante ;
- `find()` : rechercher une entité par son identifiant ;
- `merge()` : recopier l’état d’une entité détachée dans une entité gérée ;
- `remove()` : programmer la suppression d’une entité gérée ;
- `createQuery()` : créer une requête JPQL.

Les changements sont synchronisés avec la base lors du *flush*, généralement au cours de la transaction.

## 22. Quelle est la responsabilité du `TransactionManager` ?

**Réponse :** Le gestionnaire de transactions coordonne le début, la validation (*commit*) et l’annulation (*rollback*) des transactions. Avec JPA, `JpaTransactionManager` est couramment utilisé pour gérer les transactions d’un `EntityManagerFactory`.

## 23. À quoi sert `@EnableJpaRepositories` ?

**Réponse :** Cette annotation active la détection des interfaces de repository Spring Data JPA dans les packages indiqués et permet à Spring de créer leurs implémentations dynamiques.

```java
@Configuration
@EnableJpaRepositories("com.usermanagement.repository")
public class PersistenceConfig {
}
```

La configuration doit aussi fournir les composants de persistance nécessaires, notamment un `EntityManagerFactory` et un gestionnaire de transactions.

## 24. Qu’est-ce qu’un repository Spring Data ?

**Réponse :** Un repository est une interface qui représente la couche d’accès aux données. En étendant `JpaRepository`, elle hérite de méthodes CRUD et de pagination sans qu’il soit nécessaire d’écrire soi-même une implémentation pour les opérations courantes.

```java
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
```

Spring Data peut dériver certaines requêtes à partir du nom des méthodes.

## 25. Quelles sont les méthodes génériques fournies par `JpaRepository` ?

**Réponse :** Les méthodes courantes comprennent :
- `save(entity)` : enregistrer ou mettre à jour selon l’état de l’entité ;
- `findById(id)` : rechercher par identifiant ;
- `findAll()` : récupérer les entités ;
- `existsById(id)` : vérifier l’existence ;
- `deleteById(id)` : supprimer par identifiant ;
- `delete(entity)` : supprimer une entité ;
- `count()` : compter les entités ;
- `findAll(Pageable)` : récupérer une page ;
- `flush()` : synchroniser le contexte de persistance avec la base.

Le comportement exact dépend de la configuration et de la version utilisée.

## 26. Comment gérer les transactions avec Spring ?

**Réponse :** On utilise généralement `@Transactional` sur une méthode de service ou une classe de service. Spring ouvre la transaction, exécute la méthode puis valide ou annule la transaction selon le résultat.

```java
@Service
public class UserService {
    @Transactional
    public void createUser() {
        // opérations de persistance
    }
}
```

Points importants :
- `propagation` définit comment la transaction interagit avec une transaction existante.
- `REQUIRED` rejoint une transaction existante ou en crée une.
- `REQUIRES_NEW` suspend la transaction existante et en démarre une nouvelle.
- Par défaut, Spring effectue généralement un rollback pour les exceptions non contrôlées (`RuntimeException` et `Error`), mais pas pour toutes les exceptions contrôlées.
- `rollbackFor` permet de préciser les exceptions qui doivent provoquer un rollback.

## 27. Pourquoi définir manuellement la connexion à la base de données avant Spring Boot ?

**Réponse :** Sans Spring Boot, le développeur configure explicitement le `DataSource`, l’URL JDBC, le pilote, les identifiants et éventuellement le pool de connexions. Cela permet de comprendre la chaîne de connexion et de contrôler la configuration au lieu de dépendre de l’auto-configuration.

Les mots de passe doivent être stockés dans des variables d’environnement ou un système de configuration sécurisé, et non dans le dépôt Git.

## 28. Que doit contenir une configuration de persistance complète ?

**Réponse :** Une configuration classique contient :
1. **DataSource** : URL JDBC, pilote, identifiants et pool de connexions.
2. **EntityManagerFactory** : fournisseur JPA, packages d’entités et propriétés Hibernate.
3. **TransactionManager** : généralement `JpaTransactionManager`.
4. **Activation des repositories** : `@EnableJpaRepositories`.
5. **Activation des transactions** : `@EnableTransactionManagement`.
6. **Configuration des propriétés** : par exemple via `application.properties`.

Exemple simplifié :

```java
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.usermanagement.repository")
public class PersistenceConfig {

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factory =
                new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan("com.usermanagement.entity");
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(
            EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
```

Cet exemple suppose qu’un bean `DataSource` est déclaré ailleurs. Les propriétés du fournisseur et de la base doivent être ajoutées selon le projet.

## 29. Qu’est-ce que la validation de contrainte dans le modèle ?

**Réponse :** La validation vérifie que les données respectent des règles, grâce notamment à Jakarta Bean Validation et à des annotations telles que :
- `@NotBlank` : chaîne non vide après suppression des espaces ;
- `@Email` : format d’adresse électronique ;
- `@Size(min = 2, max = 50)` : longueur autorisée ;
- `@NotNull` : valeur obligatoire.

Exemple :

```java
@NotBlank
@Email
@Size(max = 150)
private String email;
```

Une annotation `@Email` ne garantit pas que l’adresse est unique. L’unicité doit être assurée par une contrainte `UNIQUE` en base, avec éventuellement une vérification préalable dans le service. La validation applicative ne remplace pas les contraintes de base de données.

## 30. Quelle est la différence entre une suppression logique (soft delete) et une suppression physique ?

**Réponse :**
- **Suppression physique (*hard delete*)** : la ligne est réellement supprimée de la base.
- **Suppression logique (*soft delete*)** : la ligne est conservée, mais marquée comme supprimée, par exemple avec `deleted = true` ou `deletedAt`.

La suppression logique permet de conserver l’historique, mais les requêtes doivent exclure les enregistrements supprimés lorsque c’est nécessaire. La suppression physique peut être plus simple, mais elle peut faire perdre définitivement des données.

---

# Partie 3 — Spring MVC : contrôleurs et couche web

## 31. Que signifie MVC (Model-View-Controller) et quel est son objectif dans Spring ?

**Réponse :** MVC signifie *Model-View-Controller* :
- **Model** : données et informations présentées ;
- **View** : interface affichée, par exemple une page JSP ;
- **Controller** : reçoit les requêtes, appelle les services et choisit la réponse.

Ce modèle sépare les responsabilités et facilite la maintenance. Dans une API REST, la réponse est souvent un objet sérialisé en JSON plutôt qu’une vue HTML.

## 32. Quel est le rôle du `DispatcherServlet` dans Spring MVC ?

**Réponse :** Le `DispatcherServlet` est le contrôleur frontal (*Front Controller*) de Spring MVC. Il reçoit les requêtes web mappées sur lui, trouve le contrôleur adapté, coordonne le traitement et prépare la réponse HTTP.

## 33. Quelle est la différence entre `@Controller` et `@RestController` ?

**Réponse :**
- `@Controller` est utilisé principalement pour les contrôleurs MVC qui retournent des noms de vues.
- `@RestController` combine le comportement de `@Controller` et de `@ResponseBody`. Les valeurs retournées par les méthodes sont écrites dans le corps de la réponse, généralement au format JSON pour une API REST.

## 34. À quoi sert `@RequestMapping` ?

**Réponse :** `@RequestMapping` associe une classe ou une méthode à une ou plusieurs routes HTTP. Elle peut préciser le chemin, la méthode HTTP, le type de contenu accepté ou produit.

```java
@RequestMapping("/api/users")
@RestController
public class UserController {
}
```

## 35. À quoi servent `@GetMapping`, `@PostMapping`, `@PutMapping` et `@DeleteMapping` ?

**Réponse :** Ce sont des annotations spécialisées pour associer une méthode de contrôleur à une méthode HTTP :
- `@GetMapping` : lecture de ressources ;
- `@PostMapping` : création ou traitement d’une demande ;
- `@PutMapping` : remplacement ou mise à jour d’une ressource ;
- `@DeleteMapping` : suppression d’une ressource.

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    @GetMapping
    public List<UserDto> findAll() { /* ... */ return List.of(); }

    @PostMapping
    public UserDto create(@RequestBody UserDto dto) { /* ... */ return dto; }

    @PutMapping("/{id}")
    public UserDto update(@PathVariable UUID id,
                          @RequestBody UserDto dto) { /* ... */ return dto; }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) { /* ... */ }
}
```

Les corps de méthode sont illustratifs : dans une application réelle, le contrôleur délègue le traitement à un service.

## 36. À quoi sert `@Valid` ?

**Réponse :** `@Valid` demande à Spring de déclencher la validation d’un objet selon les contraintes déclarées sur ses champs. Dans un contrôleur, elle est souvent utilisée avec `@RequestBody` pour valider les données reçues.

```java
@PostMapping
public UserDto create(@Valid @RequestBody CreateUserRequest request) {
    return userService.createUser(request);
}
```

Les erreurs de validation doivent être transformées en réponses HTTP claires, par exemple `400 Bad Request`.

## 37. Quelle est la différence entre `@RequestBody` et `@PathVariable` ?

**Réponse :**
- `@RequestBody` lit le corps de la requête et convertit son contenu, souvent JSON, en objet Java.
- `@PathVariable` récupère une valeur incluse dans le chemin de l’URL.

Exemple : dans `PUT /api/users/123`, `123` peut être récupéré avec `@PathVariable`. Les informations de l’utilisateur peuvent être envoyées en JSON et lues avec `@RequestBody`.

## 38. Comment le `DispatcherServlet` traite-t-il une requête HTTP du début à la fin ?

**Réponse :** Le déroulement typique est le suivant :
1. Le client envoie une requête HTTP au serveur web.
2. Le serveur transmet la requête au `DispatcherServlet` si le mapping correspond.
3. Spring trouve le contrôleur et la méthode adaptés grâce aux mappings.
4. Les paramètres sont extraits et convertis ; la validation peut être exécutée.
5. Le contrôleur appelle généralement un service métier.
6. Le contrôleur retourne une vue ou un objet.
7. Pour une API REST, un convertisseur HTTP, comme Jackson, écrit l’objet dans le corps de la réponse.
8. Spring renvoie la réponse HTTP au client.

Des filtres Servlet et des intercepteurs Spring peuvent intervenir avant ou pendant ce processus.

## 39. Qu’est-ce que la classe `WebConfig` et à quoi sert-elle ?

**Réponse :** `WebConfig` est une classe de configuration Java de la couche web. Elle est généralement annotée avec `@Configuration` et `@EnableWebMvc`. Elle peut configurer le scan des contrôleurs, les convertisseurs, les validateurs, les intercepteurs, les ressources statiques et d’autres éléments de Spring MVC.

```java
@Configuration
@EnableWebMvc
@ComponentScan("com.usermanagement.controller")
public class WebConfig {
}
```

La configuration précise dépend de l’application.

## 40. Pourquoi faut-il initialiser le `DispatcherServlet` manuellement avant Spring Boot ?

**Réponse :** Dans une application Spring classique déployée sur un serveur Servlet, il faut configurer le `DispatcherServlet` et le contexte Spring. Cette configuration peut être réalisée avec `web.xml` ou avec une classe Java telle que `WebAppInitializer`. Spring Boot automatise généralement cette partie, mais sans Boot, le développeur doit la définir explicitement.

## 41. Qu’est-ce qu’un `WebAppInitializer` et pourquoi remplace-t-il `web.xml` ?

**Réponse :** Un `WebAppInitializer` est une classe Java qui configure le démarrage de Spring MVC dans un environnement Servlet compatible. En étendant `AbstractAnnotationConfigDispatcherServletInitializer`, elle permet de déclarer les classes de configuration et le mapping du `DispatcherServlet` sans écrire de `web.xml`.

```java
public class WebAppInitializer
        extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] { RootConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { WebConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
}
```

Le conteneur Servlet doit prendre en charge l’initialisation Servlet requise pour détecter cette classe. Les classes racines contiennent généralement les services et la persistance, tandis que la configuration du servlet contient les composants web.

## 42. Quelles sont les étapes de traitement d’une requête REST dans Spring MVC ?

**Réponse :**
1. Le client envoie une requête HTTP, par exemple `POST /api/users`.
2. Le `DispatcherServlet` reçoit la requête.
3. Spring identifie le contrôleur grâce aux mappings.
4. Jackson convertit le JSON reçu en objet Java si un `@RequestBody` est utilisé.
5. Spring exécute la validation si elle est demandée avec `@Valid`.
6. Le contrôleur appelle le service, qui applique les règles métier et utilise les repositories.
7. Le contrôleur retourne un DTO et, si nécessaire, un statut HTTP.
8. Jackson sérialise le DTO en JSON.
9. Le client reçoit la réponse HTTP.

Les erreurs peuvent être traitées par un gestionnaire global comme `@RestControllerAdvice`.

## 43. Comment se fait la sérialisation et la désérialisation des objets JSON ?

**Réponse :**
- **Désérialisation** : conversion du JSON reçu en objet Java.
- **Sérialisation** : conversion d’un objet Java en JSON envoyé au client.

Spring MVC utilise des `HttpMessageConverter`. Pour JSON, le convertisseur s’appuie généralement sur Jackson.

Exemple de JSON reçu :
```json
{
  "firstName": "Yassir",
  "email": "yassir@example.com"
}
```

Un paramètre annoté avec `@RequestBody` peut recevoir cet objet converti. Pour éviter d’exposer directement les entités JPA, il est généralement préférable d’utiliser des DTO adaptés à l’API.

## 44. À quoi sert `@RestControllerAdvice` ?

**Réponse :** `@RestControllerAdvice` permet de centraliser le traitement des exceptions de plusieurs contrôleurs REST. Avec des méthodes annotées par `@ExceptionHandler`, elle peut transformer les exceptions en réponses JSON cohérentes avec les bons statuts HTTP.

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleNotFound(
            UserNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }
}
```

Dans une application réelle, un DTO d’erreur structuré est souvent préférable à une simple chaîne de caractères.

## 45. Quelles sont les bonnes pratiques pour organiser les packages d’un projet MVC ?

**Réponse :** Il faut séparer les responsabilités et éviter de mélanger contrôleurs, logique métier et accès aux données. Une organisation possible est :

```text
com.usermanagement
├── config
│   ├── RootConfig.java
│   ├── PersistenceConfig.java
│   └── WebConfig.java
├── controller
├── dto
│   ├── request
│   └── response
├── entity
├── exception
├── mapper
├── repository
├── service
│   └── impl
└── validation
```

Bonnes pratiques :
- les contrôleurs gèrent le protocole HTTP et délèguent la logique métier aux services ;
- les services portent les règles métier et les frontières transactionnelles ;
- les repositories gèrent l’accès aux données ;
- les DTO définissent les données échangées avec les clients ;
- les entités représentent le modèle de persistance ;
- la gestion des exceptions doit être cohérente ;
- les dépendances doivent être injectées, de préférence par constructeur ;
- les secrets et mots de passe ne doivent pas être codés en dur.

---

# Récapitulatif

- **Spring Core** : conteneur IoC, beans et injection de dépendances.
- **Spring Data JPA** : repositories, persistance et accès aux données.
- **Spring Transactions** : gestion des transactions avec `@Transactional`.
- **Spring MVC** : traitement des requêtes HTTP via `DispatcherServlet` et les contrôleurs.
- **Configuration sans Spring Boot** : déclaration explicite des configurations racine, persistance et web, ainsi que l’initialisation du servlet.