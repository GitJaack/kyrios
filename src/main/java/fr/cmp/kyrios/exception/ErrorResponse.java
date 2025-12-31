package fr.cmp.kyrios.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Réponse d'erreur standard")
public class ErrorResponse {
    @Schema(description = "Message d'erreur", example = "Emploi non trouvé")
    private String message;
}
