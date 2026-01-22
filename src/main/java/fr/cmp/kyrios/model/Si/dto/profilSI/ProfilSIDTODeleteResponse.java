package fr.cmp.kyrios.model.Si.dto.profilSI;

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
public class ProfilSIDTODeleteResponse {
    @Schema(description = "Message de confirmation de la suppression du profil SI", example = "Profil SI 'Developpeur fullstack' supprimé avec succès. 1 emploi(s) ont été détaché(s).")
    private String message;
    private List<EmploiInfo> emploisDetaches;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EmploiInfo {
        @Schema(description = "ID unique de l'emploi", example = "1")
        private int id;

        @Schema(description = "Nom de l'emploi", example = "Developpeur informatique")
        private String nom;
    }
}
