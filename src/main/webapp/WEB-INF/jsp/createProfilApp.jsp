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
                    
                    <div x-show="!loadingResources && groupedResources.length > 0">
                        <template x-for="(group, index) in groupedResources" :key="group.key + '-' + index">
                            <div class="category-section" x-data="{ open: false }">
                                <div class="category-header" @click="open = !open">
                                    <h4 class="category-title" x-text="group.label"></h4>
                                    <div style="display: flex; align-items: center; gap: 8px;">
                                        <span class="category-count"
                                              x-text="checkedCount(group.resourceIds) + ' / ' + group.resourceIds.length"></span>
                                        <img x-show="!open" src="/images/chevron-down.svg" alt="chevron down icon">
                                        </img>
                                        <img x-show="open" src="/images/chevron-up.svg" alt="chevron up icon">
                                        </img>
                                    </div>
                                </div>

                                <div class="category-content" x-show="open" x-collapse>
                                    <template x-for="(resource, resourceIndex) in group.resources" :key="String(resource.id) + '-' + index + '-' + resourceIndex">
                                        <label class="ressource-item" :for="'resource' + resource.id">
                                            <div class="ressource-checkbox">
                                                <input type="checkbox" :id="'resource' + resource.id" name="ressources" :value="String(resource.id)" x-model="selectedRessourceIds">
                                                <label :for="'resource' + resource.id" x-text="resource.name"></label>
                                                <span class="resource-description" x-show="resource.description" x-text="' - ' + resource.description"></span>
                                                <template x-if="resource.name === 'Niveau de permission'">
                                                    <select class="type-acces-select"
                                                            x-model="permissionLevel"
                                                            :disabled="!selectedRessourceIds.includes(String(resource.id))"
                                                            @click.stop>
                                                        <option value="0">0</option>
                                                        <option value="1">1</option>
                                                        <option value="2">2</option>
                                                        <option value="3">3</option>
                                                        <option value="4">4</option>
                                                        <option value="5">5</option>
                                                        <option value="6">6</option>
                                                    </select>
                                                </template>
                                            </div>
                                        </label>
                                    </template>
                                </div>
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
        selectedRessourceIds: [],
        permissionLevel: '0',
        loadingResources: false,

        normalizeGroupedResources(groups) {
            return (groups || []).map(group => {
                const label = group.name || group.categoryName || 'Sans categorie';
                const resources = group.resources || [];
                return {
                    key: `cat-${label}`,
                    label,
                    resources,
                    resourceIds: resources.map(resource => String(resource.id))
                };
            });
        },

        checkedCount(resourceIds) {
            return resourceIds.reduce((count, id) => {
                return count + (this.selectedRessourceIds.includes(id) ? 1 : 0);
            }, 0);
        },
        
        async loadResources() {
            if (!this.selectedApplicationId) {
                this.resources = [];
                this.groupedResources = [];
                this.selectedRessourceIds = [];
                return;
            }
            
            this.loadingResources = true;
            try {
                const response = await fetch('/api/ressources-app/by-categorie?applicationId=' + this.selectedApplicationId);
                if (!response.ok) {
                    throw new Error('Erreur lors du chargement des ressources');
                }
                const data = await response.json();
                this.groupedResources = this.normalizeGroupedResources(data);
                this.resources = this.groupedResources.flatMap(group => group.resources || []);
                this.selectedRessourceIds = [];
            } catch (error) {
                console.error('Erreur:', error);
                this.message = 'Erreur lors du chargement des ressources: ' + error.message;
                this.messageType = 'error';
                this.resources = [];
                this.groupedResources = [];
                this.selectedRessourceIds = [];
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

                const selectedRessourceIds = this.selectedRessourceIds.map(id => parseInt(id));
                
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
                    ressourceAppIds: selectedRessourceIds,
                    permissionLevel: this.selectedRessourceIds.some(id => {
                        const res = this.resources.find(r => String(r.id) === id);
                        return res && res.name === 'Niveau de permission';
                    }) ? parseInt(this.permissionLevel) : null
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