<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Formulaire Test Sprint 8</title>
</head>
<body>
    <h1>ETU3088</h1>
    <h2>Formulaire de test (Sprint 8)</h2>
    <form method="post" action="${pageContext.request.contextPath}/test/form">
        <label>Nom : <input type="text" name="nom" /></label><br>
        <label>Âge : <input type="number" name="age" /></label><br>
        <input type="submit" value="Envoyer" />
    </form>
</body>
</html>