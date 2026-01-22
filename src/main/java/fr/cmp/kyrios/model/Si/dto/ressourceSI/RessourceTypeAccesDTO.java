package fr.cmp.kyrios.model.Si.dto.ressourceSI;

import fr.cmp.kyrios.model.Si.RessourceSIModel.TypeAcces;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class RessourceTypeAccesDTO {
    @NotNull(message = "L'ID de la ressource est obligatoire")
    @Positive(message = "L'ID de la ressource doit être positif")
    @Schema(description = "ID de la ressource SI", example = "1")
    private Integer ressourceId;

    @NotNull(message = "Le type d'accès est obligatoire")
    @Schema(description = "Type d'accès pour cette ressource", example = "LECTURE")
    private TypeAcces typeAcces;
}
