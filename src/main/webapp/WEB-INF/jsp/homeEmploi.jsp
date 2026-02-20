<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<link href="/css/deleteModal.css" rel="stylesheet">

<div class = "content-body" x-data="emploiManager()">
    <div class="content-above">
        <div class="container-above-left">
            <div class="search-container">
                <img src="/images/search.svg" alt="search icon" class="icon">
                <input type="text" placeholder="Rechercher un emploi..." class="search-input">
            </div>

            <button class="filter-button">
                <img src="/images/funnel.svg" alt="funnel icon">
            </button>
        </div>

        <div class="container-above-right">
            <a href="/emploi/create" class="create-button">
                <img src="/images/plus.svg" alt="plus icon">
                Créer un emploi
            </a>
        </div>
    </div>

    <div class="emploi-section">
        <c:forEach var="emploi" items="${emplois}">
            <div class="emploi-card">
                <div class="emploi-header">
                    <p style="font-weight:600; font-size:1rem">${emploi.emploiName}</p>
                    <div class="emploi-status-container">
                        ${emploi.status}
                    </div>
                </div>

                <div style="display: flex; flex-direction: column;gap: 0.5rem;">
                    <div class="emploi-info">
                        <img src="/images/building-2.svg" alt="building-2 icon" class="icon-direction">
                        <p>${emploi.direction.name}</p>
                    </div>

                    <div class="emploi-info">
                        <img src="/images/map-pin.svg" alt="map-pin icon" class="icon-service">
                        <c:choose>
                            <c:when test="${not empty emploi.service}">
                                <p>${emploi.service.name}</p>
                            </c:when>

                            <c:otherwise>
                                <p>-</p>
                            </c:otherwise>    
                        </c:choose>
                    </div>

                    <div class="emploi-info">
                        <img src="/images/landmark.svg" alt="landmark icon" class="icon-domaine">
                        <c:choose>
                            <c:when test="${not empty emploi.domaine}">
                                <p>${emploi.domaine.name}</p>
                            </c:when>
                            
                            <c:otherwise>
                                <p>-</p>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="emploi-info">
                        <img src="/images/user.svg" alt="user icon" class="icon-statut">
                        <c:choose>
                            <c:when test="${not empty emploi.profilSI}">
                                 <p>Profil SI : n°${emploi.profilSI.id} ${emploi.profilSI.name}</p>
                            </c:when>
                            
                            <c:otherwise>
                                <p>Profil SI : -</p>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="emploi-info card-footer">
                        <p>Derniere modification : ${dateUpdatedById[emploi.id]}</p>
                    </div>

                    <div class="emploi-actions">
                        <a href="/emploi/edit/${emploi.id}" class="edit-button">
                            <img src="/images/square-pen.svg" alt="square-pen icon">
                            <span>Modifier</span>
                        </a>

                        <button class="delete-button" type="button" @click="showDeleteModal(${emploi.id}, '${emploi.emploiName}')">
                                <img src="/images/trash-2.svg" alt="trash-2 icon">
                        </button>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

    <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 1rem;">
        <p style="color: hsl(220, 10%, 50%); font-size: 0.875rem;">
            Total : ${totalItems} emplois
        </p>

        <c:if test="${totalPages > 1}">
            <div class="pagination" style="margin-top: 0; padding-bottom: 0;">
                <div class="pagination-container">
                    <c:if test="${currentPageNumber > 0}">
                        <a href="/emploi?page=${currentPageNumber - 1}" class="pagination-link">&laquo;</a>
                    </c:if>
                    <c:if test="${currentPageNumber == 0}">
                        <span class="pagination-link disabled">&laquo;</span>
                    </c:if>

                    <c:forEach begin="0" end="${totalPages - 1}" var="i">
                        <a href="/emploi?page=${i}" class="pagination-link ${currentPageNumber == i ? 'active' : ''}">${i + 1}</a>
                    </c:forEach>

                    <c:if test="${currentPageNumber < totalPages - 1}">
                        <a href="/emploi?page=${currentPageNumber + 1}" class="pagination-link">&raquo;</a>
                    </c:if>
                    <c:if test="${currentPageNumber == totalPages - 1}">
                            <span class="pagination-link disabled">&raquo;</span>
                    </c:if>
                </div>
            </div>
        </c:if>
    </div>

    <jsp:include page="deleteEmploi.jsp" />
    
</div>