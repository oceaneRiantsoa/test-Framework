<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Résultat Connexion</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 500px; margin: 50px auto; padding: 20px; }
        .success { background-color: #d4edda; border: 1px solid #c3e6cb; color: #155724; padding: 20px; border-radius: 4px; }
        .error { background-color: #f8d7da; border: 1px solid #f5c6cb; color: #721c24; padding: 20px; border-radius: 4px; }
        .links { margin-top: 20px; text-align: center; }
        .links a { margin: 0 10px; color: #007bff; text-decoration: none; padding: 10px 20px; background: #e9ecef; border-radius: 4px; }
    </style>
</head>
<body>
    <% if ((Boolean) request.getAttribute("success")) { %>
        <div class="success">
            <h2>✓ Connexion réussie!</h2>
            <p><%= request.getAttribute("message") %></p>
        </div>
        <div class="links">
            <a href="profil">Mon Profil</a>
            <a href="logout">Se déconnecter</a>
        </div>
    <% } else { %>
        <div class="error">
            <h2>✗ Échec de connexion</h2>
            <p><%= request.getAttribute("message") %></p>
        </div>
        <div class="links">
            <a href="login">Réessayer</a>
        </div>
    <% } %>
</body>
</html>
