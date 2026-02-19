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
                    Vous êtes sur le point de supprimer l'emploi : <strong x-text="selectedEmploiName"></strong>
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
    function emploiManager() {
        return {
            showModal: false,
            selectedEmploiId: null,
            selectedEmploiName: '',
            loading: false,

            showDeleteModal(emploiId, emploiName) {
                this.selectedEmploiId = emploiId;
                this.selectedEmploiName = emploiName;
                this.showModal = true;
            },

            async confirmDelete() {
                this.loading = true;
                try {
                    const response = await fetch('api/emplois/' + this.selectedEmploiId, {
                        method: 'DELETE',
                    });

                    if (response.ok) {
                        setTimeout(() => {
                            window.location.reload();
                        }, 500);
                    } else {
                        const error = await response.json();
                        alert('Erreur : ' + error.message || 'Impossible de supprimer l\'emploi.');
                        this.loading = false;
                    }
                } catch (error) {
                    console.error('Error:', error);
                    alert('Une erreur est survenue lors de la suppression de l\'emploi.');
                } finally {
                    this.loading = false;
                }
            },

            closeModal() {
                this.showModal = false;
                this.selectedEmploiId = null;
                this.selectedEmploiName = '';
            }
        };
    }
</script>
