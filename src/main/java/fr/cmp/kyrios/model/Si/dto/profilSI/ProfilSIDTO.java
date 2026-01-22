package fr.cmp.kyrios.model.Si.dto.profilSI;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProfilSIDTO extends ProfilSIUpdateDTO {

    public enum ModeCreation {
        NOUVEAU,
        COPIER
    }

    @NotNull(message = "Le mode de création est requis")
    @Schema(description = "Mode de création du profil SI", example = "NOUVEAU")
    private ModeCreation modeCreation;

    @Schema(description = "ID du profil SI source pour la copie", example = "null lors mode NOUVEAU ou ID du profil à copier lors mode COPIER")
    private Integer profilSISourceId;
}
