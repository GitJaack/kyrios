package fr.cmp.kyrios.model.Si.dto;

import fr.cmp.kyrios.model.Si.RessourceSIModel.TypeAcces;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RessourceSIDTO {
    @Schema(description = "ID unique de la ressource SI", example = "1")
    private int id;

    @Schema(description = "Catégorie de la ressource SI", example = "Repertoire de services")
    private String categorie;

    @Schema(description = "Libellé d'accès de la ressource SI", example = "SVC_Agence Comptable")
    private String libelleAcces;

    @Schema(description = "Type d'accès de la ressource SI", example = "LECTURE")
    private TypeAcces typeAcces;
    private boolean isDefault;
    // private boolean selected;
}
