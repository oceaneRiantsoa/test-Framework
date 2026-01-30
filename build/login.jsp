<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Connexion</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 400px; margin: 50px auto; padding: 20px; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; }
        input[type="text"], input[type="password"] {
            width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box;
        }
        button { background-color: #4CAF50; color: white; padding: 12px 20px; border: none; border-radius: 4px; cursor: pointer; width: 100%; font-size: 16px; }
        button:hover { background-color: #45a049; }
        .message { padding: 10px; background-color: #d4edda; border-radius: 4px; margin-bottom: 15px; }
        .links { margin-top: 20px; text-align: center; }
        .links a { margin: 0 10px; color: #007bff; }
    </style>
</head>
<body>
    <h1>🔐 Connexion</h1>
    
    <% if ((Boolean) request.getAttribute("isLoggedIn") != null && (Boolean) request.getAttribute("isLoggedIn")) { %>
        <div class="message">
            <%= request.getAttribute("message") %>
        </div>
        <div class="links">
            <a href="profil">Mon Profil</a> | <a href="logout">Se déconnecter</a>
        </div>
    <% } else { %>
        <form action="login" method="POST">
            <div class="form-group">
                <label>Nom d'utilisateur:</label>
                <input type="text" name="username" required placeholder="Votre nom">
            </div>
            <div class="form-group">
                <label>Mot de passe:</label>
                <input type="password" name="password" required placeholder="Mot de passe (1234)">
            </div>
            <button type="submit">Se connecter</button>
        </form>
        <p style="text-align: center; color: #666; margin-top: 15px;">
            <small>Mot de passe de test: 1234</small>
        </p>
    <% } %>
</body>
</html>
