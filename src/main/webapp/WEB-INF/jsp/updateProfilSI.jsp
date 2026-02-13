<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="content-body" x-data="profilSIUpdateForm(${profilId})" x-init="init()">
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
		<form id="updateProfilForm" action="#" method="post" @submit.prevent="submitForm()">
			<div class="form-section">
				<h3>Informations du profil SI</h3>

				<div class="form-group">
					<label for="name">Nom du profil SI <span class="required">*</span></label>
					<input type="text" id="name" name="name" required placeholder="Entrer un nom de profil SI" x-model="profilName">
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
	function profilSIUpdateForm(profilId) {
		return {
			profilId: profilId,
			loading: false,
			message: '',
			messageType: '',
			profilName: '',
			directionName: '',
			directionId: ${directionId},
			selectedRessources: {},

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

				this.loadProfil();
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

			async loadProfil() {
				try {
					const response = await fetch('/api/profils-si/' + this.profilId);
					if (!response.ok) {
						this.messageType = 'error';
						this.message = 'Impossible de charger le profil SI';
						return;
					}

					const profil = await response.json();
					this.profilName = profil.name || '';
					this.directionName = profil.direction || '';

					this.resetRessources();
					const directionId = this.directionId;
					const ressourcesDefault = directionId ? (this.directionRessourcesMap[directionId] || []) : [];
					this.applyDefaultRessources(ressourcesDefault);

					if (profil.ressources && profil.ressources.length) {
						profil.ressources.forEach(ressource => {
							if (this.selectedRessources[ressource.id]) {
								this.selectedRessources[ressource.id].checked = true;
								this.selectedRessources[ressource.id].typeAcces = ressource.typeAcces;
							}
						});
					}
				} catch (error) {
					console.error('Erreur de chargement', error);
					this.messageType = 'error';
					this.message = 'Erreur de connexion au serveur';
				}
			},

			async submitForm() {
				this.loading = true;
				this.message = '';
				this.messageType = '';

				try {
					if (!this.profilName.trim()) {
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
						profilSI: this.profilName,
						ressources: ressources
					};

					const response = await fetch('/api/profils-si/' + this.profilId, {
						method: 'PUT',
						headers: {
							'Content-Type': 'application/json'
						},
						body: JSON.stringify(data)
					});

					if (response.ok) {
						this.messageType = 'success';
						this.message = 'Profil SI modifié avec succès!';

						setTimeout(() => {
							window.location.href = '/profilSI';
						}, 1500);
					} else {
						const error = await response.json();
						this.messageType = 'error';
						this.message = error.message || 'Erreur lors de la modification du profil SI';
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