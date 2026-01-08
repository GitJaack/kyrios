package fr.cmp.kyrios.model.Si.dto;

import java.time.LocalDateTime;
import java.util.List;

import fr.cmp.kyrios.model.Emploi.EmploiModel;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfilSIDTOResponse {
    // Informations de l'emploi créé et lié
    @Schema(description = "ID unique de l'emploi", example = "1")
    private int idEmploi;

    @Schema(description = "Nom de l'emploi", example = "Developpeur informatique")
    private String emploi;

    @Schema(description = "Direction associée", example = "Direction des Systemes d'Information")
    private String direction;

    @Schema(description = "Service associé", example = "Infrastructure")
    private String service;

    @Schema(description = "Domaine associé", example = "Java")
    private String domaine;

    @Schema(description = "Statut de l'emploi", example = "PERMANENT")
    private EmploiModel.Status status;

    // Informations du profil SI
    @Schema(description = "ID unique du profil SI", example = "1")
    private int idProfilSI;

    @Schema(description = "Nom du profil SI", example = "Developpeur fullstack")
    private String name;

    private List<RessourceSIDTO> ressources;

    @Schema(description = "Date de création du profil SI", example = "2026-01-15T10:00:00")
    private LocalDateTime dateCreated;

    @Schema(description = "Date de la dernière mise à jour du profil SI", example = "2026-02-20T15:30:00")
    private LocalDateTime dateUpdated;
}
