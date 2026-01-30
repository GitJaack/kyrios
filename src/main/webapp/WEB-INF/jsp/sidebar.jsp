<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<div class="sidebar">
    <nav>
        <div class="container-logo">
            <div class="logo">K</div>
            <div>KYRIOS</div>
        </div>
        <div class="nav-main">
            <ul>
                <li><a href="/" class="${currentPage == '/' ? 'active' : ''}">
                <img src="/images/house.svg" alt="house icon" class="icon">
                 Accueil</a></li>
                <li><a href="/profilSI" class="${currentPage == '/profilSI' ? 'active' : ''}">
                <img src="/images/users.svg" alt="users icon" class="icon">
                Gestion des profils SI</a></li>
                <li><a href="/emploi" class="${currentPage == '/emploi' ? 'active' : ''}">
                <img src="/images/briefcase.svg" alt="briefcase icon" class="icon">
                Gestion des emplois</a></li>
                <li><a href="/profilApp" class="${currentPage == '/profilApp' ? 'active' : ''}">
                <img src="/images/layers.svg" alt="layers icon" class="icon">
                Gestion des profils applicatifs</a></li>
                <li><a href="/settings" class="${currentPage == '/settings' ? 'active' : ''}">
                <img src="/images/settings.svg" alt="settings icon" class="icon">
                Configuration</a></li>
            </ul>
        </div>
        <div class="nav-bottom">
            <ul>
                <li><a href="/login">
                <img src="/images/log-in.svg" alt="login icon" class="icon">
                Connexion</a></li>
            </ul>
        </div>
    </nav>
</div>