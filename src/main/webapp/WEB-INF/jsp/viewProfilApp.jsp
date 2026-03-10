<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="content-body">
    <div class="container-header">
        <a href="/profil-app">
            <div class="back-button">
                <img src="/images/arrow-left.svg" alt="arrow-left">
                <span>Retour</span>
            </div>
        </a>
    </div>

    <div class="section">
        <div class="card-container">
            <div class="application-header">
                <div class="icon-container icon-purple-bg">
                    <img src="/images/layers.svg" alt="layers icon" class="icon-purple">
                </div>
                
                <div>
                    <p class="application-name">${profilApp.application.name}</p>
                    <div class="application-details">
                        <p>${profilApp.application.direction.name}</p>
                        <p>-</p>
                        <c:if test="${not empty profilApp.application.description}">
                            <p class="application-description">${profilApp.application.description}</p>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="section">
        <div class="card-container">
            <div class="card-title">Profils SI associés</div>
            <div class="profilSI-list">
                <c:forEach var="profilSILinked" items="${profilApp.profilSI}">
                    <div class="ressource-item">
                        <span>n°${profilSILinked.profilSI.id} ${profilSILinked.profilSI.name}</span>
                    </div>
                </c:forEach>
                <c:if test="${empty profilApp.profilSI}">
                    <p class="empty-message">Aucun profil SI associe</p>
                </c:if>
            </div>
        </div>
    </div>

    <div class="section">
        <div class="card-container">
            <div class="card-title">Ressources associées</div>
            <div class="ressources-list">
                <c:forEach var="ressourceLinked" items="${profilApp.profilAppRessources}">
                    <div class="ressource-item">
                        <span class="ressource-name">${ressourceLinked.ressource.name}</span>
                        <c:if test="${not empty ressourceLinked.ressource.description}">
                            <span class="ressource-description"> - ${ressourceLinked.ressource.description}</span>
                        </c:if>
                    </div>
                </c:forEach>
                <c:if test="${empty profilApp.profilAppRessources}">
                    <p class="empty-message">Aucune ressource associee</p>
                </c:if>
            </div>
        </div>
    </div>
</div>