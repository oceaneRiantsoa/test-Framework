<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Résultat</title>
</head>
<body>
    <h2>Résultat du formulaire</h2>
    <p style="color:green;">${message}</p>
    <p><strong>Nom :</strong> ${nom}</p>
    <p><strong>Âge :</strong> ${age}</p>
    <hr>
    <a href="${pageContext.request.contextPath}/test/form">Retour au formulaire</a>
</body>
</html>