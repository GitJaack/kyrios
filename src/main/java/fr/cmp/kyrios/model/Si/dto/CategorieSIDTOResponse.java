package fr.cmp.kyrios.model.Si.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategorieSIDTOResponse {
    @Schema(description = "ID de la catégorie", example = "1")
    private int id;

    @Schema(description = "Nom de la catégorie", example = "Répertoires de service")
    private String name;
}
