<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kyrios - Application de gestion d'habilitation</title>
    <script defer src="https://cdn.jsdelivr.net/npm/alpinejs@3.x.x/dist/cdn.min.js"></script>
    <style>
        body {
            font-family: system-ui, -apple-system, sans-serif;
            max-width: 1200px;
            margin: 0 auto;
            padding: 2rem;
            background: #f5f5f5;
        }
        .container {
            background: white;
            padding: 2rem;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        h1 {
            color: #333;
            margin-bottom: 1rem;
        }
        .demo-section {
            margin-top: 2rem;
            padding: 1.5rem;
            background: #f9f9f9;
            border-radius: 4px;
        }
        button {
            background: #007bff;
            color: white;
            border: none;
            padding: 0.5rem 1rem;
            border-radius: 4px;
            cursor: pointer;
        }
        button:hover {
            background: #0056b3;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Bienvenue sur Kyrios</h1>
        <p>Application de gestion d'habilitation</p>
        
        <div class="demo-section" x-data="{ count: 0, message: 'Alpine.js fonctionne!' }">
            <h2>Demo Alpine.js</h2>
            <p x-text="message"></p>
            <p>Compteur: <span x-text="count"></span></p>
            <button @click="count++">Incrémenter</button>
            <button @click="count--">Décrémenter</button>
            <button @click="count = 0">Réinitialiser</button>
        </div>
    </div>
</body>
</html>
