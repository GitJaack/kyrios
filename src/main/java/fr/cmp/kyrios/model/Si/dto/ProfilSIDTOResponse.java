package fr.cmp.kyrios.model.Si.dto;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfilSIDTOResponse {
    @Schema(description = "ID unique du profil SI", example = "1")
    private int idProfilSI;

    @Schema(description = "Nom du profil SI", example = "Developpeur fullstack")
    private String name;

    private List<RessourceSIDTO> ressources;

    @Schema(description = "Date de création du profil SI", example = "2026-01-15T10:00:00")
    private LocalDateTime dateCreated;

    @Schema(description = "Date de la dernière mise à jour du profil SI", example = "2026-02-20T15:30:00")
    private LocalDateTime dateUpdated;
}
