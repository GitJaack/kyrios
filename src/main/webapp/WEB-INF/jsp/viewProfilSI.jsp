<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="content-body">
    <div class="container-header">
        <a href="/profilSI">
            <div class="back-button">
                <img src="/images/arrow-left.svg" alt="arrow-left">
                <span>Retour</span>
            </div>
        </a>
    </div>

    <div class="emplois-section">
        <div class="emplois-header">
            <h2>Emplois associes</h2>
            <p class="emplois-subtitle">Details par emploi</p>
        </div>

        <c:choose>
            <c:when test="${not empty profilSI.emplois}">
                <div class="emplois-grid">
                    <c:forEach var="emploi" items="${profilSI.emplois}">
                        <div class="emploi-card">
                            <div class="emploi-title">${emploi.emploiName}</div>

                            <div class="emploi-row">
                                <span class="emploi-label">Direction</span>
                                <span class="emploi-value">${emploi.direction.name}</span>
                            </div>

                            <div class="emploi-row">
                                <span class="emploi-label">Statut</span>
                                <span class="emploi-value">${emploi.status}</span>
                            </div>

                            <div class="emploi-row">
                                <span class="emploi-label">Domaine</span>
                                <c:choose>
                                    <c:when test="${not empty emploi.domaine}">
                                        <span class="emploi-value">${emploi.domaine.name}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="emploi-value">-</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="emploi-row">
                                <span class="emploi-label">Service</span>
                                <c:choose>
                                    <c:when test="${not empty emploi.service}">
                                        <span class="emploi-value">${emploi.service.name}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="emploi-value">-</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <p class="emplois-empty">Aucun emploi associe a ce profil SI.</p>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="ressources-section">
        <div class="ressources-header">
            <h2>Ressources</h2>
            <p class="ressources-subtitle">Par categorie</p>
        </div>

        <c:choose>
            <c:when test="${not empty ressourcesByCategorie}">
                <div class="ressources-grid">
                    <c:forEach var="entry" items="${ressourcesByCategorie}">
                        <div class="ressource-card">
                            <div class="ressource-title">${entry.key}</div>
                            <ul class="ressource-list">
                                <c:forEach var="profilRessource" items="${entry.value}">
                                    <li class="ressource-item">
                                        <span class="ressource-name">${profilRessource.ressource.name}</span>
                                        <span class="ressource-acces">${profilRessource.typeAcces}</span>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <p class="ressources-empty">Aucune ressource associee a ce profil SI.</p>
            </c:otherwise>
        </c:choose>
    </div>

</div>
