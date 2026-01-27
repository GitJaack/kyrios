package fr.cmp.kyrios.model.App.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfilAppDTODeleteResponse {
    @Schema(description = "Message de confirmation de la suppression du profil d'application", example = "Profil d'application 'Developpeur fullstack' supprimé avec succès : 1 profil a été détaché")
    private String message;
    private List<ProfilSIInfo> profilsSIDetaches;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProfilSIInfo {
        @Schema(description = "ID unique du profil SI", example = "1")
        private int id;

        @Schema(description = "Nom du profil SI", example = "Developpeur Fullstack")
        private String name;
    }
}
