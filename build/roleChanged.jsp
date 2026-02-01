<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Rôle Changé</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 500px; margin: 50px auto; padding: 20px; text-align: center; }
        .success { background-color: #d4edda; border: 1px solid #c3e6cb; color: #155724; padding: 20px; border-radius: 4px; }
        .menu { margin-top: 20px; }
        .menu a { margin: 5px 10px; padding: 10px 20px; background: #007bff; color: white; text-decoration: none; border-radius: 4px; }
    </style>
</head>
<body>
    <div class="success">
        <h2>✓ Rôle modifié</h2>
        <p><strong><%= request.getAttribute("username") %></strong>, votre rôle est maintenant :</p>
        <h3><%= request.getAttribute("newRole") %></h3>
    </div>

    <div class="menu">
        <a href="private">Page privée</a>
        <a href="admin/dashboard">Admin</a>
        <a href="chef/reports">Chef</a>
    </div>
</body>
</html>