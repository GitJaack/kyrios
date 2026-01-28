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
public class RessourceAppDTOUpdate {
    @NotNull(message = "Le nom de la ressource est requis")
    @Schema(description = "Nom de la ressource", example = "BTSY")
    private String name;

    @Schema(description = "Description de la ressource", example = "Acces Synthese Client")
    private String description;
}
