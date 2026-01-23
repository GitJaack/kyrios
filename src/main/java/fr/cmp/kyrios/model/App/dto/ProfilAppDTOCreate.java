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
public class ProfilAppDTOCreate {
    @NotNull(message = "Le nom du profil d'application est requis")
    @Schema(description = "Nom du profil d'application", example = "Developpeur")
    private String name;

    @NotNull(message = "L'ID de l'application est requis")
    @Schema(description = "ID de l'application associée", example = "1")
    private int applicationId;

    @NotNull(message = "L'ID du profil SI est requis")
    @Schema(description = "ID du profil SI associé", example = "1")
    private int profilSIId;
}
