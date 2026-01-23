package fr.cmp.kyrios.model.App.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppDTOResponse {
    @Schema(description = "ID unique de l'application", example = "1")
    private int id;

    @Schema(description = "Nom de l'application", example = "THEMIS")
    private String name;

    @Schema(description = "Direction associée", example = "Direction des Systèmes d'Information")
    private String direction;

    @Schema(description = "Description de l'application", example = "Application de gestion des ressources")
    private String description;

    @Schema(description = "Date de création de l'application", example = "2026-01-15T10:00:00")
    private LocalDateTime dateCreated;
}
