<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Formulaire Sprint 7</title>
</head>
<body>
    <h2>Formulaire d'insertion</h2>
    <form method="post" action="form/save">
        Nom : <input type="text" name="nom" /><br>
        Email : <input type="text" name="email" /><br>
        <input type="submit" value="Enregistrer" />
    </form>
    <hr>
    <h3>Infos insérées :</h3>
    <p>Nom : ${nom}</p>
    <p>Email : ${email}</p>
    <p style="color:green">${message}</p>
</body>
</html>