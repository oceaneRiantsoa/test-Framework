<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Mon Profil</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 500px; margin: 50px auto; padding: 20px; }
        .profile-card { background-color: #f8f9fa; border: 1px solid #dee2e6; padding: 20px; border-radius: 8px; }
        .profile-row { margin: 15px 0; padding: 10px; background: white; border-left: 3px solid #4CAF50; }
        .label { font-weight: bold; color: #555; }
        .value { margin-left: 10px; }
        .not-logged { background-color: #f8d7da; padding: 20px; border-radius: 4px; text-align: center; }
        .links { margin-top: 20px; text-align: center; }
        .links a { margin: 0 10px; color: #007bff; text-decoration: none; padding: 10px 20px; background: #e9ecef; border-radius: 4px; }
    </style>
</head>
<body>
    <h1>👤 Mon Profil</h1>
    
    <% if ((Boolean) request.getAttribute("isLoggedIn") != null && (Boolean) request.getAttribute("isLoggedIn")) { %>
        <div class="profile-card">
            <h2>Informations de session</h2>
            <div class="profile-row">
                <span class="label">Nom d'utilisateur:</span>
                <span class="value"><%= request.getAttribute("username") %></span>
            </div>
            <div class="profile-row">
                <span class="label">Rôle:</span>
                <span class="value"><%= request.getAttribute("role") %></span>
            </div>
            <div class="profile-row">
                <span class="label">Connecté depuis:</span>
                <span class="value">
                    <% 
                        Long loginTime = (Long) request.getAttribute("loginTime");
                        if (loginTime != null) {
                            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            out.print(sdf.format(new java.util.Date(loginTime)));
                        }
                    %>
                </span>
            </div>
        </div>
        <div class="links">
            <a href="logout">Se déconnecter</a>
        </div>
    <% } else { %>
        <div class="not-logged">
            <h2>⚠️ Non connecté</h2>
            <p><%= request.getAttribute("message") %></p>
        </div>
        <div class="links">
            <a href="login">Se connecter</a>
        </div>
    <% } %>
</body>
</html>
