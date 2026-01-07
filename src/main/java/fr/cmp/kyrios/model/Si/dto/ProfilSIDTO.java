package fr.cmp.kyrios.model.Si.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfilSIDTO {

    public enum ModeCreation {
        NOUVEAU,
        COPIER
    }

    // Etape 1 : Informations de l'emploi
    private EmploiSimpleDTO emploi;

    // Etape 2 : Informations du profil SI
    @NotBlank(message = "Le profil SI ne peut pas être vide")
    @Size(max = 50, message = "Le profil SI ne doit pas dépasser 50 caractères")
    @Schema(description = "Nom du profil SI", example = "Developpeur fullstack")
    private String profilSI;

    @NotNull(message = "Le mode de création est requis")
    @Schema(description = "Mode de création du profil SI", example = "NOUVEAU")
    private ModeCreation modeCreation;

    @Schema(description = "ID du profil SI source pour la copie", example = "null lors mode NOUVEAU ou ID du profil à copier lors mode COPIER")
    private Integer profilSISourceId;

    private List<Integer> ressourceIds;

}
