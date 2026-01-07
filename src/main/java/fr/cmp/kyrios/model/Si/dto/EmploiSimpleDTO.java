package fr.cmp.kyrios.model.Si.dto;

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
public class EmploiSimpleDTO {
    @Schema(description = "ID unique de l'emploi", example = "1")
    private int id;

    @Schema(description = "Nom de l'emploi", example = "Developpeur informatique")
    private String emploi;

    @Schema(description = "Direction de l'emploi", example = "1")
    private int direction;

    @Schema(description = "Service de l'emploi", example = "null")
    private Integer service;

    @Schema(description = "Domaine de l'emploi", example = "null")
    private Integer domaine;

    @Schema(description = "Statut de l'emploi", example = "PERMANENT")
    private EmploiModel.Status status;
}