<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<template x-if="showModal">
    <div x-show="showModal" 
        x-transition
        class="modal-overlay" 
        style="display: flex;"
        @click.self="closeModal()">
        <div class="modal-content">
            <div class="modal-header">
                <h2>Confirmer la suppression</h2>
                <button type="button" class="modal-close" @click="closeModal()">
                    <img src="/images/x.svg" alt="x icon">
                </button>
            </div>

            <div class="modal-body">
                <p class="modal-message">
                    Suppression du profil SI : <strong x-text="selectedProfilName"></strong>
                </p>

                <div class="warning-box">
                    <p class="warning-title">Emplois impactés :</p>
                    <ul class="emplois-list">
                        <template x-for="emploi in affectedEmplois" :key="emploi.id">
                            <li x-text="'n°' + emploi.id + ' - ' + emploi.emploiName"></li>
                        </template>
                    </ul>
                </div>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn-cancel" @click="closeModal()">Annuler</button>
                <button type="button" class="btn-delete" @click="confirmDelete()" :disabled="loading">
                    <span x-text="loading ? 'Suppression...' : 'Supprimer'"></span>
                </button>
            </div>
        </div>
    </div>
</template>

<script>
    function profilSIManager() {
        return {
            showModal: false,
            selectedProfilId: null,
            selectedProfilName: '',
            affectedEmplois: [],
            loading: false,

            showDeleteModal(profilId, profilName) {
                this.selectedProfilId = profilId;
                this.selectedProfilName = profilName;
                this.affectedEmplois = [];
                this.showModal = true;
                this.loadAffectedEmplois();
            },

            async loadAffectedEmplois() {
                try {
                    const response = await fetch('/api/profils-si/' + this.selectedProfilId);
                    if (response.ok) {
                        const profil = await response.json();
                        this.affectedEmplois = profil.emplois || [];
                    }
                } catch (error) {
                    console.error('Erreur lors du chargement des données:', error);
                }
            },

            async confirmDelete() {
                this.loading = true;

                try {
                    const response = await fetch('/api/profils-si/' + this.selectedProfilId, {
                        method: 'DELETE'
                    });

                    if (response.ok) {
                        setTimeout(() => {
                            window.location.reload();
                        }, 500);
                    } else {
                        const error = await response.json();
                        alert('Erreur : ' + (error.message || 'Impossible de supprimer le profil SI'));
                        this.loading = false;
                    }
                } catch (error) {
                    console.error('Erreur:', error);
                    alert('Une erreur est survenue lors de la suppression du profil SI.');
                    this.loading = false;
                }
            },

            closeModal() {
                this.showModal = false;
                this.selectedProfilId = null;
                this.selectedProfilName = '';
                this.affectedEmplois = [];
                this.loading = false;
            }
        }
    }
</script>
