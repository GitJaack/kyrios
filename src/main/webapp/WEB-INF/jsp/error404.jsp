<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="content-body error-page">
    <div class="error-card">
        <p class="error-code">404</p>
        <h2>Page non trouvee</h2>
        <p>
            La page demandee n'existe pas ou a ete deplacee.
            <c:if test="${not empty requestedPath}">
                <br>
                Chemin demande: <strong>${requestedPath}</strong>
            </c:if>
        </p>

        <div class="error-actions">
            <a href="/" class="btn-primary">Retour a l'accueil</a>
        </div>
    </div>
</div>
