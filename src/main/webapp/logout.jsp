<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Déconnexion</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 400px; margin: 50px auto; padding: 20px; text-align: center; }
        .logout-box { background-color: #e2e3e5; padding: 30px; border-radius: 8px; }
        h2 { color: #383d41; }
        .links { margin-top: 20px; }
        .links a { color: #007bff; text-decoration: none; padding: 10px 20px; background: white; border-radius: 4px; }
    </style>
</head>
<body>
    <div class="logout-box">
        <h2>👋 Déconnexion</h2>
        <p><%= request.getAttribute("message") %></p>
    </div>
    <div class="links">
        <a href="login">Se reconnecter</a>
    </div>
</body>
</html>
