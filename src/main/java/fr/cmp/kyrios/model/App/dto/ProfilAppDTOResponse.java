package fr.cmp.kyrios.model.App.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfilAppDTOResponse {
    @Schema(description = "ID unique du profil d'application", example = "1")
    private int id;

    @Schema(description = "Nom du profil d'application", example = "Developpeur")
    private String name;

    @Schema(description = "Nom de l'application associée", example = "THEMIS")
    private String application;

    @Schema(description = "Nom du profil SI associé", example = "Developpeur")
    private String profilSI;

    @Schema(description = "Date de création du profil d'application", example = "2026-01-15T10:00:00")
    private LocalDateTime dateCreated;

    @Schema(description = "Date de la dernière mise à jour du profil d'application", example = "2026-02-20T15:30:00")
    private LocalDateTime dateUpdated;
}
