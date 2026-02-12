<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="content-body">
    <div class="content-above">
        <div class="container-header">
            <a href="/emploi">
                <div class="back-button">
                    <img src="/images/arrow-left.svg" alt="arrow-left">
                    <span>Annuler</span>
                </div>
            </a>

            <div class="content-header-right">
                <button class="save-button" type="submit" form="createEmploiForm">
                    <img src="/images/save.svg" alt="save icon">
                    <span>Enregistrer</span>
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
                    <input type="text" id="emploi" name="emploi" required 
                           placeholder="Entrer un nom d'emploi" maxlength="50">
                </div>

                <div class="form-group">
                    <label for="direction">Direction <span class="required">*</span></label>
                    <select id="direction" name="direction" required>
                        <option value="" disabled selected>Sélectionner une direction</option>
                        <c:forEach var="direction" items="${directions}">
                            <option value="${direction.id}">${direction.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="service">Service</label>
                    <select id="service" name="service">
                        <option value="">Aucun service</option>
                        <c:forEach var="service" items="${services}">
                            <option value="${service.id}">${service.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="domaine">Domaine</label>
                    <select id="domaine" name="domaine">
                        <option value="">Aucun domaine</option>
                        <c:forEach var="domaine" items="${domaines}">
                            <option value="${domaine.id}">${domaine.name}</option>
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
document.getElementById('createEmploiForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    
    const emploiName = document.getElementById('emploi').value;
    const direction = document.getElementById('direction').value;
    const service = document.getElementById('service').value;
    const domaine = document.getElementById('domaine').value;
    const status = document.getElementById('status').value;
    const profilSI = document.getElementById('profilSI').value;
    
    // Construire l'objet selon le format EmploiDTOCreate
    const data = {
        emploi: {
            emploi: emploiName,
            direction: parseInt(direction),
            service: service ? parseInt(service) : null,
            domaine: domaine ? parseInt(domaine) : null,
            status: status
        },
        profilSI: parseInt(profilSI)
    };
    
    try {
        const response = await fetch('/api/emplois', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data)
        });
        
        if (response.ok) {
            // Rediriger vers la liste des emplois ou afficher un message de succès
            alert('Emploi créé avec succès!');
            window.location.href = '/emploi';
        } else {
            const error = await response.json();
            alert('Erreur lors de la création: ' + (error.message || 'Erreur inconnue'));
        }
    } catch (error) {
        console.error('Erreur:', error);
        alert('Erreur lors de la création de l\'emploi');
    }
});
</script>

<style>
.form-group {
    margin-bottom: 1.25rem;
}

.form-group label {
    display: block;
    font-weight: 500;
    font-size: 0.875rem;
    margin-bottom: 0.5rem;
    color: hsl(220, 10%, 20%);
}

.form-group .required {
    color: hsl(0, 70%, 50%);
}

.form-group input[type="text"],
.form-group select {
    width: 100%;
    padding: 0.625rem 0.875rem;
    border: 1px solid hsl(220, 10%, 85%);
    border-radius: 0.375rem;
    font-size: 0.875rem;
    font-family: inherit;
    background-color: white;
    transition: border-color 0.2s;
}

.form-group input[type="text"]:focus,
.form-group select:focus {
    outline: none;
    border-color: hsl(220, 70%, 60%);
    box-shadow: 0 0 0 3px hsla(220, 70%, 60%, 0.1);
}

.form-group input[type="text"]::placeholder {
    color: hsl(220, 10%, 60%);
}

.form-description {
    font-size: 0.875rem;
    color: hsl(220, 10%, 50%);
    margin-bottom: 1rem;
}
</style>
