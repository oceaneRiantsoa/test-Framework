<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Formulaire Employé</title>
</head>
<body>
    <h2>Formulaire Employé (Sprint 8bis)</h2>
    <form action="${pageContext.request.contextPath}/emp/save" method="post">
        <label>Nom : <input type="text" name="emp.name" placeholder="Nom" /></label><br>
        <label>Département : <input type="text" name="emp.dept" placeholder="Dept" /></label><br>
        <label>Âge : <input type="number" name="emp.age" placeholder="Age" /></label><br>
        <label>Valeur i : <input type="number" name="i" placeholder="Valeur i" /></label><br>
        <button type="submit">Enregistrer</button>
    </form>
</body>
</html>