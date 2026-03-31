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
												<input type="checkbox" :id="'resource' + resource.id" :value="String(resource.id)" x-model="selectedRessourceIds">
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
		permissionLevel: '${profilApp.permissionLevel != null ? profilApp.permissionLevel : 0}',
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
				const response = await fetch('/api/ressources-app/by-categorie?applicationId=' + this.applicationId);
				if (!response.ok) {
					throw new Error('Erreur lors du chargement des ressources');
				}
				const data = await response.json();
				this.groupedResources = this.normalizeGroupedResources(data);
				this.resources = this.groupedResources.flatMap(group => group.resources || []);
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
					ressourceAppIds: this.selectedRessourceIds.map(id => parseInt(id)),
					permissionLevel: this.selectedRessourceIds.some(id => {
						const res = this.resources.find(r => String(r.id) === id);
						return res && res.name === 'Niveau de permission';
					}) ? parseInt(this.permissionLevel) : null
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