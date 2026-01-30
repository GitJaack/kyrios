<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<div>
    <div class="header">

        <div class="content-header">
            <h1>Accueil</h1>
            <p>Bienvenue sur Kyrios</p>
        </div>

        <div class="logout-button">
            <img src="/images/log-out.svg" alt="log-out icon" class="log-out icon">
            <p>Déconnexion</p>
        </div>

    </div>

    <div class="content-body">
        <div class="grid-container">
        
            <div class="grid-item">
                <div>
                    <p class="grid-item-title">Profil SI</p>
                    <p class="grid-item-value">${profilSICount}</p>
                </div>
                <div class="icon-container">
                    <img src="/images/users.svg" alt="users icon" class="icon-blue">
                </div>
            </div>

            <div class="grid-item">
                <div>
                    <p class="grid-item-title">Emplois</p>
                    <p class="grid-item-value">${emploiCount}</p>
                </div>
                <div class="icon-container">
                    <img src="/images/briefcase.svg" alt="briefcase icon" class="icon-green">
                </div>
            </div>

            <div class="grid-item">
                <div>
                    <p class="grid-item-title">Profils applicatifs</p>
                    <p class="grid-item-value">${profilAppCount}</p>
                </div>
                <div class="icon-container">
                    <img src="/images/layers.svg" alt="layers icon" class="icon-purple">
                </div>
            </div>

            <div class="grid-item">
                <div>
                    <p class="grid-item-title">Applications</p>
                    <p class="grid-item-value">${applicationCount}</p>
                </div>
                <div class="icon-container">
                    <img src="/images/folder-open.svg" alt="folder icon" class="icon-orange">
                </div>
            </div>

        </div>

        <div class="content-bottom">

            <div class="content-quick">
                <h3>Actions rapides</h3>
                <div>
                    <a href='/profilSI/create'">Créer un profil SI</a>
                    <a href='/emploi/create'">Créer un emploi</a>
                    <a href='/profilApp/create'">Créer un profil applicatif</a>
                </div>
            </div>

            <div class="content-recent-activities">
                <h3>Activités récentes</h3>
            </div>

        </div>

    </div>
</div>