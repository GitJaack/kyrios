package fr.cmp.kyrios.model.Si.dto.categorieSI;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class CategorieSIDTOCreate {
    @NotBlank(message = "Le nom de la catégorie ne peut pas être vide")
    @Size(max = 50, message = "Le nom ne doit pas dépasser 50 caractères")
    @Schema(description = "Nom de la catégorie", example = "Répertoires de service")
    private String name;
}
