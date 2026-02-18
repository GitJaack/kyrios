<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="content-body" x-data="emploiForm()">
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
            <a href="/emploi">
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
        <form id="createEmploiForm">
            
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
                <h3>Association au profil SI</h3>
                <p class="form-description">Sélectionnez le profil SI à associer à cet emploi</p>
                
                <div class="form-group">
                    <label for="profilSI">Profil SI <span class="required">*</span></label>
                    <select id="profilSI" name="profilSI" required>
                        <option value="" disabled selected>Sélectionner un profil SI</option>
                        <c:forEach var="profilSI" items="${profilsSI}">
                            <option value="${profilSI.id}">${profilSI.name}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>

        </form>
    </div>
</div>

<script>
    function emploiForm(){
        return{
            loading: false,
            message: '',
            messageType: '',

            async submitForm(){
                this.loading = true;
                this.message = '';
                this.messageType = '';

                try{
                    const emploiName = document.getElementById('emploi').value;
                    const direction = document.getElementById('direction').value;
                    const status = document.getElementById('status').value;
                    const service = document.getElementById('service').value || null;
                    const domaine = document.getElementById('domaine').value || null;
                    const profilSI = document.getElementById('profilSI').value;

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

                    if (!profilSI.trim()) {
                        this.messageType = 'error';
                        this.message = 'Le profil SI est requis';
                        this.loading = false;
                        return;
                    }

                    const data = {
                        emploi:{
                            emploi: emploiName,
                            direction: parseInt(direction),
                            status: status,
                            service: service ? parseInt(service) : null,
                            domaine: domaine ? parseInt(domaine) : null,
                        },
                        profilSI: parseInt(profilSI)
                    };

                    const response = await fetch('/api/emplois', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(data)
                    });

                    if(response.ok){
                        const newEmploi = await response.json();
                        this.messageType = 'success';
                        this.message = 'Emploi créé avec succès !';

                        setTimeout(() => {
                            window.location.href = '/emploi';
                        }, 1500);
                    } else {
                        const errorData = await response.json();
                        this.messageType = 'error';
                        this.message = errorData.message || 'Erreur lors de la création de l\'emploi.';
                    }

                } catch(error){
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


