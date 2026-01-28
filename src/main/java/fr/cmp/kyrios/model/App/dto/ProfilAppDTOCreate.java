package fr.cmp.kyrios.model.App.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
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
    private Integer applicationId;

    @NotEmpty(message = "Au moins un profil SI est requis")
    @Schema(description = "Liste des IDs des profils SI associés", example = "[1, 2]")
    private List<Integer> profilSIIds;

    @Schema(description = "Liste des IDs des ressources d'application associées (optionnel)", example = "[1, 2, 3]")
    private List<Integer> ressourceAppIds;
}
