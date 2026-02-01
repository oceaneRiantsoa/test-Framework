<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Page Publique</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 600px; margin: 50px auto; padding: 20px; }
        .public { background-color: #d1ecf1; border: 1px solid #bee5eb; color: #0c5460; padding: 20px; border-radius: 4px; }
        .menu { margin-top: 20px; padding: 15px; background: #f8f9fa; border-radius: 4px; }
        .menu a { margin: 5px 10px; padding: 8px 15px; background: #007bff; color: white; text-decoration: none; border-radius: 3px; }
        .menu a.danger { background: #dc3545; }
        .menu a.warning { background: #ffc107; color: #212529; }
    </style>
</head>
<body>
    <div class="public">
        <h1>🌍 Page Publique</h1>
        <p><%= request.getAttribute("message") %></p>
        <p><strong>Cette page est accessible sans connexion.</strong></p>
    </div>

    <div class="menu">
        <h3>Navigation de test :</h3>
        <a href="login">Se connecter</a>
        <a href="private" class="warning">Page privée</a>
        <a href="admin/dashboard" class="danger">Admin</a>
        <a href="chef/reports" class="danger">Chef</a>
    </div>
</body>
</html>