<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Rapports Chef</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 700px; margin: 50px auto; padding: 20px; }
        .chef { background-color: #d4edda; border: 1px solid #c3e6cb; color: #155724; padding: 20px; border-radius: 4px; }
        .reports { margin: 15px 0; padding: 15px; background: white; border-left: 3px solid #28a745; }
        .menu { margin-top: 20px; padding: 15px; background: #f8f9fa; border-radius: 4px; }
        .menu a { margin: 5px 10px; padding: 8px 15px; background: #007bff; color: white; text-decoration: none; border-radius: 3px; }
        ul { list-style: none; padding: 0; }
        li { margin: 5px 0; padding: 8px; background: #f8f9fa; border-radius: 3px; }
    </style>
</head>
<body>
    <div class="chef">
        <h1>📊 Rapports Chef d'Équipe</h1>
        <p><%= request.getAttribute("message") %></p>
        <p><strong>Connecté en tant que :</strong> <%= request.getAttribute("username") %> (Chef)</p>
        
        <div class="reports">
            <h3>Rapports disponibles :</h3>
            <ul>
                <% 
                    String[] reports = (String[]) request.getAttribute("reports");
                    if (reports != null) {
                        for (String report : reports) {
                %>
                <li>📋 <%= report %></li>
                <% 
                        }
                    }
                %>
            </ul>
        </div>
    </div>

    <div class="menu">
        <h3>Navigation :</h3>
        <a href="public">Page publique</a>
        <a href="private">Page privée</a>
        <a href="admin/dashboard">Admin Dashboard</a>
        <a href="logout">Se déconnecter</a>
    </div>
</body>
</html>