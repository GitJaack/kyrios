<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="content-body" x-data="profilSIForm()">
    <div
        x-show="message"
        x-transition
        class="form-message"
        :class="messageType"
        >
        <span x-text="message"></span>
    </div>


    <div class="content-above">
        <div class="container-header">
            <a href="/profilSI">
                <div class="back-button">
                    <img src="/images/arrow-left.svg" alt="arrow-left">
                    <span>Annuler</span>
                </div>
            </a>

            <div class="content-header-right">
                <button class="save-button" type="submit" @click="submitForm()" :disabled="loading">
                    <img src="/images/save.svg" alt="save icon">
                    <span x-text="loading ? 'Enregistrement...' : 'Enregistrer'"></span>
                </button>
            </div>

        </div>
    </div>

    <div class="content-form">
        <form id="createProfilForm" action="/profilSI/create" method="post" @submit.prevent="submitForm()">
            
            <div class="form-section">
                <h3>Informations de l'emploi</h3>
                <div class="form-group">
                    <label for="emploi">Nom de l'emploi <span class="required">*</span></label>
                    <input type="text" id="emploi" name="emploi" required placeholder="Entrer un nom d'emploi">
                </div>

                <div style="display: grid; grid-template-columns: repeat(2, minmax(0, 1fr));">
                    <div class="form-group">
                        <label for="direction">Direction <span class="required">*</span></label>
                        <select id="direction" name="direction" required x-model="selectedDirection" @change="onDirectionChange()">
                            <option value="" disabled selected>Sélectionner une direction</option>
                            <c:forEach var="direction" items="${directions}">
                                <option value="${direction.id}">${direction.name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="status">Statut <span class="required">*</span></label>
                        <select id="status" name="status" required>
                            <option value="" disabled selected>Sélectionner un statut</option>
                            <c:forEach var="status" items="${statusOptions}">
                                <option value="${status}">
                                    <c:choose>
                                        <c:when test="${status == 'PERMANENT'}">Permanent</c:when>
                                        <c:when test="${status == 'STAGIAIRE'}">Stagiaire</c:when>
                                        <c:when test="${status == 'ALTERNANT'}">Alternant</c:when>
                                        <c:when test="${status == 'PRESTATAIRES_EXTERIEURS'}">Prestataires extérieurs</c:when>
                                        <c:when test="${status == 'SAISONNIER'}">Saisonnier</c:when>
                                        <c:otherwise>${status}</c:otherwise>
                                    </c:choose>
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="service">Service</label>
                        <select id="service" name="service">
                            <option value="" disabled selected>Sélectionner un service</option>
                            <c:forEach var="service" items="${services}">
                                <option value="${service.id}">${service.name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="domaine">Domaine</label>
                        <select id="domaine" name="domaine">
                            <option value="" disabled selected>Sélectionner un domaine</option>
                            <c:forEach var="domaine" items="${domaines}">
                                <option value="${domaine.id}">${domaine.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>

            <div class="form-section">
                <h3>Informations du profil SI</h3>
                
                <div class="form-group">
                    <label for="name">Nom du profil SI <span class="required">*</span></label>
                    <input type="text" id="name" name="name" required placeholder="Entrer un nom de profil SI">
                </div>

                <div class="form-group">
                    <label>Mode de création <span class="required">*</span></label>

                    <div class="toggle-switch">
                        <button type="button"
                                class="toggle-option"
                                :class="{ 'active': modeCreation === 'NOUVEAU' }"
                                @click="
                                    modeCreation = 'NOUVEAU';
                                    profilSISourceId = '';
                                    profilsSISource = [];
                                    onDirectionChange();
                                ">
                            Nouveau
                        </button>

                        <button type="button"
                                class="toggle-option"
                                :class="{ 'active': modeCreation === 'COPIER' }"
                                @click="
                                    modeCreation = 'COPIER';
                                    loadProfilsSISource();
                                ">
                            Copier
                        </button>
                    </div>
                </div>
                
                <div class="form-group" x-show="modeCreation === 'COPIER'">
                    <label for="profilSISource">Profil SI source</label>
                    <select id="profilSISource" x-model="profilSISourceId" @change="onProfilSourceChange()">
                        <option value="" disabled selected>Sélectionner un profil SI</option>
                        <template x-for="profil in profilsSISource" :key="profil.idProfilSI">
                            <option :value="profil.idProfilSI" x-text="profil.name"></option>
                        </template>
                    </select>
                </div>

            </div>

            <div class="form-section">
                <h3>Ressources SI</h3>
                <p class="form-description">Sélectionnez les ressources et leur type d'accès</p>

                <c:forEach var="categorie" items="${categories}">
                    <div class="category-section" x-data="{ open: false, resourceIds: [<c:forEach var="ressource" items="${categorie.ressources}" varStatus="resStatus">${ressource.id}<c:if test="${!resStatus.last}">,</c:if></c:forEach>] }">
                        <div class="category-header" @click="open = !open">
                            <h4 class="category-title">${categorie.name}</h4>
                            <div style="display: flex; align-items: center; gap: 8px;">
                                <span class="category-count" x-text="checkedCount(resourceIds) + ' / ' + resourceIds.length"></span>
                                <img x-show="!open" src="/images/chevron-down.svg" alt="chevron down icon">
                                </img>
                                <img x-show="open" src="/images/chevron-up.svg" alt="chevron up icon">
                                </img>
                            </div>
                        </div>
                        
                        <div class="category-content" x-show="open" x-collapse>
                            <c:forEach var="ressource" items="${categorie.ressources}">
                                <div class="ressource-item">
                                    <div class="ressource-checkbox">
                                        <input type="checkbox" 
                                               id="ressource_${ressource.id}" 
                                               value="${ressource.id}"
                                               x-model="selectedRessources[${ressource.id}].checked"
                                               :disabled="selectedRessources[${ressource.id}].isDefault">
                                        <label for="ressource_${ressource.id}">${ressource.name}</label>
                                    </div>
                                    
                                    <select class="type-acces-select"
                                            x-model="selectedRessources[${ressource.id}].typeAcces"
                                            :disabled="!selectedRessources[${ressource.id}].checked">
                                        <option value="LECTURE">Lecture</option>
                                        <option value="LECTURE_ECRITURE">Lecture/Écriture</option>
                                    </select>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </c:forEach>
            </div>

        </form>
    </div>
</div>

<script>
    function profilSIForm() {
    return {
        loading: false,
        message: '',
        messageType: '',
        selectedDirection: '',
        selectedRessources: {},
        modeCreation: 'NOUVEAU',
        profilsSISource: [],
        profilSISourceId: '',

        directionRessourcesMap: {
            <c:forEach var="direction" items="${directions}" varStatus="status">
            ${direction.id}: [
                <c:forEach var="ressource" items="${direction.ressourcesDefault}" varStatus="resStatus">
                {
                    id: ${ressource.id},
                    typeAcces: '${ressource.typeAcces}'
                }<c:if test="${!resStatus.last}">,</c:if>
                </c:forEach>
            ]<c:if test="${!status.last}">,</c:if>
            </c:forEach>
        },

        init() {
            <c:forEach var="categorie" items="${categories}">
                <c:forEach var="ressource" items="${categorie.ressources}">
                this.selectedRessources[${ressource.id}] = {
                    checked: false,
                    typeAcces: 'LECTURE',
                    isDefault: false
                };
                </c:forEach>
            </c:forEach>
        },

        onDirectionChange() {
            if (!this.selectedDirection) return;

            const directionId = parseInt(this.selectedDirection);
            const ressourcesDefault = this.directionRessourcesMap[directionId] || [];

            this.resetRessources();

            if (this.modeCreation === 'NOUVEAU') {
                this.applyDefaultRessources(ressourcesDefault);
            } else if (this.modeCreation === 'COPIER') {
                this.applyCopyModeRessources();
            }

            if (this.modeCreation === 'COPIER') {
                this.loadProfilsSISource();
            }
        },

        resetRessources() {
            Object.keys(this.selectedRessources).forEach(id => {
                this.selectedRessources[id].checked = false;
                this.selectedRessources[id].typeAcces = 'LECTURE';
                this.selectedRessources[id].isDefault = false;
            });
        },

        applyDefaultRessources(ressourcesDefault) {
            ressourcesDefault.forEach(ressource => {
                if (this.selectedRessources[ressource.id]) {
                    this.selectedRessources[ressource.id].checked = true;
                    this.selectedRessources[ressource.id].typeAcces = ressource.typeAcces;
                    this.selectedRessources[ressource.id].isDefault = true;
                }
            });
        },

        checkedCount(resourceIds) {
            return resourceIds.reduce((count, id) => {
                return count + (this.selectedRessources[id] && this.selectedRessources[id].checked ? 1 : 0);
            }, 0);
        },

        onProfilSourceChange() {
            if (this.modeCreation !== 'COPIER') return;
            this.applyCopyModeRessources();
        },

        applyCopyModeRessources() {
            if (!this.selectedDirection) return;

            const directionId = parseInt(this.selectedDirection);
            const ressourcesDefault = this.directionRessourcesMap[directionId] || [];

            this.resetRessources();
            this.applyDefaultRessources(ressourcesDefault);

            if (!this.profilSISourceId) {
                return;
            }

            const profilId = parseInt(this.profilSISourceId);
            const profil = this.profilsSISource.find(item => item.idProfilSI === profilId);
            if (!profil || !profil.ressources) {
                return;
            }

            profil.ressources.forEach(ressource => {
                if (this.selectedRessources[ressource.id]) {
                    this.selectedRessources[ressource.id].checked = true;
                    this.selectedRessources[ressource.id].typeAcces = ressource.typeAcces;
                    if (!this.selectedRessources[ressource.id].isDefault) {
                        this.selectedRessources[ressource.id].isDefault = false;
                    }
                }
            });
        },

        async loadProfilsSISource() {
            if (!this.selectedDirection) {
                this.profilsSISource = [];
                return;
            }

            try {
                const response = await fetch('/api/profils-si?directionId=' + this.selectedDirection);
                if (response.ok) {
                    this.profilsSISource = await response.json();
                    if (this.modeCreation === 'COPIER' && this.profilSISourceId) {
                        this.applyCopyModeRessources();
                    }
                } else {
                    console.error('Erreur lors du chargement des profils SI');
                    this.profilsSISource = [];
                }
            } catch (error) {
                console.error('Erreur de connexion', error);
                this.profilsSISource = [];
            }
        },

        async submitForm() {
            this.loading = true;
            this.message = '';
            this.messageType = '';

            try {
                const emploiName = document.getElementById('emploi').value;
                const direction = document.getElementById('direction').value;
                const service = document.getElementById('service').value;
                const domaine = document.getElementById('domaine').value;
                const status = document.getElementById('status').value;
                const profilSIName = document.getElementById('name').value;

                if (!emploiName.trim()) {
                    this.messageType = 'error';
                    this.message = 'Le nom de l\'emploi ne peut pas être vide';
                    this.loading = false;
                    return;
                }
                if (!direction) {
                    this.messageType = 'error';
                    this.message = 'La direction est requise';
                    this.loading = false;
                    return;
                }
                if (!status) {
                    this.messageType = 'error';
                    this.message = 'Le statut ne peut pas être vide';
                    this.loading = false;
                    return;
                }
                if (!profilSIName.trim()) {
                    this.messageType = 'error';
                    this.message = 'Le nom du profil SI ne peut pas être vide';
                    this.loading = false;
                    return;
                }

                const ressources = [];
                Object.keys(this.selectedRessources).forEach(id => {
                    if (this.selectedRessources[id].checked) {
                        ressources.push({
                            ressourceId: parseInt(id),
                            typeAcces: this.selectedRessources[id].typeAcces
                        });
                    }
                });

                const data = {
                    emploi: {
                        emploi: emploiName,
                        direction: parseInt(direction),
                        service: service ? parseInt(service) : null,
                        domaine: domaine ? parseInt(domaine) : null,
                        status: status
                    },
                    profilSI: {
                        profilSI: profilSIName,
                        modeCreation: this.modeCreation,
                        profilSISourceId: this.profilSISourceId ? parseInt(this.profilSISourceId) : null,
                        ressources: ressources
                    }
                };

                const response = await fetch('/api/profils-si', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(data)
                });

                if (response.ok) {
                    const newProfil = await response.json();

                    if (this.modeCreation === 'COPIER') {
                        await this.loadProfilsSISource();
                    }

                    this.messageType = 'success';
                    this.message = 'Profil SI créé avec succès!';

                    setTimeout(() => {
                        window.location.href = '/profilSI';
                    }, 2000);
                } else {
                    const error = await response.json();
                    this.messageType = 'error';
                    this.message = error.message || 'Erreur lors de la création du profil SI';
                }
            } catch (error) {
                console.error('Erreur:', error);
                this.messageType = 'error';
                this.message = 'Erreur de connexion au serveur';
            } finally {
                this.loading = false;
            }
        }
    }
}

</script>