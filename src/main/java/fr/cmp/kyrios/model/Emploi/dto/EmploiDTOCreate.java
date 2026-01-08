package fr.cmp.kyrios.model.Emploi.dto;

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

public class EmploiDTOCreate {
    private EmploiDTO emploi;

    @NotNull(message = "Le profil SI est requis")
    @Schema(description = "Profil SI associé", example = "1")
    private int profilSI;
}
