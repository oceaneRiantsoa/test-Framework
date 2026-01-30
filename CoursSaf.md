# Cours Servlet Avancé

## Sprint 1 : Mise en place du projet et premier servlet

Sprint 1 pose les bases du projet Java EE : configuration de l’environnement, création du premier servlet, et test d’une réponse simple.  
On découvre l’annotation `@WebServlet`, la méthode `doGet()`, et la configuration dans `web.xml`.  
Objectif : comprendre le cycle de vie d’un servlet et afficher "Hello World!" dans le navigateur.

---

## Sprint 1bis : FrontServlet et routage centralisé

Ce sprint introduit le pattern Front Controller.  
Toutes les requêtes passent par un seul servlet (`FrontServlet`) qui vérifie si la ressource existe :  
- Si oui, il la sert directement.
- Sinon, il affiche une erreur 404 ou redirige vers une gestion personnalisée.  
Cela simplifie la gestion des routes et des erreurs.

---

## Sprint 2 : Système d’annotations pour le routage

Sprint 2 ajoute la possibilité de mapper les URLs aux méthodes via l’annotation `@HandleURL`.  
Le framework scanne les méthodes annotées et les exécute selon l’URL demandée.  
On utilise la réflexion pour détecter et invoquer dynamiquement la bonne méthode.

---

## Sprint 2bis : Annotation @Controller sur les classes

On passe à une annotation au niveau des classes avec `@Controller("/basePath")`.  
Le scanner détecte toutes les classes contrôleurs et mappe automatiquement leurs méthodes.  
Cela structure le projet et facilite le scan des routes.

---

## Sprint 3 : Intégration annotations + FrontServlet

Sprint 3 relie le système d’annotations au FrontServlet.  
Au démarrage, toutes les routes sont scannées et enregistrées dans une map.  
À chaque requête, le FrontServlet consulte cette map et invoque la bonne méthode du contrôleur.

---

## Sprint 3bis : Corrections et robustesse

Ce sprint corrige les erreurs de package, de scan, et améliore la robustesse du système.  
On sépare bien le scan des routes (au démarrage) et leur invocation (à chaque requête).  
La gestion des erreurs et des logs est aussi améliorée.

---

## Sprint 4 : Gestion avancée des types de retour String

Sprint 4 impose que les méthodes de contrôleur retournent du HTML sous forme de `String`.  
Le FrontServlet affiche ce contenu dans une interface enrichie, avec navigation et informations sur la route exécutée.  
Les méthodes non supportées (autre que String) sont ignorées ou signalées.

## Sprint 5 - ModelView et passage de données (23 novembre 2025)

Sprint 5 introduit la classe `ModelView` pour retourner à la fois une vue (JSP) et des données dynamiques.  
Le contrôleur peut retourner un objet `ModelView` contenant le nom de la vue et une map de données (clé: nom du modèle, valeur: objet).  
Le FrontServlet détecte ce retour, place les données dans les attributs de la requête, puis fait un forward vers la JSP.  
Exemple :  
```java
public ModelView listDept(HttpServletRequest req, HttpServletResponse res) {
    ModelView mv = new ModelView("deptList.jsp");
    mv.addData("listeDept", getListeDept());
    return mv;
}
```
Avantages : séparation claire entre logique métier et affichage, passage facile de listes ou objets à la vue.  
Sprint 5 pose les bases pour le MVC : le contrôleur prépare les données, la vue les affiche.

---

## Sprint 6 - Injection automatique des paramètres (30 novembre 2025)

### Sprint 6 simple : Injection automatique des paramètres GET/POST

Dans ce sprint, le framework permet d'injecter automatiquement les valeurs des paramètres HTTP (GET ou POST) dans les arguments des méthodes de contrôleur.  
Par exemple, pour une route `/etudiant/details?id=5`, la méthode suivante reçoit directement la valeur :
```java
public String getEtudiant(HttpServletRequest req, HttpServletResponse res, @RequestParam("id") int id) {
    return "<h1>Détail étudiant #" + id + "</h1>";
}
```
Le système utilise la réflexion pour détecter l'annotation `@RequestParam`, lit la valeur depuis `request.getParameter("id")` et la convertit automatiquement au bon type.  
Avantage : le développeur n'a plus besoin d'appeler `getParameter` manuellement, tout est injecté dans la signature de la méthode.

---

### Sprint 6 bis : Gestion des routes ambiguës et valeurs par défaut

Ce sous-sprint gère les cas où plusieurs routes pourraient correspondre à une requête, ou quand certains paramètres sont absents.  
Si l'URL `/etudiant/{id}` est appelée sans valeur (`/etudiant/`), le framework injecte `null` ou une valeur par défaut dans le paramètre :
```java
public String getEtudiant(@RequestParam("id") Integer id) {
    if (id == null) return "<h1>Aucun étudiant sélectionné</h1>";
    return "<h1>Détail étudiant #" + id + "</h1>";
}
```
Le système vérifie la correspondance entre les paramètres attendus et ceux présents dans l'URL ou la requête.  
Si plusieurs routes sont possibles, il choisit la meilleure correspondance ou affiche une erreur claire.  
Cela rend le routage plus robuste et évite les erreurs de typage ou de conversion.

---

### Sprint 6 ter : Injection des valeurs depuis l'URL dynamique

Ce sous-sprint se concentre sur l'extraction et l'injection des valeurs directement depuis l'URL, notamment pour les routes dynamiques comme `/etudiant/{id}`.  
Quand l'utilisateur accède à `/etudiant/42`, le framework extrait `42` et l'injecte dans le paramètre `id` de la méthode :
```java
@HandleURL("/etudiant/{id}")
public String getEtudiantById(int id) {
    return "<h1>Détail étudiant #" + id + "</h1>";
}
```
La logique d'extraction utilise le pattern `{id}` pour repérer la variable dans l'URL, la convertir au bon type (int, String, etc.) et l'injecter dans la méthode.  
Si la valeur n'est pas présente, le système peut injecter `null` ou une valeur par défaut selon la signature.  
Ce mécanisme permet de créer des routes RESTful et de simplifier la gestion des paramètres dynamiques.

---

**Résumé Sprint 6 :**  
Le Sprint 6 et ses sous-sprints rendent le framework capable d'injecter automatiquement les paramètres HTTP et les valeurs dynamiques de l'URL dans les méthodes de contrôleur, avec gestion des cas ambigus et valeurs par défaut, pour une expérience développeur plus fluide et robuste.


## Sprint 7 - Séparation GET/POST avec annotations dédiées

Sprint 7 introduit deux annotations : `@Get` et `@Post` pour distinguer les méthodes HTTP dans les contrôleurs.  
Chaque méthode de contrôleur est annotée selon le type de requête qu’elle doit gérer :

```java
@Get("/produits/ajout")
public String afficherFormulaire(HttpServletRequest req, HttpServletResponse res) { ... }

@Post("/produits/ajout")
public String ajouterProduit(HttpServletRequest req, HttpServletResponse res) { ... }
```

Lors du scan, le framework enregistre chaque route avec son URL et le type HTTP (GET ou POST).  
À chaque requête, le FrontServlet vérifie l’URL **et** la méthode HTTP (`req.getMethod()`) pour choisir la bonne fonction à invoquer.  
Cela permet d’avoir deux méthodes différentes pour la même URL, selon que la requête est GET ou POST (ex : affichage du formulaire vs traitement du formulaire).

**Avantages :**
- Séparation claire entre affichage (GET) et traitement (POST)
- Respect des standards REST et MVC
- Routes plus flexibles et sécurisées

Sprint 7 prépare le framework à gérer des formulaires et des API REST avec un routage précis selon la méthode HTTP.


## Sprint 8 – Binding d’objet (POST) et ModelView avec Map<String, Object>

Objectif:
- Les méthodes POST des contrôleurs reçoivent directement une Map<String, Object> contenant les champs du formulaire.
- `ModelView` transporte une Map<String, Object> pour passer des données riches à la vue.
- Le FrontServlet injecte automatiquement la map à partir de `request.getParameterMap()` puis copie `mv.getData()` dans les attributs avant le forward.

Exemple contrôleur (POST avec Map):
```java
@PostMapping("/test/form")
public ModelView submitForm(java.util.Map<String, Object> form) {
    String nom = (String) form.get("nom");
    int age = 0;
    Object ageVal = form.get("age");
    if (ageVal instanceof String s && !s.isEmpty()) age = Integer.parseInt(s);

    ModelView mv = new ModelView("testResult.jsp");
    mv.addData("nom", nom);
    mv.addData("age", age);
    mv.addData("message", "Données reçues");
    return mv;
}
```

Formulaire (exemple):
```html
<form action="${pageContext.request.contextPath}/test/form" method="post">
  <input name="nom" placeholder="Nom">
  <input name="age" type="number" placeholder="Age">
  <button type="submit">Envoyer</button>
</form>
```

Rappels:
- `ModelView.addData(key, value)` remplit la Map transmise à la JSP.
- L’injection de la Map est faite dans FrontServlet (construction des arguments) à partir des paramètres de requête.
- Réf: [`com.itu.demo.tools.ModelView`](src/main/java/com.itu.demo/tools/ModelView.java), [`com.itu.demo.annotations.PostMapping`](src/main/java/com.itu.demo/annotations/PostMapping.java), [`com.itu.demo.FrontServlet`](src/main/java/com.itu.demo/FrontServlet.java), exemple: [`com.itu.demo.controllers.EmpController`](src/main/java/com.itu.demo/controllers/EmpController.java)

## Sprint 8bis – Binding automatique d’objet métier dans les contrôleurs

Objectif :
- Permettre à une méthode de contrôleur de recevoir directement un objet métier (ex : `Emp e`) et des paramètres simples (ex : `int i`) en arguments.
- Le FrontServlet instancie et remplit l’objet à partir des paramètres du formulaire, en utilisant la convention `emp.nom`, `emp.dept`, etc.

Fonctionnement :
- Lors de l’appel à une route POST, le FrontServlet analyse la signature de la méthode.
- Pour chaque paramètre de type objet (non primitif), il crée une instance et utilise la réflexion pour setter les propriétés à partir des paramètres de la requête.
- Les champs du formulaire doivent suivre la convention `emp.nom`, `emp.dept`, `emp.age` pour être correctement injectés.
- Les paramètres simples (ex : `int i`) sont injectés normalement via leur nom ou annotation.

Exemple contrôleur :
```java
@PostMapping("/emp/save")
public ModelView save(Emp e, int i) {
    ModelView mv = new ModelView("empResult.jsp");
    mv.addData("emp", e);
    mv.addData("message", "Emp enregistré avec i=" + i);
    mv.addData("i", i);
    return mv;
}
```

Exemple formulaire :
```html
<form action="${pageContext.request.contextPath}/emp/save" method="post">
  <input name="emp.name" placeholder="Nom">
  <input name="emp.dept" placeholder="Dept">
  <input name="emp.age" type="number" placeholder="Age">
  <input name="i" type="number" placeholder="Valeur i">
  <button type="submit">Enregistrer</button>
</form>
```
Avantages :
- Simplifie la gestion des objets complexes dans les contrôleurs.
- Permet de recevoir plusieurs types d’arguments (objets, primitives) dans une même méthode.
- Facilite le développement de formulaires avancés et le respect du pattern MVC.

## Sprint 9 – Exposition d’API REST et retour JSON

Objectif :
- Permettre à un contrôleur d’exposer une méthode en API REST qui retourne directement du JSON, sans passer par ModelView.
- Ajout d’une annotation `@Web` (ou `@Rest`) pour signaler qu’une méthode retourne une réponse API.
- La réponse JSON contient au minimum : `status` (success/error) et `data` (objet ou liste).

Fonctionnement :
- Lorsqu’une méthode de contrôleur est annotée avec `@Web`, le FrontServlet sérialise automatiquement la valeur de retour en JSON.
- Si la méthode retourne un objet ou une liste, le JSON généré inclut `{ "status": "success", "data": ... }`.
- En cas d’exception, le FrontServlet retourne `{ "status": "error", "message": "...", "data": null }`.

Exemple annotation et contrôleur :
```java
@Web("/api/emp/all")
public List<Emp> getAllEmps() {
    return empService.findAll();
}

@Web("/api/emp/one")
public Emp getEmp(@RequestParam("id") int id) {
    return empService.findById(id);
}
```

Exemple de réponse JSON :
```json
{
  "status": "success",
  "data": [
    { "name": "Alice", "dept": "IT", "age": 30 },
    { "name": "Bob", "dept": "RH", "age": 25 }
  ]
}
```

Points clés :
- Le FrontServlet détecte l’annotation `@Web` et change le content-type en `application/json`.
- La sérialisation peut utiliser une librairie comme Jackson ou une méthode utilitaire simple.
- Plus besoin de ModelView pour les routes REST : seule la donnée métier compte.

Avantages :
- Facile d’exposer des endpoints RESTful.
- Structure de réponse standardisée (status + data).
- Séparation claire entre vues HTML (ModelView) et API (JSON).

Réf : [`com.itu.demo.annotations.Web`](src/main/java/com.itu.demo/annotations/Web.java), [`com.itu.demo.FrontServlet`](src/main/java/com.itu.demo/FrontServlet.java)

## Sprint 10 – Upload de fichiers et injection dans les contrôleurs

Sprint 10 introduit la gestion de l’upload de fichiers via les formulaires HTML.  
Le FrontServlet détecte si la requête contient un fichier (multipart/form-data) et extrait les fichiers envoyés grâce à `request.getParts()`.  
Chaque fichier est encapsulé dans un objet (ex : `FileUpload`) contenant le nom, le contenu (byte[]), et le type MIME.

Fonctionnement :
- Lorsqu’un formulaire envoie un fichier, le FrontServlet sépare les données classiques (Map<String, Object>) et les fichiers (Map<String, FileUpload>).
- Les méthodes de contrôleur peuvent recevoir ces deux maps en argument, ou directement un objet `FileUpload` si besoin.
- Exemple de signature :
```java
@PostMapping("/file/upload")
public ModelView uploadFile(Map<String, Object> formData, Map<String, FileUpload> files) {
    // Traitement ici
}
```
- Le contrôleur peut alors accéder à la description, au fichier, et retourner une vue de résultat.

Avantages :
- Permet de gérer facilement les uploads de fichiers dans l’application.
- Injection automatique des fichiers et des autres champs du formulaire.

---

## Sprint 11 – Gestion avancée de la session dans les contrôleurs

Sprint 11 améliore la gestion de la session utilisateur côté contrôleur.  
Les méthodes peuvent recevoir une Map représentant la session (copie de la HttpSession) pour lire, ajouter ou supprimer des attributs de session.

Fonctionnement :
- Le FrontServlet détecte si un paramètre de la méthode est annoté `@Session` et de type `Map<String, Object>`.
- Il injecte une copie de la session courante dans ce paramètre.
- Après l’exécution de la méthode, les modifications apportées à la Map sont synchronisées avec la vraie session HTTP.
- Exemple :
```java
@GetMapping("/profil")
public ModelView profil(@Session Map<String, Object> session) {
    String username = (String) session.get("username");
    // ...
}
```

Avantages :
- Permet de manipuler la session sans dépendre directement de l’API Servlet.
- Facilite les tests et la maintenance du code.

---

## Sprint 11bis – Sécurité basée sur les rôles et annotations

Sprint 11bis introduit la gestion fine des droits d’accès via des annotations sur les méthodes de contrôleur.

Fonctionnement :
- Annotation `@AuthRequired` : la méthode nécessite une authentification.
- Annotation `@Role("chef")` : la méthode n’est accessible qu’aux utilisateurs ayant le rôle "chef".
- Sans annotation : la méthode est accessible à tous (rôle du développeur).
- Le FrontServlet vérifie les droits avant d’exécuter la méthode et redirige ou affiche une erreur si besoin.

Exemple :
```java
@Role("chef")
@GetMapping("/admin/reports")
public ModelView rapportsChef(@Session Map<String, Object> session) {
    // Accessible uniquement aux chefs
}
```

Avantages :
- Sécurisation centralisée et déclarative des routes.
- Contrôle précis des accès selon le rôle utilisateur.

---

sprint10: on va faire un upload de fichier via un formulaire et attaché un fichier, comment on va faire pour le mettre dans un formulaire d'action.
Dans frontservlet, on verifie si il y a un fichier attaché ou pas: par getParts pour pour obtenir Parts[]. On obtient son nom et le bytes[], quand on appelle la methode d'action on peut y mettre le nom et les bytes[] du fichier attaché. possibilité d'avoir 2Map: un fichier et l'autre les autres données.

mettre les objets en argument du controller sa atao singleton le controlleur 

sprint 11: 
ajout/recup/enlever session session :dans controller misy session (atao anaty methode pas dans httpsession)
map(String nomSession,Object value)
verification si mila session par les methodes porte voir si @param annoté session est de type map(String nomSession,Object value)
, de io no mitondra session.
parcourir variables httpsessios; (map ses = copy httpsession)
raha miova ses de miova le map httpsession viceversa



sprint11bis: 
on créera une  methode d'action dans controller: on va creer une annotation qui dit seul le role chef peut l'executer, ou quand c'est anonyme sans annotation,
1. @authRest: tsy maintsy authentifier
2. sans annotation: role du dev
3. @ profil : authentifier avec le profil en question


### Script de test mis à jour

```bat
@echo off
echo === TEST SPRINT 4 - RETOURS STRING ===
deploy.bat

echo.
echo Testez les URLs suivantes dans votre navigateur:
echo.
echo http://localhost:8080/testProject/accueil
echo http://localhost:8080/testProject/accueil/about  
echo http://localhost:8080/testProject/accueil/contact-us
echo http://localhost:8080/testProject/employes
echo http://localhost:8080/testProject/employes/stats
echo http://localhost:8080/testProject/employes/details?id=123
echo.
echo Pour voir toutes les routes disponibles:
echo http://localhost:8080/testProject/nonexistent
echo.
pause
```

## Structure du projet mise à jour (Sprint 6)
```
src/main/java/com.itu.demo/
├── FrontServlet.java                    (Front controller + ModelView + injection param)
├── annotations/
│   ├── HandleURL.java                   (mapping URL)
│   ├── Controller.java                  (déclaration contrôleur)
│   └── RequestParam.java                (injection paramètre méthode)
├── controllers/
│   ├── AccueilController.java           (retour String)
│   ├── Employe.java                     (retour String ou ModelView)
│   ├── Produit.java                     (exemple @RequestParam et {id})
│   └── UtilityClass.java
├── router/
│   └── Router.java                      (gestion des routes dynamiques)
├── tools/
│   ├── ControllerTest.java
│   ├── ControllerScanner.java           (scan + validation String/ModelView)
│   └── ModelView.java                   (Sprint 5 : passage de données à la vue)
└── testAnnotation/
    ├── TestAnnotation.java
    └── MainTest.java
```
