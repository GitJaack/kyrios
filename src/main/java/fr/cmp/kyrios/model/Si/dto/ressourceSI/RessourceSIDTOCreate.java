package fr.cmp.kyrios.model.Si.dto.ressourceSI;

import fr.cmp.kyrios.model.Si.RessourceSIModel.TypeAcces;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RessourceSIDTOCreate {
    @NotNull(message = "La catégorie est obligatoire")
    @Positive(message = "L'ID de la catégorie doit être positif")
    @Schema(description = "Catégorie de la ressource SI", example = "1")
    private Integer categorie;

    @NotBlank(message = "Le nom est obligatoire")
    @Schema(description = "Libellé d'accès de la ressource SI", example = "SVC_Agence Comptable")
    private String name;

    @NotNull(message = "Le type d'accès est obligatoire")
    @Schema(description = "Type d'accès de la ressource SI", example = "LECTURE")
    private TypeAcces typeAcces;
}
