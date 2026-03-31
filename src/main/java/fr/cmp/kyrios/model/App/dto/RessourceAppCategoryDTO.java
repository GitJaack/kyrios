package fr.cmp.kyrios.model.App.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RessourceAppCategoryDTO {
    @Schema(description = "ID de la categorie", example = "1")
    private int id;

    @Schema(description = "Nom de la categorie", example = "Code ecran")
    private String name;

    @Schema(description = "Ressources de la categorie")
    private List<ResourceItem> resources;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ResourceItem {
        @Schema(description = "ID de la ressource", example = "1")
        private int id;

        @Schema(description = "Nom de la ressource", example = "BTSY")
        private String name;

        @Schema(description = "Description de la ressource", example = "Acces Synthese Client")
        private String description;
    }
}
