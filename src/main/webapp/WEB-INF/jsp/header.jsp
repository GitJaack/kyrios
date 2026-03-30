<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<link href="/css/header.css" rel="stylesheet">
<div class="header">
    <div class="content-header">
        <h1>${pageHeader}</h1>
        <p>${pageDescription}</p>
    </div>

    <c:if test="${pageContext.request.userPrincipal != null}">
        <form action="/logout" method="post" class="logout-form">
            <button type="submit" class="logout-button">
                <img src="/images/log-out.svg" alt="log-out icon" class="log-out icon">
                <span>Déconnexion</span>
            </button>
        </form>
    </c:if>
</div>
