<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<div class="content-body">
        <div class="grid-container">
        
            <div class="grid-item">
                <div>
                    <p class="grid-item-title">Profil SI</p>
                    <p class="grid-item-value">${profilSICount}</p>
                </div>
                <div class="icon-container icon-blue-bg">
                    <img src="/images/users.svg" alt="users icon" class="icon-blue">
                </div>
            </div>

            <div class="grid-item">
                <div>
                    <p class="grid-item-title">Emplois</p>
                    <p class="grid-item-value">${emploiCount}</p>
                </div>
                <div class="icon-container icon-green-bg">
                    <img src="/images/briefcase.svg" alt="briefcase icon" class="icon-green">
                </div>
            </div>

            <div class="grid-item">
                <div>
                    <p class="grid-item-title">Profils applicatifs</p>
                    <p class="grid-item-value">${profilAppCount}</p>
                </div>
                <div class="icon-container icon-purple-bg">
                    <img src="/images/layers.svg" alt="layers icon" class="icon-purple">
                </div>
            </div>

            <div class="grid-item">
                <div>
                    <p class="grid-item-title">Applications</p>
                    <p class="grid-item-value">${applicationCount}</p>
                </div>
                <div class="icon-container icon-orange-bg">
                    <img src="/images/folder-open.svg" alt="folder icon" class="icon-orange">
                </div>
            </div>

        </div>

        <div class="content-bottom">

            <div class="content-quick">
                <div class="quick-header">
                    <h3>Actions rapides</h3>    
                </div>

                <div class="quick-links">
                    <a href='/profilSI/create'">
                        <div class="quick-link">
                            <div class="icon-container icon-blue-bg">
                                <img src="/images/users.svg" alt="users icon" class="icon-blue">
                            </div>
                            <div>
                                <p class="title">Créer un profil SI</p>
                                <p class="desc">Ajouter un nouveau profil SI</p>
                            </div>
                        </div>
                    </a>

                    <a href='/emploi/create'">
                        <div class="quick-link">
                            <div class="icon-container icon-green-bg">
                                <img src="/images/briefcase.svg" alt="briefcase icon" class="icon-green">
                            </div>
                            <div>
                                <p class="title">Créer un emploi</p>
                                <p class="desc">Ajouter un nouvel emploi</p>
                            </div>
                        </div> 
                    </a>

                    <a href='/profil-app/create'">
                        <div class="quick-link">
                            <div class="icon-container icon-purple-bg">
                                <img src="/images/layers.svg" alt="layers icon" class="icon-purple">
                            </div>
                            <div>
                                <p class="title">Créer un profil applicatif</p>
                                <p class="desc">Ajouter un nouveau profil applicatif</p>
                            </div>
                        </div>
                    </a>
                </div>
            </div>

            <div class="content-recent-activities">
                <h3>Activités récentes</h3>
            </div>

        </div>

    </div>
</div>