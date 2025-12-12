<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Résultat Employé</title>
</head>
<body>
    <h2>Résultat de l'enregistrement</h2>
    <p style="color:green;">${message}</p>
    <p><strong>Nom :</strong> ${emp.name}</p>
    <p><strong>Département :</strong> ${emp.dept}</p>
    <p><strong>Âge :</strong> ${emp.age}</p>
    <p><strong>Valeur i :</strong> ${i}</p>
    <hr>
    <a href="${pageContext.request.contextPath}/emp/form">Retour au formulaire</a>
</body>
</html>