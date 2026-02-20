<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<link href="/css/deleteModal.css" rel="stylesheet">

<div class="content-body" x-data="profilSIManager()">
        <div class="content-above">
            <div class="container-above-left">
                <div class="search-container">
                    <img src="/images/search.svg" alt="search icon" class="icon">
                    <input type="text" placeholder="Rechercher un profil SI..." class="search-input">
                </div>

                <button class="filter-button">
                    <img src="/images/funnel.svg" alt="funnel icon">
                </button>
            </div>

            <div class="container-above-right">
                <button class="export-button">
                    <img src="/images/download.svg" alt="download icon">
                    Exporter
                </button>

                <a href="/profilSI/create" class="create-button">
                    <img src="/images/plus.svg" alt="plus icon">
                    Créer un profil
                </a>
            </div>
        </div>

        <div class="profils-grid">
            <c:forEach var="profil" items="${profilsSI}">
                <div class="profil-card">
                    <div style="margin-bottom: 0.75rem;">
                        <p style="font-weight: 600; font-size: var(--font-size-base);">n°${profil.id} ${profil.name}</p>
                    </div>

                    <div class="profils-linked-section">
                        <p style="font-size: var(--font-size-sm); color: hsl(220, 10%, 50%);">Emplois associés</p>
                        <div class="profils-linked-container">
                            <c:forEach var="emploi" items="${profil.emplois}">
                                <div class="profils-linked">
                                    <p>${emploi.emploiName}</p>
                                </div>
                            </c:forEach>
                        </div>
                    </div>

                    <div class="card-footer">
                        <p style="font-size: var(--font-size-sm); color: hsl(220, 10%, 50%); margin-bottom: 0.5rem;">Dernière modification : ${DateUpdatedById[profil.id]}</p>
                        <div class="profil-actions">
                            <a href="/profilSI/view/${profil.id}" class="view-button">
                                <img src="/images/eye.svg" alt="eye icon">
                                <span>Voir</span>
                            </a>
                            <a href="/profilSI/edit/${profil.id}" class="edit-button">
                                <img src="/images/square-pen.svg" alt="square-pen icon">
                                <span>Modifier</span>
                            </a>
                            <button type="button" class="delete-button" @click="showDeleteModal(${profil.id}, 'n°${profil.id} ${profil.name}')">
                                <img src="/images/trash-2.svg" alt="trash-2 icon">
                            </button>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 1rem;">
            <p style="color: hsl(220, 10%, 50%); font-size: 0.875rem;">
                Total : ${totalItems} profils
            </p>

            <c:if test="${totalPages > 1}">
                <div class="pagination" style="margin-top: 0; padding-bottom: 0;">
                    <div class="pagination-container">
                        <c:if test="${currentPageNumber > 0}">
                            <a href="/profilSI?page=${currentPageNumber - 1}" class="pagination-link">&laquo;</a>
                        </c:if>
                        <c:if test="${currentPageNumber == 0}">
                            <span class="pagination-link disabled">&laquo;</span>
                        </c:if>

                        <c:forEach begin="0" end="${totalPages - 1}" var="i">
                            <a href="/profilSI?page=${i}" class="pagination-link ${currentPageNumber == i ? 'active' : ''}">${i + 1}</a>
                        </c:forEach>

                        <c:if test="${currentPageNumber < totalPages - 1}">
                            <a href="/profilSI?page=${currentPageNumber + 1}" class="pagination-link">&raquo;</a>
                        </c:if>
                        <c:if test="${currentPageNumber == totalPages - 1}">
                             <span class="pagination-link disabled">&raquo;</span>
                        </c:if>
                    </div>
                </div>
            </c:if>
        </div>

        <jsp:include page="deleteProfilSI.jsp" />

    </div>