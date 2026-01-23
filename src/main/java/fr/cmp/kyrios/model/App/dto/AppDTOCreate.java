package fr.cmp.kyrios.model.App.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppDTOCreate {
    @NotNull(message = "Le nom de l'application est requis")
    @Schema(description = "Nom de l'application", example = "THEMIS")
    private String name;

    @NotNull(message = "La direction est requise")
    @Schema(description = "ID de la direction associée", example = "1")
    private Integer directionId;

    @Schema(description = "Description de l'application", example = "Application de gestion des ressources")
    private String description;
}
