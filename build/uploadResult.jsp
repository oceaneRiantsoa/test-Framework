<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Résultat de l'upload</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 800px;
            margin: 50px auto;
            padding: 20px;
        }
        .success {
            background-color: #d4edda;
            border: 1px solid #c3e6cb;
            color: #155724;
            padding: 15px;
            border-radius: 4px;
            margin-bottom: 20px;
        }
        .info-section {
            background-color: #f8f9fa;
            border: 1px solid #dee2e6;
            padding: 15px;
            border-radius: 4px;
            margin-bottom: 15px;
        }
        h2 {
            color: #333;
            border-bottom: 2px solid #4CAF50;
            padding-bottom: 10px;
        }
        .info-row {
            margin: 10px 0;
            padding: 8px;
            background-color: white;
            border-left: 3px solid #4CAF50;
            padding-left: 10px;
        }
        .label {
            font-weight: bold;
            color: #555;
        }
        .value {
            color: #000;
            margin-left: 10px;
        }
        a {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 4px;
        }
        a:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <div class="success">
        <h1>✓ Upload réussi!</h1>
    </div>

    <div class="info-section">
        <h2>Fichiers uploadés</h2>
        <% 
            Map<String, com.itu.demo.FileUpload> filesMap = (Map<String, com.itu.demo.FileUpload>) request.getAttribute("uploadedFiles");
            if (filesMap != null && !filesMap.isEmpty()) {
                int fileCount = 0;
                for (Map.Entry<String, com.itu.demo.FileUpload> entry : filesMap.entrySet()) {
                    fileCount++;
                    com.itu.demo.FileUpload file = entry.getValue();
        %>
        <div style="background-color: white; padding: 10px; margin: 10px 0; border-left: 3px solid #4CAF50;">
            <h3 style="margin: 0 0 10px 0; color: #4CAF50;">Fichier #<%= fileCount %> (champ: <%= entry.getKey() %>)</h3>
            <div class="info-row">
                <span class="label">Nom:</span>
                <span class="value"><%= file.getFileName() %></span>
            </div>
            <div class="info-row">
                <span class="label">Taille:</span>
                <span class="value"><%= file.getSize() %> octets (<%= String.format("%.2f", file.getSize() / 1024.0) %> KB)</span>
            </div>
            <div class="info-row">
                <span class="label">Type:</span>
                <span class="value"><%= file.getContentType() %></span>
            </div>
        </div>
        <% 
                }
            } else {
        %>
        <p>Aucun fichier uploadé</p>
        <% } %>
    </div>

    <div class="info-section">
        <h2>Données du formulaire</h2>
        <% 
            Map<String, Object> formData = (Map<String, Object>) request.getAttribute("formData");
            if (formData != null) {
                for (Map.Entry<String, Object> entry : formData.entrySet()) {
        %>
        <div class="info-row">
            <span class="label"><%= entry.getKey() %>:</span>
            <span class="value"><%= entry.getValue() %></span>
        </div>
        <% 
                }
            }
        %>
    </div>

    <a href="uploadForm.html">← Retour au formulaire</a>
</body>
</html>
