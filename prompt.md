Sprint 6:, on a formulaire ajoutdep (html), on a balise form avec url, on a deptcontroller avec methode save en associant avec url /dep/save, comment récupérer les données saisies d un formulaire, ne pas utiliser request get parameter:1) dans une page fiche etudiant= etud/id, dans etudiant controller on peut avoir get associé url avec int id avec requestparam de variable différents, on règle 3 choses: cas
1:merge différent, permettre url que quand /2 on arrive dans, cas
2: qunad on a accolade c est ce qu'on appelle dans get, on regarde si y a argument dans methode, si y a la meme nom commme get request parameter du formulaire et on le prend .
Pour faire conversion on utilise librairie





Sprint 6 : 
- Asiana arguments amin'izay ilay méthodes mappena amin'ilay annotation @UrlMapping iny dia ilay arguments asaina tadiaviny ao amin'ny requête nahatongavana tany @le endpoint (request.getParameter({anaranle-argument})

Sprint 6-bis : 
Mamorona annotation @RequestParam ho an'ilay arguments anle fonction endpoint dia ilay valeur ao anatiny no clé hitadiavana anle paramètre anatinle requête 
Ohatra hoe raha misy 
voidfonctionTest(@RequestParam("nbr") int nombre) dia tadiavina @ request.getParameter("nbr") ilay valeur tokony hi-invoke-na anle méthode fonctionTest

Sprint 6-ter : 
Atao afaka mirécupère an'ilay {valeur} ao anatin'ilay URL ilay framework dia lasa ireny (raha misy) no injecter-na ao amin'ilay méthode endpoint eo amin'ny placen'ilay argument mitovy anarana aminy
Ohatra hoe fonctionTest(int id) izany mitady {id} ao amle URL dia izay valeur hitany ao no ampiasainy
Dia mba hanamoraina ny fiainana amty ray ty ozy Mr Naina hoe asio ordre de priorité fotsiny ohatra hoe raha tss anle variable anaty url dia tadiavo anaty request ilay variable sinon soloy null na mithrow-eva Exception (exemple ana ordre io anah io fa izay tianareo)