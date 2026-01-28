package fr.cmp.kyrios.model.App.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RessourceAppDTOCreate {
    @NotNull(message = "L'ID de l'application est requis")
    @Schema(description = "ID de l'application associée", example = "1")
    private Integer applicationId;

    @NotNull(message = "Le nom de la ressource est requis")
    @Schema(description = "Nom de la ressource", example = "BTSY")
    private String name;

    @Schema(description = "Description de la ressource", example = "Acces Synthese Client")
    private String description;
}
