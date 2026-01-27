package fr.cmp.kyrios.model.App.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfilAppDTOSimple {
    @Schema(description = "ID unique du profil d'application", example = "1")
    private int id;

    @Schema(description = "Nom du profil d'application", example = "Developpeur")
    private String name;

    @Schema(description = "Nom de l'application associée", example = "THEMIS")
    private String application;
}
