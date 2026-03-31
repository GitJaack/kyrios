<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="content-body" x-data="profilAppForm()" @submit.prevent="submitForm()">
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
            <a href="/profil-app">
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
        <form id="createProfilForm" action="/profil-app/create" method="post" @submit.prevent="submitForm()">
            
            <div class="form-section">
                <h3>Informations générales</h3>
                <div class="form-group">
                    <label for="profilapp">Nom du profil applicatif <span class="required">*</span></label>
                    <input type="text" id="profilapp" name="profilapp" required placeholder="Entrer un nom de profil applicatif">
                </div>

                <div class="form-group">
                    <label for="application">Application associée <span class="required">*</span></label>
                    <select id="application" name="application" x-model="selectedApplicationId" @change="loadResources()" required>
                        <option value="" disabled selected>Sélectionner une application</option>
                        <c:forEach var="app" items="${apps}">
                            <option value="${app.id}">${app.name}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="form-section">
                <h3>Profil SI associés</h3>
                
                <div class="ressources-grid">
                    <c:forEach var="profilSI" items="${profilsSI}">
                        <label class="ressource-item">
                            <div class="ressource-checkbox">
                                <input type="checkbox" id="profilSI${profilSI.id}" name="profilSI" value="${profilSI.id}">
                                <label for="profilSI${profilSI.id}">${profilSI.name}</label>
                            </div>
                        </label>
                    </c:forEach>
                </div>

            </div>

            <div class="form-section">
                <h3>Ressources de l'application</h3>
                
                <div x-show="selectedApplicationId" style="display:none;">
                    <div x-show="loadingResources" class="loading-message" style="display: none;">
                        <p>Chargement des ressources...</p>
                    </div>
                    
                    <div x-show="!loadingResources && groupedResources.length > 0" class="ressources-grid">
                        <template x-for="group in groupedResources" :key="group.key">
                            <div class="ressource-group">
                                <div class="ressource-category" x-text="group.label"></div>
                                <template x-for="resource in group.resources" :key="resource.id">
                                    <label class="ressource-item" :for="'resource' + resource.id">
                                        <div class="ressource-checkbox">
                                            <input type="checkbox" :id="'resource' + resource.id" name="ressources" :value="resource.id">
                                            <span x-text="resource.name"></span>
                                            <span class="resource-description" x-show="resource.description" x-text="' - ' + resource.description"></span>
                                        </div>
                                    </label>
                                </template>
                            </div>
                        </template>
                    </div>
                    
                    <div x-show="!loadingResources && groupedResources.length === 0" class="empty-message" style="display: none;">
                        <p>Aucune ressource disponible pour cette application</p>
                    </div>
                </div>
                
                <div x-show="!selectedApplicationId" class="placeholder-message">
                    <p>Sélectionnez une application pour voir ses ressources</p>
                </div>
                
            </div>

        </form>

    </div>

</div>
      

<script>
function profilAppForm() {
    return {
        loading: false,
        message: '',
        messageType: '',
        selectedApplicationId: '',
        resources: [],
        groupedResources: [],
        loadingResources: false,

        buildGroupedResources(resources) {
            const grouped = [];
            let currentGroup = null;
            resources.forEach(resource => {
                const category = resource.category ? resource.category : 'Sans categorie';
                if (!currentGroup || currentGroup.label !== category) {
                    currentGroup = {
                        key: `cat-${category}`,
                        label: category,
                        resources: []
                    };
                    grouped.push(currentGroup);
                }
                currentGroup.resources.push(resource);
            });
            return grouped;
        },
        
        async loadResources() {
            if (!this.selectedApplicationId) {
                this.resources = [];
                this.groupedResources = [];
                return;
            }
            
            this.loadingResources = true;
            try {
                const response = await fetch('/api/ressources-app/ressource-application/?id=' + this.selectedApplicationId);
                if (!response.ok) {
                    throw new Error('Erreur lors du chargement des ressources');
                }
                this.resources = await response.json();
                this.groupedResources = this.buildGroupedResources(this.resources);
            } catch (error) {
                console.error('Erreur:', error);
                this.message = 'Erreur lors du chargement des ressources: ' + error.message;
                this.messageType = 'error';
                this.resources = [];
                this.groupedResources = [];
            } finally {
                this.loadingResources = false;
            }
        },
        
        async submitForm() {
            this.loading = true;
            this.message = '';
            this.messageType = '';

            try {
                const profilAppName = document.getElementById('profilapp').value;

                const selectedApplicationId = document.getElementById('application').value;

                const selectedProfilSIIds = Array.from(document.querySelectorAll('input[name="profilSI"]:checked')).map(cb => parseInt(cb.value));

                const selectedRessourceIds = Array.from(document.querySelectorAll('input[name="ressources"]:checked')).map(cb => parseInt(cb.value));
                
                if(!profilAppName.trim()) {
                    this.messageType = 'error';
                    this.message = 'Le nom du profil applicatif ne peut pas être vide';
                    this.loading = false;
                    return;
                }

                if(!selectedApplicationId) {
                    this.messageType = 'error';
                    this.message = 'Veuillez sélectionner une application';
                    this.loading = false;
                    return;
                }

                if(selectedProfilSIIds.length === 0) {
                    this.messageType = 'error';
                    this.message = 'Veuillez sélectionner au moins un profil SI associé';
                    this.loading = false;
                    return;
                }

                const data ={
                    name: profilAppName,
                    applicationId: parseInt(selectedApplicationId),
                    profilSIIds: selectedProfilSIIds,
                    ressourceAppIds: selectedRessourceIds
                }

                const response = await fetch('/api/profil-app', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(data)
                });
                
                if (response.ok) {
                    const newProfilApp = await response.json();

                    this.messageType = 'success';
                    this.message = 'Profil applicatif créé avec succès !';

                    setTimeout(() => {
                        window.location.href = '/profil-app';   
                    }, 1500);


                }else{
                    const errorData = await response.json();
                    throw new Error(errorData.message || 'Erreur lors de la création du profil applicatif');
                }
                
            } catch (error) {
                console.error('Erreur:', error);
                this.messageType = 'error';
                this.message = error.message || 'Erreur lors de la création du profil applicatif';
            } finally {
                this.loading = false;
            }
        }
    }
}
</script>                  