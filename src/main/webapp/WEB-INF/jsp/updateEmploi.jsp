<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class = "content-body" x-data="emploiUpdateForm()">
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
        <form id="updateEmploiForm" action="#" method="post" @submit.prevent="submitForm()">
            <div class="form-section">
                <h3>Informations de l'emploi</h3>
                <div class="form-group">
                    <label for="emploi">Nom de l'emploi <span class="required">*</span></label>
                    <input type="text" id="emploi" name="emploi" required placeholder="Entrer un nom d'emploi" x-model="emploiName">
                </div>

                <div style="display: grid; grid-template-columns: repeat(2, minmax(0, 1fr));">
                    <div class="form-group">
                        <label for="direction">Direction <span class="required">*</span></label>
                        <select id="direction" name="direction" required x-model="selectedDirection">
                            <option value="" disabled>Sélectionner une direction</option>
                            <c:forEach var="direction" items="${directions}">
                                <option value="${direction.id}">${direction.name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="status">Statut <span class="required">*</span></label>
                        <select id="status" name="status" required x-model="selectedStatus">
                            <option value="" disabled>Sélectionner un statut</option>
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
                        <select id="service" name="service" x-model="selectedService">
                            <option value="" disabled>Sélectionner un service</option>
                            <c:forEach var="service" items="${services}">
                                <option value="${service.id}">${service.name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="domaine">Domaine</label>
                        <select id="domaine" name="domaine" x-model="selectedDomaine">
                            <option value="" disabled>Sélectionner un domaine</option>
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
                    <select id="profilSI" name="profilSI" required x-model="selectedProfilSI">
                        <option value="" disabled>Sélectionner un profil SI</option>
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
    function emploiUpdateForm(){
        return {
            emploiId: ${emploiId},
            loading: false,
            message: '',
            messageType: '',
            emploiName: '${emploi.emploiName}',
            selectedDirection: ${emploi.direction != null ? emploi.direction.id : "''"},
            selectedStatus: '${emploi.status}',
            selectedService: ${emploi.service != null ? emploi.service.id : "''"},
            selectedDomaine: ${emploi.domaine != null ? emploi.domaine.id : "''"},
            selectedProfilSI: ${emploi.profilSI != null ? emploi.profilSI.id : "''"},
            
            async submitForm() {
                this.loading = true;
                this.message = '';
                this.messageType = '';

                try{
                    if(!this.emploiName.trim()) {
                        this.messageType = 'error';
                        this.message = 'Le nom de l\'emploi est requis';
                        this.loading = false;
                        return;
                    }
                    
                    const data = {
                        emploi: {
                            emploi: this.emploiName,
                            direction: this.selectedDirection,
                            status: this.selectedStatus,
                            service: this.selectedService ? parseInt(this.selectedService) : null,
                            domaine: this.selectedDomaine ? parseInt(this.selectedDomaine) : null,
                        },
                        profilSI: this.selectedProfilSI
                    };

                    const response = await fetch('/api/emplois/' + this.emploiId, {
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(data)
                    });

                    if (response.ok) {
                        this.messageType = 'success';
                        this.message = 'Emploi mis à jour avec succès !';

                        setTimeout(() => {
                            window.location.href = '/emploi';
                        }, 1500);
                    } else {
                        const error = await response.json();
                        this.messageType = 'error';
                        this.message = error.message || 'Erreur lors de la modification de l\'emploi';
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