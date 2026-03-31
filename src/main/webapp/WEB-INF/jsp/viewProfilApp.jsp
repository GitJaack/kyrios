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
                    <p class="application-name">${profilApp.application}</p>
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
                        <span>n°${profilSILinked.id} ${profilSILinked.name}</span>
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
                <c:set var="currentCategory" value="" />
                <c:forEach var="ressource" items="${profilApp.ressourcesAppDetails}">
                    <c:set var="ressourceCategory" value="${empty ressource.category ? 'Sans categorie' : ressource.category}" />
                    <c:if test="${ressourceCategory ne currentCategory}">
                        <div class="ressource-category">${ressourceCategory}</div>
                        <c:set var="currentCategory" value="${ressourceCategory}" />
                    </c:if>
                    <div class="ressource-item">
                        <span class="ressource-name">${ressource.name}</span>
                        <c:if test="${ressource.name == 'Niveau de permission' && ressource.permissionLevel != null}">
                            <span class="ressource-description"> - Niveau ${ressource.permissionLevel}</span>
                        </c:if>
                        <c:if test="${not empty ressource.description}">
                            <span class="ressource-description"> - ${ressource.description}</span>
                        </c:if>
                    </div>
                </c:forEach>
                <c:if test="${empty profilApp.ressourcesAppDetails}">
                    <p class="empty-message">Aucune ressource associee</p>
                </c:if>
            </div>
        </div>
    </div>
</div>