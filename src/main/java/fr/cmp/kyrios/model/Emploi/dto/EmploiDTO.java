package fr.cmp.kyrios.model.Emploi.dto;

import fr.cmp.kyrios.model.Emploi.EmploiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmploiDTO {
    @NotBlank(message = "Le nom de l'emploi ne peut pas être vide")
    @Size(max = 50, message = "Le nom ne doit pas dépasser 50 caractères")
    @Schema(description = "Nom de l'emploi", example = "Developpeur informatique")
    private String emploi;

    @NotNull(message = "La direction est requise")
    @Schema(description = "Direction de l'emploi", example = "1")
    private Integer direction;

    @Schema(description = "Service de l'emploi (optionnel)", example = "null")
    private Integer service;

    @Schema(description = "Domaine de l'emploi (optionnel)", example = "null")
    private Integer domaine;

    @NotNull(message = "Le statut est requis")
    @Schema(description = "Statut de l'emploi", example = "PERMANENT")
    private EmploiModel.Status status;
}