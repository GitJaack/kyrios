package fr.cmp.kyrios.model.Si.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProfilSIUpdateDTO {
    @NotBlank(message = "Le profil SI ne peut pas être vide")
    @Size(max = 50, message = "Le profil SI ne doit pas dépasser 50 caractères")
    @Schema(description = "Nom du profil SI", example = "Developpeur fullstack")
    private String profilSI;

    @Schema(description = "Liste des IDs des ressources SI associées au profil SI", example = "[1, 2, 3]")
    private List<Integer> ressourceIds;
}
