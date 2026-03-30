<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Connexion - Kyrios</title>
    <link href="/css/base.css" rel="stylesheet">
    <link href="/css/login.css" rel="stylesheet">
</head>
<body class="login-page">
    <main class="login-main">
        <section class="login-card">
            <div class="login-header">
                <h1>Connexion</h1>
            </div>

            <c:if test="${param.error != null}">
                <p class="login-message error">Identifiants invalides.</p>
            </c:if>

            <c:if test="${param.logout != null}">
                <p class="login-message success">Vous etes deconnecte.</p>
            </c:if>

            <form method="post" action="/login" class="login-form">
                <label for="username">Nom d'utilisateur</label>
                <input type="text" id="username" name="username" required autocomplete="username">

                <label for="password">Mot de passe</label>
                <input type="password" id="password" name="password" required autocomplete="current-password">

                <button type="submit">Se connecter</button>
            </form>

            <div class="login-footer">
                <a href="/">Accedez a Kyrios sans compte.</a>
            </div>
        </section>
    </main>
</body>
</html>
