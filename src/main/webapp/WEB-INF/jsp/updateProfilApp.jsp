<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="content-body" x-data="profilAppUpdateForm()" x-init="init()" @submit.prevent="submitForm()">
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
					<span x-text="loading ? 'Enregistrement...' : 'Enregistrer' "></span>
				</button>
			</div>
		</div>
	</div>

	<div class="content-form">
		<input type="hidden" id="initialProfilAppName" value="<c:out value='${profilApp.name}' />">
		<input type="hidden" id="initialApplicationName" value="<c:out value='${profilApp.application}' />">

		<form id="updateProfilAppForm" action="#" method="post" @submit.prevent="submitForm()">
			<div class="form-section">
				<h3>Informations generales</h3>
				<div class="form-group">
					<label for="profilapp">Nom du profil applicatif <span class="required">*</span></label>
					<input type="text" id="profilapp" name="profilapp" required placeholder="Entrer un nom de profil applicatif" x-model="profilAppName">
				</div>

				<div class="form-group">
					<label for="application">Application associee</label>
					<input type="text" id="application" name="application" x-model="applicationName" disabled>
				</div>
			</div>

			<div class="form-section">
				<h3>Profil SI associes</h3>

				<div class="ressources-grid">
					<c:forEach var="profilSI" items="${profilsSI}">
						<label class="ressource-item">
							<div class="ressource-checkbox">
								<input
									type="checkbox"
									id="profilSI${profilSI.id}"
									name="profilSI"
									value="${profilSI.id}"
									x-model="selectedProfilSIIds"
								>
								<label for="profilSI${profilSI.id}">${profilSI.name}</label>
							</div>
						</label>
					</c:forEach>
				</div>
			</div>

			<div class="form-section">
				<h3>Ressources de l'application</h3>

				<div>
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
											<input type="checkbox" :id="'resource' + resource.id" :value="resource.id" x-model="selectedRessourceIds">
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
			</div>
		</form>
	</div>
</div>

<script>
function profilAppUpdateForm() {
	return {
		profilAppId: ${profilApp.id},
		loading: false,
		message: '',
		messageType: '',
		applicationId: ${profilApp.applicationId},
		existingRessourceNames: [
			<c:forEach var="ressourceName" items="${profilApp.ressourcesApp}" varStatus="status">
				'${ressourceName}'<c:if test="${!status.last}">,</c:if>
			</c:forEach>
		],
		applicationName: '',
		profilAppName: '',
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
		selectedProfilSIIds: [
			<c:forEach var="profilSI" items="${profilApp.profilSI}" varStatus="status">
				'${profilSI.id}'<c:if test="${!status.last}">,</c:if>
			</c:forEach>
		],
		selectedRessourceIds: [],

		init() {
			this.profilAppName = document.getElementById('initialProfilAppName').value;
			this.applicationName = document.getElementById('initialApplicationName').value;
			this.loadResources();
		},

		async loadResources() {
			this.loadingResources = true;
			try {
				const response = await fetch('/api/ressources-app/ressource-application/?id=' + this.applicationId);
				if (!response.ok) {
					throw new Error('Erreur lors du chargement des ressources');
				}
				this.resources = await response.json();
				this.groupedResources = this.buildGroupedResources(this.resources);
				this.selectedRessourceIds = this.resources
					.filter(resource => this.existingRessourceNames.includes(resource.name))
					.map(resource => String(resource.id));
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
				if (!this.profilAppName.trim()) {
					this.messageType = 'error';
					this.message = 'Le nom du profil applicatif ne peut pas etre vide';
					this.loading = false;
					return;
				}

				if (this.selectedProfilSIIds.length === 0) {
					this.messageType = 'error';
					this.message = 'Veuillez selectionner au moins un profil SI associe';
					this.loading = false;
					return;
				}

				const data = {
					name: this.profilAppName,
					applicationId: parseInt(this.applicationId),
					profilSIIds: this.selectedProfilSIIds.map(id => parseInt(id)),
					ressourceAppIds: this.selectedRessourceIds.map(id => parseInt(id))
				};

				const response = await fetch('/api/profil-app/' + this.profilAppId, {
					method: 'PUT',
					headers: {
						'Content-Type': 'application/json'
					},
					body: JSON.stringify(data)
				});

				if (response.ok) {
					this.messageType = 'success';
					this.message = 'Profil applicatif mis a jour avec succes !';

					setTimeout(() => {
						window.location.href = '/profil-app';
					}, 1500);
				} else {
					const errorData = await response.json();
					throw new Error(errorData.message || 'Erreur lors de la modification du profil applicatif');
				}
			} catch (error) {
				console.error('Erreur:', error);
				this.messageType = 'error';
				this.message = error.message || 'Erreur lors de la modification du profil applicatif';
			} finally {
				this.loading = false;
			}
		}
	}
}
</script>