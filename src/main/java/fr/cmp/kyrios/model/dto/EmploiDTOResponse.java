package fr.cmp.kyrios.model.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fr.cmp.kyrios.model.EmploiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonPropertyOrder({ "id", "emploi", "direction", "service", "domaine", "status", "profilSI" })
public class EmploiDTOResponse {
    @Schema(description = "ID unique de l'emploi", example = "1")
    private int id;

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

    @Schema(description = "Profil SI associé avec ID et nom")
    private ProfilSISimpleDTO profilSI;

    @Schema(description = "Date de création de l'emploi", example = "2026-01-15T10:00:00")
    private LocalDateTime dateCreated;

    @Schema(description = "Date de la dernière mise à jour de l'emploi", example = "2026-02-20T15:30:00")
    private LocalDateTime dateUpdated;
}
