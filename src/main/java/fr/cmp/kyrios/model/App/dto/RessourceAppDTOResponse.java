package fr.cmp.kyrios.model.App.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RessourceAppDTOResponse {
    @Schema(description = "ID de la ressource", example = "1")
    private int id;

    @Schema(description = "Nom de l'application associée", example = "THEMIS")
    private String application;

    @Schema(description = "Nom de la ressource", example = "BTSY")
    private String name;

    @Schema(description = "Description de la ressource", example = "Acces Synthese Client")
    private String description;
}
