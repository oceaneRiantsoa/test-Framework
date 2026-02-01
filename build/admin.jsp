<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 700px; margin: 50px auto; padding: 20px; }
        .admin { background-color: #f8d7da; border: 1px solid #f5c6cb; color: #721c24; padding: 20px; border-radius: 4px; }
        .stats { margin: 15px 0; padding: 15px; background: white; border-left: 3px solid #dc3545; }
        .menu { margin-top: 20px; padding: 15px; background: #f8f9fa; border-radius: 4px; }
        .menu a { margin: 5px 10px; padding: 8px 15px; background: #007bff; color: white; text-decoration: none; border-radius: 3px; }
    </style>
</head>
<body>
    <div class="admin">
        <h1>⚙️ Administration</h1>
        <p><%= request.getAttribute("message") %></p>
        <p><strong>Connecté en tant que :</strong> <%= request.getAttribute("username") %> (Administrateur)</p>
        
        <div class="stats">
            <h3>Statistiques :</h3>
            <p><%= request.getAttribute("stats") %></p>
        </div>
    </div>

    <div class="menu">
        <h3>Navigation :</h3>
        <a href="public">Page publique</a>
        <a href="private">Page privée</a>
        <a href="chef/reports">Rapports Chef</a>
        <a href="logout">Se déconnecter</a>
    </div>
</body>
</html>