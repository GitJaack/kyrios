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
                    Vous êtes sur le point de supprimer le profil applicatif : <strong x-text="selectedProfilAppName"></strong>
                </p>
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
    function profilAppManager() {
        return {
            activeTab: ${!empty apps ? apps[0].id : 0},
            showModal: false,
            selectedProfilAppId: null,
            selectedProfilAppName: '',
            loading: false,

            showDeleteModal(profilAppId, profilAppName) {
                this.selectedProfilAppId = profilAppId;
                this.selectedProfilAppName = profilAppName;
                this.showModal = true;
            },

            async confirmDelete() {
                this.loading = true;
                try {
                    const response = await fetch('/api/profil-app/' + this.selectedProfilAppId, {
                        method: 'DELETE',
                    });

                    if (response.ok) {
                        setTimeout(() => {
                            window.location.reload();
                        }, 500);
                    } else {
                        const error = await response.json();
                        alert('Erreur : ' + error.message || 'Impossible de supprimer le profil applicatif.');
                        this.loading = false;
                    }
                } catch (error) {
                    console.error('Error:', error);
                    alert('Une erreur est survenue lors de la suppression du profil applicatif.');
                } finally {
                    this.loading = false;
                }
            },

            closeModal() {
                this.showModal = false;
                this.selectedProfilAppId = null;
                this.selectedProfilAppName = '';
            }
        };
    }
</script>
