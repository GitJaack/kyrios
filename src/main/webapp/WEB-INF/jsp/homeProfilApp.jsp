<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<link href="/css/deleteModal.css" rel="stylesheet">

<div class="content-body" x-data="profilAppManager()">
     <div class="content-above">
        <div class="container-above-left">
            <div class="search-container">
                <img src="/images/search.svg" alt="search icon" class="icon">
                <input type="text" placeholder="Rechercher un profil applicatif..." class="search-input">
            </div>
        </div>

        <div class="container-above-right">
            <a href="/profil-app/create" class="create-button">
                <img src="/images/plus.svg" alt="plus icon">
                Créer un profil applicatif
            </a>
        </div>
    </div>

    <div class="nav-section">
        <c:forEach var="app" items="${apps}">
            <button 
                class="nav-button" 
                :class="{ 'active': activeTab === ${app.id} }"
                @click="activeTab = ${app.id}">
                <p>${app.name}</p>
            </button>
        </c:forEach>
    </div>

    <div class="tab-content">
        <c:forEach var="app" items="${apps}">
            <div x-show="activeTab === ${app.id}" x-transition>
                <div class="app-info">
                    <p style="font-weight: 600;">${app.direction.name}</p>
                    <p>${app.description}</p>
                </div>

                <div class="profils-grid">
                    <c:forEach var="profilApp" items="${profilApps}">
                        <c:if test="${profilApp.application.id == app.id}">
                            <div class="profil-card">
                                <div style="margin-bottom:0.75rem;">
                                    <p style="font-weight: 600; font-size: var(--font-size-base);">${profilApp.name}</p>
                                </div>

                                <div class="profils-linked-section">
                                    <p style="font-size: var(--font-size-sm); color: hsl(220, 10%, 50%);">Profils SI associés</p>
                                    <div class="profils-linked-container">
                                        <c:forEach var="profilSI" items="${profilApp.profilSI}">
                                            <div class="profils-linked">
                                                <p>${profilSI.profilSI.name}</p>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                                <div class="card-footer">
                                    <p style="font-size: var(--font-size-sm); color: hsl(220, 10%, 50%); margin-bottom: 0.5rem;">Dernière modification : ${dateUpdatedById[profilApp.id]}</p>
                                    <div class="profil-actions">
                                        <a href="/profil-app/view/${profilApp.id}" class="view-button">
                                            <img src="/images/eye.svg" alt="eye icon">
                                            <span>Voir</span>
                                        </a>
                                        <a href="/profil-app/edit/${profilApp.id}" class="edit-button">
                                            <img src="/images/square-pen.svg" alt="square-pen icon">
                                            <span>Modifier</span>
                                        </a>
                                        <button class="delete-button" type="button" @click="showDeleteModal(${profilApp.id}, '${profilApp.name}')">
                                            <img src="/images/trash-2.svg" alt="trash-2 icon">
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                    </c:forEach>
                </div>
            </div>
        </c:forEach>
    </div>

    <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 1rem;">
        <p style="color: hsl(220, 10%, 50%); font-size: 0.875rem;">
            Total : ${totalItems} profils applicatifs
        </p>

        <c:if test="${totalPages > 1}">
            <div class="pagination" style="margin-top: 0; padding-bottom: 0;">
                <div class="pagination-container">
                    <c:if test="${currentPageNumber > 0}">
                        <a href="/profil-app?page=${currentPageNumber - 1}" class="pagination-link">&laquo;</a>
                    </c:if>
                    <c:if test="${currentPageNumber == 0}">
                        <span class="pagination-link disabled">&laquo;</span>
                    </c:if>

                    <c:forEach begin="0" end="${totalPages - 1}" var="i">
                        <a href="/profil-app?page=${i}" class="pagination-link ${currentPageNumber == i ? 'active' : ''}">${i + 1}</a>
                    </c:forEach>

                    <c:if test="${currentPageNumber < totalPages - 1}">
                        <a href="/profil-app?page=${currentPageNumber + 1}" class="pagination-link">&raquo;</a>
                    </c:if>
                    <c:if test="${currentPageNumber == totalPages - 1}">
                            <span class="pagination-link disabled">&raquo;</span>
                    </c:if>
                </div>
            </div>
        </c:if>
    </div>

    <jsp:include page="deleteProfilApp.jsp" />

</div>