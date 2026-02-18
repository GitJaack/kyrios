<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<link href="/css/deleteProfilSI.css" rel="stylesheet">

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

        <div class="content-below">
            <div class="table-header">
                <p>Profil SI</p>
                <p>Emploi</p>
                <p>Dernière modification</p>
                <p style="justify-self: end;">Actions</p>
            </div>

            <c:forEach var="profil" items="${profilsSI}">
                <div class="table-row">
                    <p>n°${profil.id} ${profil.name}</p>
                    <p>
                        <c:forEach var="emploi" items="${profil.emplois}" varStatus="status">
                            ${emploi.emploiName}${!status.last ? ', ' : ''}
                        </c:forEach>
                    </p>
                    <p>${DateUpdatedById[profil.id]}</p>

                    <div class="actions">
                        <a href="/profilSI/view/${profil.id}" class="view-button">
                            <img src="/images/eye.svg" alt="eye icon">
                        </a>
                        <a href="/profilSI/edit/${profil.id}" class="edit-button">
                            <img src="/images/square-pen.svg" alt="square-pen icon">
                        </a>
                        <button type="button" class="delete-button" @click="showDeleteModal(${profil.id}, 'n°${profil.id} ${profil.name}')">
                            <img src="/images/trash-2.svg" alt="trash-2 icon">
                        </button>
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