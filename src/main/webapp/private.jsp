<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Page Privée</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 600px; margin: 50px auto; padding: 20px; }
        .private { background-color: #fff3cd; border: 1px solid #ffeaa7; color: #856404; padding: 20px; border-radius: 4px; }
        .user-info { margin: 15px 0; padding: 10px; background: white; border-left: 3px solid #ffc107; }
        .menu { margin-top: 20px; padding: 15px; background: #f8f9fa; border-radius: 4px; }
        .menu a { margin: 5px 10px; padding: 8px 15px; background: #007bff; color: white; text-decoration: none; border-radius: 3px; }
        .menu a.danger { background: #dc3545; }
    </style>
</head>
<body>
    <div class="private">
        <h1>🔐 Page Privée</h1>
        <p><%= request.getAttribute("message") %></p>
        <div class="user-info">
            <p><strong>Utilisateur :</strong> <%= request.getAttribute("username") %></p>
            <p><strong>Rôle :</strong> <%= request.getAttribute("role") %></p>
        </div>
    </div>

    <div class="menu">
        <h3>Navigation de test :</h3>
        <a href="public">Page publique</a>
        <a href="admin/dashboard" class="danger">Admin Dashboard</a>
        <a href="chef/reports" class="danger">Rapports Chef</a>
        <a href="logout">Se déconnecter</a>
        
        <h4>Changer de rôle (pour test) :</h4>
        <form action="switch-role" method="POST" style="display: inline;">
            <select name="role">
                <option value="user">User</option>
                <option value="admin">Admin</option>
                <option value="chef">Chef</option>
            </select>
            <button type="submit">Changer</button>
        </form>
    </div>
</body>
</html>