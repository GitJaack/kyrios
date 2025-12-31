package fr.cmp.kyrios.model.dto;

import fr.cmp.kyrios.model.EmploiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmploiDTO {
    @NotBlank(message = "Le nom de l'emploi ne peut pas être vide")
    @Size(max = 50, message = "Le nom ne doit pas dépasser 50 caractères")
    @Schema(description = "Nom de l'emploi", example = "Développeur informatique Fullstack (Java)")
    private String emploi;

    @NotNull(message = "La direction est requise")
    @Schema(description = "Direction associée", example = "Direction des Systèmes d'Information")
    private String direction;

    @Schema(description = "Service associé (optionnel)", example = "null")
    private String service;

    @Schema(description = "Domaine associé (optionnel)", example = "null")
    private String domaine;

    @NotNull(message = "Le statut est requis")
    @Schema(description = "Statut de l'emploi", example = "PERMANENT")
    private EmploiModel.Status status;

    @NotNull(message = "L'ID du profil SI est requis")
    @Schema(description = "Profil SI lié à cet emploi", example = "Developpeur fullstack")
    private int profilSIid;
}
