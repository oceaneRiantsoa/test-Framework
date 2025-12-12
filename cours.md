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




sprint 9: 
possibilité pour exposer en api rest, retourner json tsy miraharaha hoe modelView ve fa le data no alaina; ajout annotation "web annontation",
status: success, error
data: 
MAMPIASA LIBRAIRIE
maka departement , (liste infini)
izay objet eo no


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
   