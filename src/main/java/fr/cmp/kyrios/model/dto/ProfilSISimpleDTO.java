package fr.cmp.kyrios.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Représentation simplifiée d'un profil SI")
public class ProfilSISimpleDTO {
    @Schema(description = "ID unique du profil SI", example = "1")
    private int id;

    @Schema(description = "Nom du profil SI", example = "Developpeur fullstack")
    private String name;
}
